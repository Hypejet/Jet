package net.hypejet.jet.server.world.handler;

import com.google.common.collect.Ordering;
import net.hypejet.concurrency.empty.EmptyAcquirable;
import net.hypejet.concurrency.empty.EmptyAcquisition;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import java.util.Objects;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.entity.acquisition.world.EntityWorldAcquisition;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchFinishedPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchStartPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerInvalidateChunkPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.coordinate.ChunkPositionUtil;
import net.hypejet.jet.server.world.acquisition.worldmap.WriteWorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.view.ChunkView;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.event.world.events.StartWaitingForWorldChunksWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

/**
 * Represents something that handles sending batches of {@linkplain JetChunk chunks} to a client.
 *
 * @since 1.0
 * @see JetChunk
 */
public final class ChunkBatchHandler implements AutoCloseable, NetworkDisconnectionHandler {

    /**
     * A minimum view distance that {@linkplain JetPlayer a player} can have.
     *
     * @since 1.0
     */
    public static final byte MINIMUM_VIEW_DISTANCE = 2;

    private static final float MINIMUM_CHUNKS_PER_TICK = 0.01f;
    private static final float MAXIMUM_CHUNKS_PER_TICK = 64f;

    private static final byte SHUTDOWN_TIMEOUT = 20;
    private static final TimeUnit SHUTDOWN_TIMEOUT_UNIT = TimeUnit.SECONDS;

    private final JetPlayer player;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final EmptyAcquirable lock = new EmptyAcquirable();
    private final Set<ChunkPosition> chunksScheduled = new HashSet<>();

    private @NonNull ChunkView chunkView;
    private @Nullable ScheduledFuture<?> future;

    private float chunksPerTick = 9f;
    private float maximumUnacknowledgedBatches = 1f;

    private float chunksToSend = 0f;
    private float unacknowledgedBatches = 0f;

    /**
     * Constructs the {@linkplain ChunkBatchHandler chunk batch handler}.
     *
     * @param player a player that the chunks should be sent to
     * @param position an initial position of the player
     * @since 1.0
     */
    public ChunkBatchHandler(@NonNull JetPlayer player, @NonNull Position position) {
        this.player = Objects.requireNonNull(player, "player");
        Objects.requireNonNull(position, "position");

        try (NotNullObjectAcquisition<Player.Settings> settingsAcquisition = this.player.settings()) {
            ChunkPosition centerChunkPosition = ChunkPositionUtil.fromCoordinate(position);
            byte viewDistance = this.createViewDistance(settingsAcquisition.get().viewDistance());
            this.chunkView = new ChunkView(centerChunkPosition, viewDistance);
        }
    }

    /**
     * Consumes {@linkplain ChunkView a chunk view} and {@linkplain Set a set}
     * of {@linkplain ChunkPosition chunk positions} of {@linkplain JetChunk chunks} that are pending to be sent
     * to a client.
     *
     * @param consumer a bi-consumer to consume the data with
     * @since 1.0
     */
    public void consumeChunkData(@NonNull BiConsumer<ChunkView, Set<ChunkPosition>> consumer) {
        try (EmptyAcquisition ignored = this.lock.acquireRead()) {
            consumer.accept(this.chunkView, this.chunksScheduled);
        }
    }

    /**
     * Schedules a task, which sends chunks to the player.
     *
     * @throws IllegalStateException if the task has been already scheduled or settings of the play have not been set
     * @since 1.0
     */
    public void scheduleTask() {
        try (EmptyAcquisition ignored = this.lock.acquireWrite()) {
            if (this.future != null)
                throw new IllegalStateException("The task has been already scheduled");

            this.player.sendPacket(new ServerWorldEventPlayPacket(StartWaitingForWorldChunksWorldEvent.INSTANCE));
            this.scheduleChunks(null);

            this.future = this.executorService.scheduleAtFixedRate(
                    this::tick,
                    0L, 50L,
                    TimeUnit.MILLISECONDS
            ); // TODO: Replace with a tick system when it gets implemented
        }
    }

