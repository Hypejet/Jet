package net.hypejet.jet.server.world.acquisition.worldmap;

import net.hypejet.concurrency.map.MapAcquisition;
import java.util.Objects;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.util.coordinate.ChunkPositionUtil;
import net.hypejet.jet.server.util.coordinate.ChunkRelativePositionUtil;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.LightType;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.acquisition.worldmap.WorldMapAcquisition;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.coordinate.biome.BiomePosition;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an implementation of {@linkplain WorldMapAcquisition a world-map acquisition}.
 *
 * @since 1.0
 * @see WorldMapAcquisition
 */
public class WorldMapAcquisitionImpl implements WorldMapAcquisition {

    protected final MapAcquisition<ChunkPosition, JetChunk, ?> acquisition;
    protected final JetWorld world;

    /**
     * Constructs the {@linkplain WorldMapAcquisitionImpl world-map acquisition implementation}.
     *
     * @param world a world that the acquisition is constructed for
     * @param acquisition an acquisition of chunks of the world
     * @since 1.0
     */
    public WorldMapAcquisitionImpl(@NonNull JetWorld world,
                                   @NonNull MapAcquisition<ChunkPosition, JetChunk, ?> acquisition) {
        this.world = Objects.requireNonNull(world, "world");
        this.acquisition = Objects.requireNonNull(acquisition, "acquisition");
    }

    @Override
    public final @Nullable BlockState getOptionalBlockState(@NonNull BlockPosition position) {
        JetChunk chunk = this.chunkOrNull(ChunkPositionUtil.fromCoordinate(position));
        if (chunk == null)
            return null;
        return blockState(position, chunk);
    }

    @Override
    public final Holder.@Nullable Reference<Biome> getOptionalBiome(@NonNull BiomePosition position) {
        JetChunk chunk = this.chunkOrNull(ChunkPositionUtil.fromBiomePosition(position));
        if (chunk == null)
            return null;
        return biome(position, chunk);
    }

    @Override
    public @Nullable CompoundBinaryTag getOptionalBlockEntity(@NonNull BlockPosition position) {
        JetChunk chunk = this.chunkOrNull(ChunkPositionUtil.fromCoordinate(position));
        if (chunk == null)
            return null;
        return blockEntity(position, chunk);
    }

    @Override
    public final byte getOptionalSkyLightLevel(@NonNull BlockPosition position) {
        JetChunk chunk = this.chunkOrNull(ChunkPositionUtil.fromCoordinate(position));
        if (chunk == null)
            return Byte.MIN_VALUE;
        return lightValue(position, LightType.SKY, chunk);
    }

    @Override
    public final byte getOptionalBlockLightLevel(@NonNull BlockPosition position) {
        JetChunk chunk = this.chunkOrNull(ChunkPositionUtil.fromCoordinate(position));
        if (chunk == null)
            return Byte.MIN_VALUE;
        return lightValue(position, LightType.BLOCK, chunk);
    }

    @Override
    public final boolean isUnlocked() {
        return this.acquisition.isUnlocked();
    }

    @Override
    public final void close() {
        this.acquisition.close();
    }

    @Override
    public final void ensurePermittedAndLocked() {
        this.acquisition.ensurePermittedAndLocked();
    }

    @Override
    public final @NotNull AcquisitionType acquisitionType() {
        return this.acquisition.acquisitionType();
    }

    /**
     * Gets {@linkplain JetWorld a world} associated with this acquisition.
     *
     * @return the world
     * @since 1.0
     */
    public @NonNull JetWorld world() {
        return this.world;
    }

    private @Nullable JetChunk chunkOrNull(@NonNull ChunkPosition position) {
        return this.acquisition.map().get(position);
    }

    /**
     * Gets {@linkplain BlockState a block state} of a block at {@linkplain BlockPosition a block position} specified
     * in {@linkplain JetChunk a chunk} specified.
     *
     * @param position the block position
     * @param chunk the chunk
     * @return the map
     * @since 1.0
     */
    protected static @NonNull BlockState blockState(@NonNull BlockPosition position, @NonNull JetChunk chunk) {
        return chunk.blockState(ChunkRelativePositionUtil.from(position));
    }

    /**
     * Gets data of a block entity of a block at {@linkplain BlockPosition a block position}
     * specified in {@linkplain JetChunk a chunk} specified.
     *
     * @param position the block position
     * @param chunk the chunk
     * @return the block entity data, {@code null} if the block does not have a block entity
     * @since 1.0
     */
    protected static @Nullable CompoundBinaryTag blockEntity(@NonNull BlockPosition position,
                                                             @NonNull JetChunk chunk) {
        ChunkRelativeBlockPosition chunkRelativePosition = ChunkRelativePositionUtil.from(position);
        return chunk.blockEntities().get(chunkRelativePosition);
    }

    /**
     * Gets a {@linkplain Holder.Reference holder referencing to} a {@linkplain Biome biome} present
     * at a {@linkplain BiomePosition biome position} associated with the specified {@linkplain JetChunk chunk}.
     *
     * @param position the biome position
     * @param chunk the chunk
     * @return the registry entry
     * @since 1.0
     */
    protected static Holder.@NonNull Reference<Biome> biome(@NonNull BiomePosition position, @NonNull JetChunk chunk) {
        ChunkRelativeBiomePosition chunkRelativePosition = ChunkRelativePositionUtil.from(position);
        ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(chunkRelativePosition);
        JetChunkSection chunkSection = chunk.chunkSectionList().sectionFor(chunkRelativePosition);
        return chunkSection.biomePalette().getElement(palettePosition);
    }

    /**
     * Gets level of a light with {@linkplain LightType a light type} specified
     * at {@linkplain BlockPosition a block position} specified in {@linkplain JetChunk a chunk} specified.
     *
     * @param position the block position
     * @param lightType the light type
     * @param chunk the chunk
     * @return the registry entry
     * @since 1.0
     */
    protected static byte lightValue(@NonNull BlockPosition position, @NonNull LightType lightType,
                                     @NonNull JetChunk chunk) {
        ChunkRelativeBlockPosition chunkRelativePosition = ChunkRelativePositionUtil.from(position);
        ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(chunkRelativePosition);

        JetLightSection lightSection = chunk.lightSectionList().sectionFor(chunkRelativePosition);
        AbstractLightStorage lightStorage = switch (lightType) {
            case SKY -> lightSection.skyLightStorage();
            case BLOCK -> lightSection.blockLightStorage();
        };

        return lightStorage.getValue(palettePosition);
    }
}