package net.hypejet.jet.server.world.handler;

import com.google.common.collect.Ordering;
import net.hypejet.concurrency.collection.CollectionAcquirable;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.collection.set.HashSetAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.object.nullable.WriteNullableObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.floats.FloatAcquirable;
import net.hypejet.concurrency.primitive.floats.FloatAcquisition;
import net.hypejet.concurrency.primitive.floats.WriteFloatAcquisition;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchFinishedPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchStartPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerInvalidateChunkPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.server.util.acquisition.BooleanMappedAcquisition;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.chunk.Chunk;
import net.hypejet.jet.server.world.chunk.view.ChunkView;
import net.hypejet.jet.server.world.coordinate.ChunkPosition;
import net.hypejet.jet.world.event.events.StartWaitingForWorldChunksWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.UnaryOperator;

/**
 * Represents something that handles sending batches of {@linkplain Chunk chunks} to a client.
 *
 * @since 1.0
 * @see Chunk
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

    private final NullableObjectAcquirable<JetWorld> world = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<ChunkView> chunkView = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<ScheduledFuture<?>> future = new NullableObjectAcquirable<>();

    private final CollectionAcquirable<?, Set<ChunkPosition>> chunksScheduled = new HashSetAcquirable<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final FloatAcquirable chunksPerTick = new FloatAcquirable(9f);
    private final FloatAcquirable maximumUnacknowledgedBatches = new FloatAcquirable(1f);

    private final FloatAcquirable chunksToSend = new FloatAcquirable(0f);
    private final FloatAcquirable unacknowledgedBatches = new FloatAcquirable(0f);

    /**
     * Constructs the {@linkplain ChunkBatchHandler chunk batch handler}.
     *
     * @param player a player that the chunks should be sent to
     * @since 1.0
     */
    public ChunkBatchHandler(@NonNull JetPlayer player) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
    }

    /**
     * Creates {@linkplain BooleanAcquisition a boolean acquisition}, whose value defines whether a task sending
     * chunks to the player is running.
     *
     * @return the boolean acquisition
     * @since 1.0
     */
    public @NonNull BooleanAcquisition isTaskRunning() {
        return new BooleanMappedAcquisition<>(this.future.acquireRead(), acquisition -> acquisition.get() != null);
    }

    /**
     * Schedules a task, which sends chunks to the player.
     *
     * @param world a world, from which the chunks should be retrieved
     * @param startingPosition a position where is the player at moment of scheduling the task
     * @throws IllegalStateException if the task has been already scheduled or settings of the play have not been set
     * @since 1.0
     */
    public void scheduleTask(@NonNull JetWorld world, @NonNull Position startingPosition) {
        NullabilityUtil.requireNonNull(world, "world");
        NullabilityUtil.requireNonNull(startingPosition, "starting position");

        try (WriteNullableObjectAcquisition<ScheduledFuture<?>> futureAcquisition = this.future.acquireWrite()) {
            if (futureAcquisition.get() != null)
                throw new IllegalStateException("The task has been already scheduled");

            try (
                    WriteNullableObjectAcquisition<JetWorld> worldAcquisition = this.world.acquireWrite();
                    NullableObjectAcquisition<Player.Settings> settingsAcquisition = this.player.settings()
            ) {
                Player.Settings settings = settingsAcquisition.get();
                if (settings == null)
                    throw new IllegalStateException("Settings of the player have not been set");

                worldAcquisition.set(world);

                ChunkPosition centerChunkPosition = ChunkPosition.fromCoordinate(startingPosition);
                byte viewDistance = this.createViewDistance(settings.viewDistance());

                this.player.sendPacket(new ServerWorldEventPlayPacket(StartWaitingForWorldChunksWorldEvent.INSTANCE));
                this.updateChunkView(true, view -> new ChunkView(centerChunkPosition, viewDistance));

                futureAcquisition.set(this.executorService.scheduleAtFixedRate(
                        this::tick,
                        0L, 50L,
                        TimeUnit.MILLISECONDS
                )); // TODO: Replace with a tick system when it gets implemented
            }
        }
    }

    /**
     * Stops a task, which sends chunks to the player, then awaits for when the task is fully stopped. Finally, all
     * chunks are removed from {@linkplain java.util.Collection collection} of scheduled chunks.
     *
     * @throws IllegalStateException if the task has not been scheduled or a future handling the task
     *                               has been completed successfully
     * @since 1.0
     */
    public void cancelTask() {
        try (WriteNullableObjectAcquisition<ScheduledFuture<?>> futureAcquisition = this.future.acquireWrite()) {
            if (futureAcquisition.get() == null)
                throw new IllegalStateException("The task has not been scheduled");

            ScheduledFuture<?> future = futureAcquisition.get();
            future.cancel(false);

            try {
                future.get();
                throw new IllegalStateException("The future has been completed successfully, which is not expected");
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
            } catch (ExecutionException exception) {
                // The task has failed, so it is already stopped
            } catch (CancellationException exception) {
                // Do nothing, this result is expected
            }

            try (
                    WriteNullableObjectAcquisition<JetWorld> worldAcquisition = this.world.acquireWrite();
                    WriteNullableObjectAcquisition<ChunkView> chunkViewAcquisition = this.chunkView.acquireWrite();
                    CollectionAcquisition<?, ?> chunksAcquisition = this.chunksScheduled.acquireWrite()
            ) {
                worldAcquisition.set(null);
                chunkViewAcquisition.set(null);
                chunksAcquisition.collection().clear();
            }
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
        this.updateChunkView(false, chunkView -> new ChunkView(chunkView.centerChunk(), clampedViewDistance));
    }

    /**
     * Handles an update of {@linkplain Position a position} of {@linkplain JetPlayer a player}. Does nothing if
     * the task sending chunks to the player has not been scheduled.
     *
     * @param position the position
     * @since 1.0
     */
    public void handlePositionUpdate(@NonNull Position position) {
        NullabilityUtil.requireNonNull(position, "position");
        ChunkPosition centerChunkPosition = ChunkPosition.fromCoordinate(position);
        this.updateChunkView(false, chunkView -> new ChunkView(centerChunkPosition, chunkView.viewDistance()));
    }

    /**
     * Handles a response to a batch of {@linkplain Chunk chunks} from the client.
     *
     * <p>It is safe to call this after a world has been changed, since there is no data that could break
     * and the unacknowledged batch count does not change.</p>
     *
     * @param chunksPerTick a number of chunks that should be sent per tick, which is desired by the client
     * @since 1.0
     */
    public void handleChunkBatchReceived(float chunksPerTick) {
        try (
                WriteFloatAcquisition unacknowledgedBatchesAcquisition = this.unacknowledgedBatches.acquireWrite();
                WriteFloatAcquisition chunksPerTickAcquisition = this.chunksPerTick.acquireWrite();
                WriteFloatAcquisition chunksToSendAcquisition = this.chunksToSend.acquireWrite();
                WriteFloatAcquisition maxUnacknowledgedAcquisition = this.maximumUnacknowledgedBatches.acquireWrite()
        ) {
            unacknowledgedBatchesAcquisition.set(unacknowledgedBatchesAcquisition.get() - 1);
            chunksPerTickAcquisition.set(Math.clamp(chunksPerTick, MINIMUM_CHUNKS_PER_TICK, MAXIMUM_CHUNKS_PER_TICK));

            if (unacknowledgedBatchesAcquisition.get() == 0f)
                chunksToSendAcquisition.set(1f);

            maxUnacknowledgedAcquisition.set(10f);
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

    private void updateChunkView(boolean allowNotRunning, @NonNull UnaryOperator<ChunkView> chunkViewUnaryOperator) {
        try (
                WriteNullableObjectAcquisition<ChunkView> chunkViewAcquisition = this.chunkView.acquireWrite();
                CollectionAcquisition<?, Set<ChunkPosition>> chunksAcquisition = this.chunksScheduled.acquireWrite()
        ) {
            ChunkView previousChunkView = chunkViewAcquisition.get();
            if (!allowNotRunning && previousChunkView == null) return; // The task is not running

            ChunkView chunkView = chunkViewUnaryOperator.apply(previousChunkView);
            chunkViewAcquisition.set(chunkView);

            ChunkPosition centerChunkPosition = chunkView.centerChunk();

            boolean previousChunkViewPresent = previousChunkView != null;
            if (!previousChunkViewPresent || !centerChunkPosition.equals(previousChunkView.centerChunk()))
                this.player.sendPacket(new ServerCenterChunkPlayPacket(centerChunkPosition));

            int minimumChunkX = chunkView.minimumChunkX();
            int maximumChunkX = chunkView.maximumChunkX();

            int minimumChunkZ = chunkView.minimumChunkZ();
            int maximumChunkZ = chunkView.maximumChunkZ();

            if (previousChunkViewPresent) {
                minimumChunkX = Math.min(minimumChunkX, previousChunkView.minimumChunkX());
                maximumChunkX = Math.max(maximumChunkX, previousChunkView.maximumChunkX());

                minimumChunkZ = Math.min(minimumChunkZ, previousChunkView.minimumChunkZ());
                maximumChunkZ = Math.max(maximumChunkZ, previousChunkView.maximumChunkZ());
            }

            Set<ChunkPosition> chunksScheduled = chunksAcquisition.collection();
            for (int chunkX = minimumChunkX; chunkX < maximumChunkX; chunkX++) {
                for (int chunkZ = minimumChunkZ; chunkZ < maximumChunkZ; chunkZ++) {
                    ChunkPosition chunkPosition = new ChunkPosition(chunkX, chunkZ);

                    if (previousChunkViewPresent && previousChunkView.isInView(chunkPosition)) {
                        if (!chunkView.isInView(chunkPosition) && !chunksScheduled.remove(chunkPosition))
                            this.player.sendPacket(new ServerInvalidateChunkPlayPacket(chunkPosition));
                        continue;
                    }

                    chunksScheduled.add(chunkPosition);
                }
            }
        }
    }

    private void tick() {
        try (
                FloatAcquisition unacknowledgedBatchesAcquisition = this.unacknowledgedBatches.acquireRead();
                FloatAcquisition chunkPerTickAcquisition = this.chunksPerTick.acquireRead();
                FloatAcquisition maxUnacknowledgedBatchesAcquisition = this.maximumUnacknowledgedBatches.acquireRead();
                WriteFloatAcquisition chunksToSendAcquisition = this.chunksToSend.acquireWrite()
        ) {
            if (unacknowledgedBatchesAcquisition.get() >= maxUnacknowledgedBatchesAcquisition.get())
                return;

            float chunksPerTick = chunkPerTickAcquisition.get();
            chunksToSendAcquisition.set(Math.min(
                    chunksToSendAcquisition.get() + chunksPerTick,
                    Math.max(1f, chunksPerTick)
            ));

            if (chunksToSendAcquisition.get() >= 1f)
                this.sendChunks();
        }
    }

    private void sendChunks() {
        try (
                NullableObjectAcquisition<ChunkView> chunkViewAcquisition = this.chunkView.acquireRead();
                NullableObjectAcquisition<JetWorld> worldAcquisition = this.world.acquireRead();
                CollectionAcquisition<?, Set<ChunkPosition>> chunksAcquisition = this.chunksScheduled.acquireWrite();
                WriteFloatAcquisition chunksToSendAcquisition = this.chunksToSend.acquireWrite()
        ) {
            Set<ChunkPosition> chunkPositions = chunksAcquisition.collection();
            if (chunkPositions.isEmpty()) return;

            ChunkView chunkView = chunkViewAcquisition.get();
            if (chunkView == null)
                throw new IllegalStateException("The chunk view has not been set");

            JetWorld world = worldAcquisition.get();
            if (world == null)
                throw new IllegalStateException("The world has not been set");

            ChunkPosition centerChunkPosition = chunkView.centerChunk();
            Comparator<ChunkPosition> comparator = Comparator.comparingInt(centerChunkPosition::distanceSquared);

            int chunkCount = (int) Math.floor(chunksToSendAcquisition.get());
            List<ChunkPosition> chunksToSendPositions = Ordering.from(comparator).leastOf(chunkPositions, chunkCount);

            this.player.sendPacket(new ServerChunkBatchStartPlayPacket());

            for (ChunkPosition chunkPosition : chunksToSendPositions) {
                try (NotNullObjectAcquisition<Chunk> chunkAcquisition = world.loadChunk(chunkPosition)) {
                    Chunk chunk = chunkAcquisition.get();
                    this.player.sendPacket(new ServerChunkAndLightDataPlayPacket(chunkPosition, chunk));
                }
                chunkPositions.remove(chunkPosition);
            }

            int batchSize = chunksToSendPositions.size();
            this.player.sendPacket(new ServerChunkBatchFinishedPlayPacket(batchSize));
            chunksToSendAcquisition.set(chunksToSendAcquisition.get() - batchSize);
        }
    }

    private byte createViewDistance(byte clientViewDistance) {
        byte maximumViewDistance = this.player.server().configuration().maximumViewDistance();
        return (byte) Math.clamp(clientViewDistance, MINIMUM_VIEW_DISTANCE, maximumViewDistance);
    }
}