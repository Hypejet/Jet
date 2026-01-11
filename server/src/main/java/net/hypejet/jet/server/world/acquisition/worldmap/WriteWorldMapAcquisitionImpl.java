package net.hypejet.jet.server.world.acquisition.worldmap;

import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.util.coordinate.ChunkPositionUtil;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.LightType;
import net.hypejet.jet.server.world.update.JetWorldMapUpdate;
import net.hypejet.jet.world.acquisition.worldmap.WriteWorldMapAcquisition;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.state.BlockState;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.biome.BiomePosition;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.update.WorldMapUpdate;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an implementation of {@linkplain WriteWorldMapAcquisition a write world-map acquisition}.
 *
 * @since 1.0
 * @see WriteWorldMapAcquisition
 */
public final class WriteWorldMapAcquisitionImpl extends WorldMapAcquisitionImpl implements WriteWorldMapAcquisition {
    /**
     * Constructs the {@linkplain WriteWorldMapAcquisitionImpl write world-map acquisition implementation}.
     *
     * @param world a world that the acquisition is constructed for
     * @param acquisition an acquisition of chunks of the world
     * @since 1.0
     */
    public WriteWorldMapAcquisitionImpl(@NonNull JetWorld world,
                                        @NonNull MapAcquisition<ChunkPosition, JetChunk, ?> acquisition) {
        super(world, acquisition);
    }

    @Override
    public @NonNull BlockState getBlockState(@NonNull BlockPosition position) {
        JetChunk chunk = this.getChunk(ChunkPositionUtil.fromCoordinate(position));
        return blockState(position, chunk);
    }

    @Override
    public Holder.@NonNull Reference<Biome> getBiome(@NonNull BiomePosition position) {
        JetChunk chunk = this.getChunk(ChunkPositionUtil.fromBiomePosition(position));
        return biome(position, chunk);
    }

    @Override
    public @Nullable CompoundBinaryTag getBlockEntity(@NonNull BlockPosition position) {
        JetChunk chunk = this.getChunk(ChunkPositionUtil.fromCoordinate(position));
        return blockEntity(position, chunk);
    }

    @Override
    public byte getSkyLightValue(@NonNull BlockPosition position) {
        JetChunk chunk = this.getChunk(ChunkPositionUtil.fromCoordinate(position));
        return lightValue(position, LightType.SKY, chunk);
    }

    @Override
    public byte getBlockLightValue(@NonNull BlockPosition position) {
        JetChunk chunk = this.getChunk(ChunkPositionUtil.fromCoordinate(position));
        return lightValue(position, LightType.BLOCK, chunk);
    }

    @Override
    public @NonNull WorldMapUpdate createUpdate() {
        return new JetWorldMapUpdate(this);
    }

    /**
     * Gets {@linkplain JetChunk a chunk} at {@linkplain ChunkPosition a chunk position} specified.
     *
     * <p>If the chunk has not been loaded, it is loaded using
     * {@linkplain net.hypejet.jet.world.chunk.ChunkLoader a chunk loader} of {@linkplain JetWorld a world}
     * associated with this acquisition.</p>
     *
     * @param position the chunk position
     * @return the chunk
     * @since 1.0
     */
    public @NonNull JetChunk getChunk(@NonNull ChunkPosition position) {
        return this.acquisition.map().computeIfAbsent(position, ignored -> {
            if (!(this.world.chunkLoader().load(position, this.world) instanceof JetChunk chunk))
                throw new IllegalArgumentException("The chunk created is not a valid chunk");
            return chunk;
        });
    }

    /**
     * Puts {@linkplain JetChunk a chunk} specified at {@linkplain ChunkPosition a chunk position} specified.
     *
     * @param position the chunk position
     * @param chunk the chunk
     * @since 1.0
     */
    public void setChunk(@NonNull ChunkPosition position, @NonNull JetChunk chunk) {
        this.acquisition.map().put(position, chunk);
    }
}