    /**
     * Stops a task, which sends chunks to the player, then awaits for when the task is fully stopped. Finally, all
     * chunks are removed from {@linkplain java.util.Collection collection} of scheduled chunks. Does nothing
     * if the task has not been scheduled.
     *
     * @throws IllegalStateException if a future handling the task has been completed successfully
     * @since 1.0
     */
    public void cancelTask() {
        try (EmptyAcquisition ignored = this.lock.acquireWrite()) {
            if (this.future == null) return;
            this.future.cancel(false);

            try {
                this.future.get();
                throw new IllegalStateException("The future has been completed successfully, which is not expected");
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
            } catch (ExecutionException exception) {
                // The task has failed, so it is already stopped
            } catch (CancellationException exception) {
                // Do nothing, this result is expected
            }

            this.chunksScheduled.clear();
        }
    }

    /**
     * Handles an update of a view distance of {@linkplain JetPlayer a player}. Does nothing if the task sending
     * chunks to the player has not been scheduled.
     *
     * @param viewDistance the new view distance
     * @since 1.0
     */
    public void handleViewDistanceUpdate(byte viewDistance) {
        byte clampedViewDistance = this.createViewDistance(viewDistance);
        this.updateChunkView(chunkView -> new ChunkView(chunkView.centerChunk(), clampedViewDistance));
    }

    /**
     * Handles an update of {@linkplain Position a position} of {@linkplain JetPlayer a player}. Does nothing if
     * the task sending chunks to the player has not been scheduled.
     *
     * @param position the position
     * @since 1.0
     */
    public void handlePositionUpdate(@NonNull Position position) {
        Objects.requireNonNull(position, "position");
        ChunkPosition centerChunkPosition = ChunkPositionUtil.fromCoordinate(position);
        this.updateChunkView(chunkView -> new ChunkView(centerChunkPosition, chunkView.viewDistance()));
    }

    /**
     * Handles a response to a batch of {@linkplain JetChunk chunks} from the client.
     *
     * <p>It is safe to call this after a world has been changed, since there is no data that could break
     * and the unacknowledged batch count does not change.</p>
     *
     * @param chunksPerTick a number of chunks that should be sent per tick, which is desired by the client
     * @since 1.0
     */
    public void handleChunkBatchReceived(float chunksPerTick) {
        try (EmptyAcquisition ignored = this.lock.acquireWrite()) {
            this.unacknowledgedBatches -= 1;
            this.chunksPerTick = Math.clamp(chunksPerTick, MINIMUM_CHUNKS_PER_TICK, MAXIMUM_CHUNKS_PER_TICK);

            if (this.unacknowledgedBatches == 0f)
                this.chunksToSend = 1f;
            this.maximumUnacknowledgedBatches = 10f;
        }
    }

    @Override
    public void handleDisconnection() {
        this.close();
    }

