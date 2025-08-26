package net.hypejet.jet.server.world.handler;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchFinishedPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchStartPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerInvalidateChunkPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.coordinate.ChunkPositionUtil;
import net.hypejet.jet.server.world.acquisition.worldmap.WriteWorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.view.ChunkView;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Something sending {@linkplain JetChunk chunks} in batches to the client.
 *
 * @since 1.0
 * @see JetChunk
 */
@NullMarked
public final class ChunkBatchHandler {

    private static final float MINIMUM_CHUNKS_PER_TICK = 0.01f;
    private static final float MAXIMUM_CHUNKS_PER_TICK = 64f;

    /**
     * A minimum view distance that {@linkplain JetPlayer a player} can have.
     *
     * @since 1.0
     */
    public static final byte MINIMUM_VIEW_DISTANCE = 2;

    private final JetPlayer player;
    private final Set<ChunkPosition> chunksScheduled = new HashSet<>();

    private @Nullable ChunkView chunkView;

    private float chunksPerTick = 9f;
    private float maximumUnacknowledgedBatches = 1f;

    private float chunksToSend;
    private float unacknowledgedBatches;

    /**
     * Constructs the {@linkplain ChunkBatchHandler chunk batch handler}.
     *
     * @param player a player that the chunks should be sent to
     * @param position an initial position of the player
     * @since 1.0
     */
    public ChunkBatchHandler(JetPlayer player, Position position) {
        this.player = Objects.requireNonNull(player, "player");
        Objects.requireNonNull(position, "position");
    }

    /**
     * Gets a {@linkplain ChunkView chunk view} currently used
     * by this {@linkplain ChunkBatchHandler chunk batch handler} for sending the {@linkplain JetChunk chunks}.
     *
     * @return the chunk view used by this chunk batch handler, {@code null} if it has not been initialized yet
     * @since 1.0
     */
    public @Nullable ChunkView chunkView() {
        return this.chunkView;
    }

    /**
     * Gets whether a {@linkplain JetChunk chunk}
     * at the specified {@linkplain ChunkPosition chunk position}
     * has been scheduled to be sent to the client.
     *
     * @param chunkPosition the position of the chunk to check whether it has been scheduled
     * @return {@code true} if the chunk position has been scheduled to be sent by this chunk batch handler,
     *         {@code false} if it is not within the used chunk view, or it has already been sent
     * @since 1.0
     */
    public boolean isPending(ChunkPosition chunkPosition) {
        return this.chunksScheduled.contains(chunkPosition);
    }

    /**
     * Re-initializes the {@linkplain ChunkView chunk view} used
     * by this {@linkplain ChunkBatchHandler chunk batch handler} and re-sends all chunks
     * within that {@linkplain ChunkView chunk view} without invalidating already sent chunks.
     *
     * @since 1.0
     */
    public void resetChunkView() {
        this.chunkView = null;
        this.updateChunkView();
    }

    /**
     * Updates {@linkplain ChunkView chunk view} used by this {@linkplain ChunkBatchHandler chunk batch handler}
     * with latest data of the {@linkplain JetPlayer player} associated with this handler.
     *
     * @since 1.0
     */
    public void updateChunkView() {
        ChunkPosition centerChunk = ChunkPositionUtil.fromCoordinate(this.player.position());
        byte viewDistance = this.viewDistance();

        if (
                this.chunkView != null
                        && this.chunkView.centerChunk().equals(centerChunk)
                        && this.chunkView.viewDistance() == viewDistance
        ) {
            return;
        }

        ChunkView newView = new ChunkView(centerChunk, viewDistance);
        if (!(this.chunkView != null && this.chunkView.centerChunk().equals(centerChunk))) {
            this.player.sendPacket(new ServerCenterChunkPlayPacket(centerChunk));
        }

        newView.forEach(chunkPosition -> {
            if (this.chunkView != null && this.chunkView.isInView(chunkPosition)) return;
            this.chunksScheduled.add(chunkPosition);
        });

        if (this.chunkView != null) {
            this.chunkView.forEach(chunkPosition -> {
                // TODO: Do not send invalidate chunk packets if the player is respawning or changing worlds
                if (newView.isInView(chunkPosition) || this.chunksScheduled.remove(chunkPosition)) return;
                this.player.sendPacket(new ServerInvalidateChunkPlayPacket(chunkPosition));
            });
        }

        this.chunkView = newView;
    }

    /**
     * Runs a cycle of this {@linkplain ChunkBatchHandler chunk batch handler}.
     *
     * <p>{@linkplain ChunkBatchHandler Chunk batch handler} cycles collect {@linkplain JetChunk chunks}
     * placed closest to the {@linkplain JetPlayer player} associated with the same chunk batch handler
     * and sends them to that player.</p>
     *
     * @since 1.0
     */
    public void tick() {
        try (WriteWorldMapAcquisitionImpl worldMapAcquisition = this.player.world().acquireWorldMapWrite()) {
            if (this.unacknowledgedBatches >= this.maximumUnacknowledgedBatches) return;
            this.chunksToSend = Math.min(this.chunksToSend + this.chunksPerTick, Math.max(1f, this.chunksPerTick));

            if (this.chunksToSend < 1f) return;
            if (this.chunksScheduled.isEmpty()) return;

            ChunkPosition centerChunk = ChunkPositionUtil.fromCoordinate(this.player.position());
            List<ChunkPosition> chunksToSend = new ArrayList<>(this.chunksScheduled);
            chunksToSend.sort(Comparator.comparingInt(centerChunk::distanceSquared));
            chunksToSend = chunksToSend.subList(0, Math.min(chunksToSend.size(), (int) Math.floor(this.chunksToSend)));

            if (chunksToSend.isEmpty()) return;
            this.player.sendPacket(new ServerChunkBatchStartPlayPacket());

            JetRegistryManager registryManager = this.player.server().registryManager();
            for (ChunkPosition position : chunksToSend) {
                JetChunk chunk = worldMapAcquisition.getChunk(position);
                this.player.sendPacket(ServerChunkAndLightDataPlayPacket.create(position, chunk, registryManager));
                this.chunksScheduled.remove(position);
            }

            int batchSize = chunksToSend.size();
            this.player.sendPacket(new ServerChunkBatchFinishedPlayPacket(batchSize));

            this.chunksToSend -= batchSize;
            this.unacknowledgedBatches++;
        }
    }

    /**
     * Handles a client response to a batch of {@linkplain JetChunk chunks}.
     *
     * <p>It is safe to call this after a world has been changed, since there is no data
     * that could break and the unacknowledged batch count does not change.</p>
     *
     * @param chunksPerTick a client-desired number of chunks that should be sent per tick
     * @since 1.0
     */
    public void handleChunkBatchReceived(float chunksPerTick) {
        this.player.server().ticker().scheduleTask(() -> {
            this.unacknowledgedBatches--;

            this.chunksPerTick = Float.isNaN(chunksPerTick)
                    ? MINIMUM_CHUNKS_PER_TICK
                    : Math.clamp(chunksPerTick, MINIMUM_CHUNKS_PER_TICK, MAXIMUM_CHUNKS_PER_TICK);

            if (this.unacknowledgedBatches == 0f)
                this.chunksToSend = 1f;
            this.maximumUnacknowledgedBatches = 10f;
        });
    }

    // TODO: Clamp when receiving settings instead
    private byte viewDistance() {
        byte maximumViewDistance = this.player.server().configuration().maximumViewDistance();
        return (byte) Math.clamp(this.player.settings().viewDistance(), MINIMUM_VIEW_DISTANCE, maximumViewDistance);
    }
}