    @Override
    public void close() {
        try {
            this.executorService.shutdown();
            if (!this.executorService.awaitTermination(SHUTDOWN_TIMEOUT, SHUTDOWN_TIMEOUT_UNIT)) {
                throw new RuntimeException(new TimeoutException(
                        "The chunk batch handler has not been terminated on time"
                ));
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }

    private void updateChunkView(@NonNull UnaryOperator<ChunkView> chunkViewUnaryOperator) {
        try (EmptyAcquisition ignored = this.lock.acquireWrite()) {
            ChunkView previousView = this.chunkView;
            this.chunkView = chunkViewUnaryOperator.apply(this.chunkView);
            this.scheduleChunks(previousView);
        }
    }

    private void scheduleChunks(@Nullable ChunkView previousView) {
        try (EmptyAcquisition ignored = this.lock.acquireWrite()) {
            boolean previousChunkViewPresent = previousView != null;
            if (previousChunkViewPresent && this.chunkView.equals(previousView)) return;

            ChunkPosition centerChunkPosition = this.chunkView.centerChunk();
            if (!previousChunkViewPresent || !centerChunkPosition.equals(previousView.centerChunk()))
                this.player.sendPacket(new ServerCenterChunkPlayPacket(centerChunkPosition));

            for (int chunkX = this.chunkView.minimumChunkX(); chunkX <= this.chunkView.maximumChunkX(); chunkX++) {
                for (int chunkZ = this.chunkView.minimumChunkZ(); chunkZ <= this.chunkView.maximumChunkZ(); chunkZ++) {
                    ChunkPosition chunkPosition = new ChunkPosition(chunkX, chunkZ);
                    if (previousChunkViewPresent && previousView.isInView(chunkPosition))
                        continue;
                    this.chunksScheduled.add(chunkPosition);
                }
            }

            if (!previousChunkViewPresent) return;
            for (int chunkX = previousView.minimumChunkX(); chunkX <= previousView.maximumChunkX(); chunkX++) {
                for (int chunkZ = previousView.minimumChunkZ(); chunkZ <= previousView.maximumChunkZ(); chunkZ++) {
                    ChunkPosition chunkPosition = new ChunkPosition(chunkX, chunkZ);
                    if (this.chunkView.isInView(chunkPosition) || this.chunksScheduled.remove(chunkPosition))
                        continue;
                    this.player.sendPacket(new ServerInvalidateChunkPlayPacket(chunkPosition));
                }
            }
        }
    }

    private void tick() {
        try (
                EntityWorldAcquisition<?> worldAcquisition = this.player.acquireWorldRead();
                WriteWorldMapAcquisitionImpl worldMapAcquisition = worldAcquisition.get().acquireWorldMapWrite();
                /* The lock acquired after the world-map, because write world-map acquisitions during a world-map
                   update consume chunk-views of all players that are connected to the server, and the chunk-views
                   are guarded by the same lock. It may also negatively affect chunk-view updating inside
                   the write world-map acquisition, because we acquire write world-map acquisition just after the
                   lock is being acquired. These cases lead to a deadlock. */
                EmptyAcquisition ignored = this.lock.acquireWrite();
        ) {
            if (this.unacknowledgedBatches >= this.maximumUnacknowledgedBatches) return;
            this.chunksToSend = Math.min(this.chunksToSend + this.chunksPerTick, Math.max(1f, this.chunksPerTick));

            if (this.chunksToSend >= 1f) {
                if (this.chunksScheduled.isEmpty()) return;

                ChunkPosition centerChunkPosition = this.chunkView.centerChunk();
                Comparator<ChunkPosition> comparator = Comparator.comparingInt(centerChunkPosition::distanceSquared);

                List<ChunkPosition> chunksToSendPositions = Ordering.from(comparator)
                        .leastOf(this.chunksScheduled, (int) Math.floor(chunksToSend));

                this.player.sendPacket(new ServerChunkBatchStartPlayPacket());

                JetRegistryManager registryManager = this.player.server().registryManager();
                for (ChunkPosition position : chunksToSendPositions) {
                    JetChunk chunk = worldMapAcquisition.getChunk(position);
                    this.player.sendPacket(ServerChunkAndLightDataPlayPacket.create(position, chunk, registryManager));
                    this.chunksScheduled.remove(position);
                }

                int batchSize = chunksToSendPositions.size();
                this.player.sendPacket(new ServerChunkBatchFinishedPlayPacket(batchSize));

                this.chunksToSend -= batchSize;
                this.unacknowledgedBatches++;
            }
        }
    }

    private byte createViewDistance(byte clientViewDistance) {
        byte maximumViewDistance = this.player.server().configuration().maximumViewDistance();
        return (byte) Math.clamp(clientViewDistance, MINIMUM_VIEW_DISTANCE, maximumViewDistance);
    }
}