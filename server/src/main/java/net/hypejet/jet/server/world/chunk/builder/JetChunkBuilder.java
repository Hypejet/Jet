package net.hypejet.jet.server.world.chunk.builder;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.factory.palette.JetChunkPaletteFactory;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.util.array.NibbleArray;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.builder.ChunkBuilder;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.hypejet.jet.world.dimension.DimensionType;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an implementation of {@linkplain ChunkBuilder a chunk builder}.
 *
 * @since 1.0
 * @see ChunkBuilder
 */
public final class JetChunkBuilder implements ChunkBuilder {

    private final Int2ObjectMap<ChunkSectionBuilder> chunkSectionBuilders = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<LightSectionBuilder> lightSectionBuilders = new Int2ObjectOpenHashMap<>();

    private final Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities = new HashMap<>();
    private final DimensionType dimensionType;

    private final JetChunkPaletteFactory<BlockState> blockStatePaletteFactory;
    private final JetChunkPaletteFactory<Holder.Reference<Biome>> biomePaletteFactory;

    private final JetBlockState defaultBlockState;
    private final Holder.Reference<Biome> defaultBiome;

    /**
     * Constructs the {@linkplain JetChunkBuilder chunk builder implementation}.
     *
     * @param dimensionType a dimension type of world that the chunk is created for
     * @param blockStatePaletteFactory the block-state palette factory that the builder
     *                                 should create block-state chunk palettes with
     * @param biomePaletteFactory the biome palette factory that the builder should create biome chunk palettes with
     * @param defaultBlockState a default block state that the block state list of chunk sections
     *                          should be initially filled with
     * @param defaultBiome a holder referencing to a default biome that the biome list
     *                     of chunk sections should be initially filled with
     * @param defaultBlockEntity data of a default block entity that the block entity map should be initially filled
     *                           with, {@code null} if the map should not be filled
     * @since 1.0
     */
    public JetChunkBuilder(@NonNull DimensionType dimensionType,
                           @NonNull JetChunkPaletteFactory<BlockState> blockStatePaletteFactory,
                           @NonNull JetChunkPaletteFactory<Holder.Reference<Biome>> biomePaletteFactory,
                           @NonNull JetBlockState defaultBlockState, Holder.@NonNull Reference<Biome> defaultBiome,
                           @Nullable CompoundBinaryTag defaultBlockEntity) {
        this.dimensionType = Objects.requireNonNull(dimensionType, "dimension type");

        this.blockStatePaletteFactory = Objects.requireNonNull(
                blockStatePaletteFactory,
                "block-state palette factory"
        );

        this.biomePaletteFactory = Objects.requireNonNull(biomePaletteFactory, "biome palette factory");
        this.defaultBlockState = Objects.requireNonNull(defaultBlockState, "default block state");
        this.defaultBiome = Objects.requireNonNull(defaultBiome, "default biome");

        if (defaultBlockEntity == null) return;
        byte horizontalAxisLength = ChunkPaletteType.BLOCK_STATE.axisLength();

        for (byte x = 0; x < horizontalAxisLength; x++) {
            for (int y = dimensionType.minY(); y < dimensionType.height(); y++) {
                for (byte z = 0; z < horizontalAxisLength; z++) {
                    this.blockEntities.put(new ChunkRelativeBlockPosition(x, y, z), defaultBlockEntity);
                }
            }
        }
    }

    @Override
    public @NonNull JetChunkBuilder setBlockState(@NonNull ChunkRelativeBlockPosition position,
                                                  @NonNull BlockState blockState) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(blockState, "block state");

        if (!(blockState instanceof JetBlockState validatedBlockState))
            throw new IllegalArgumentException("The block state specified is not a valid block state");

        int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);
        int sectionIndex = ChunkSectionList.createSectionIndex(sectionY, this.dimensionType);

        ChunkPaletteRelativePosition paletteRelativePosition = ChunkPaletteRelativePosition.from(position);
        ChunkSectionBuilder chunkSectionBuilder = this.chunkSectionBuilder(sectionIndex);

        BlockState previousBlockState = chunkSectionBuilder.getBlockState(paletteRelativePosition);
        if (previousBlockState.blockType() != validatedBlockState.blockType())
            this.blockEntities.remove(position);

        chunkSectionBuilder.setBlockState(paletteRelativePosition, validatedBlockState);
        return this;
    }

    @Override
    public @NonNull ChunkBuilder setBlockEntity(@NonNull ChunkRelativeBlockPosition position,
                                                @NonNull CompoundBinaryTag blockEntityData) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(blockEntityData, "block entity data");
        this.blockEntities.put(position, blockEntityData);
        return this;
    }

    @Override
    public @NonNull JetChunkBuilder setBiome(@NonNull ChunkRelativeBiomePosition position,
                                             Holder.@NonNull Reference<Biome> biome) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(biome, "biome");

        int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BIOME);
        int sectionIndex = ChunkSectionList.createSectionIndex(sectionY, this.dimensionType);

        ChunkPaletteRelativePosition paletteRelativePosition = ChunkPaletteRelativePosition.from(position);
        this.chunkSectionBuilder(sectionIndex).setBiome(paletteRelativePosition, biome);

        return this;
    }

    @Override
    public @NonNull JetChunkBuilder setSkyLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level) {
        Objects.requireNonNull(position, "position");
        ChunkPaletteRelativePosition paletteRelativePosition = ChunkPaletteRelativePosition.from(position);
        this.lightSectionBuilder(position).setSkyLight(paletteRelativePosition, level);
        return this;
    }

    @Override
    public @NonNull JetChunkBuilder setBlockLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level) {
        Objects.requireNonNull(position, "position");
        ChunkPaletteRelativePosition paletteRelativePosition = ChunkPaletteRelativePosition.from(position);
        this.lightSectionBuilder(position).setBlockLight(paletteRelativePosition, level);
        return this;
    }

    @Override
    public @NonNull JetChunk build() {
        int chunkSectionCount = ChunkSectionList.createSectionCount(this.dimensionType);
        List<JetChunkSection> chunkSections = new ArrayList<>(chunkSectionCount);

        JetChunkSection emptyChunkSection = null;
        for (int sectionIndex = 0; sectionIndex < chunkSectionCount; sectionIndex++) {
            JetChunkSection chunkSection;

            if (this.chunkSectionBuilders.containsKey(sectionIndex)) {
                chunkSection = this.chunkSectionBuilders.get(sectionIndex).build();
            } else {
                if (emptyChunkSection == null)
                    emptyChunkSection = this.createChunkSectionBuilder().build();
                chunkSection = emptyChunkSection;
            }

            chunkSections.add(sectionIndex, chunkSection);
        }

        int lightSectionCount = LightSectionList.createSectionCount(this.dimensionType);
        List<JetLightSection> lightSections = new ArrayList<>();

        JetLightSection emptyLightSection = null;
        for (int sectionIndex = 0; sectionIndex < lightSectionCount; sectionIndex++) {
            JetLightSection lightSection;

            if (this.lightSectionBuilders.containsKey(sectionIndex)) {
                lightSection = this.lightSectionBuilders.get(sectionIndex).build();
            } else {
                if (emptyLightSection == null)
                    emptyLightSection = new LightSectionBuilder().build();
                lightSection = emptyLightSection;
            }

            lightSections.add(sectionIndex, lightSection);
        }

        return JetChunk.create(this.dimensionType, chunkSections, lightSections, this.blockEntities);
    }

    /**
     * Gets {@linkplain ChunkSectionBuilder a chunk section builder} of {@linkplain JetChunkSection a chunk section}
     * that the chunk should have at an index specified.
     *
     * @param sectionIndex the index
     * @return the chunk section builder
     * @since 1.0
     */
    private @NonNull ChunkSectionBuilder chunkSectionBuilder(int sectionIndex) {
        return this.chunkSectionBuilders.computeIfAbsent(sectionIndex, ignored -> this.createChunkSectionBuilder());
    }

    /**
     * Gets {@linkplain LightSectionBuilder a light section builder} of {@linkplain JetLightSection a light section}
     * that the {@linkplain ChunkRelativeBlockPosition chunk-relative block position} belongs to.
     *
     * @param position the chunk-relative block position
     * @return the light section builder
     * @since 1.0
     */
    private @NonNull LightSectionBuilder lightSectionBuilder(@NonNull ChunkRelativeBlockPosition position) {
        int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);
        int sectionIndex = ChunkSectionList.createSectionIndex(sectionY, this.dimensionType);
        return this.lightSectionBuilders.computeIfAbsent(sectionIndex, ignored -> new LightSectionBuilder());
    }

    /**
     * Creates a new {@linkplain ChunkSectionBuilder chunk section builder} with a default
     * {@linkplain BlockState block state} and {@linkplain Biome biome} specified in this builder.
     *
     * <p>The server specified in this builder is going to own the chunk section.</p>
     *
     * @return the chunk section builder
     * @since 1.0
     */
    private @NonNull ChunkSectionBuilder createChunkSectionBuilder() {
        return new ChunkSectionBuilder(
                this.defaultBlockState, this.defaultBiome,
                this.blockStatePaletteFactory, this.biomePaletteFactory
        );
    }

    /**
     * Represents a builder of {@linkplain JetChunkSection a chunk section}.
     *
     * @since 1.0
     * @see JetChunkSection
     */
    private static final class ChunkSectionBuilder {

        private final List<BlockState> blockStates;
        private final List<Holder.Reference<Biome>> biomes;

        private final JetChunkPaletteFactory<BlockState> blockStatePaletteFactory;
        private final JetChunkPaletteFactory<Holder.Reference<Biome>> biomePaletteFactory;

        /**
         * Constructs the {@linkplain ChunkSectionBuilder chunk section builder}.
         *
         * @param defaultBlockState a default block state that the block state list should be initially filled with
         * @param defaultBiome a holder referencing to a default biome that
         *                     the biome list should be initially filled with
         * @param blockStatePaletteFactory the block-state palette factory that the builder
         *                                 should create block-state chunk palettes with
         * @param biomePaletteFactory the biome palette factory that the builder
         *                            should create biome chunk palettes with
         * @since 1.0
         */
        private ChunkSectionBuilder(@NonNull JetBlockState defaultBlockState,
                                    Holder.@NonNull Reference<Biome> defaultBiome,
                                    @NonNull JetChunkPaletteFactory<BlockState> blockStatePaletteFactory,
                                    @NonNull JetChunkPaletteFactory<Holder.Reference<Biome>> biomePaletteFactory) {
            int blockStateCount = ChunkPaletteType.BLOCK_STATE.elementCount();
            this.blockStates = new ArrayList<>(blockStateCount);

            for (int index = 0; index < blockStateCount; index++)
                this.blockStates.add(index, defaultBlockState);

            int biomeCount = ChunkPaletteType.BIOME.elementCount();
            this.biomes = new ArrayList<>(biomeCount);

            for (int index = 0; index < biomeCount; index++)
                this.biomes.add(index, defaultBiome);

            this.blockStatePaletteFactory = blockStatePaletteFactory;
            this.biomePaletteFactory = biomePaletteFactory;
        }

        /**
         * Sets block at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified
         * to have {@linkplain BlockState a block state} specified.
         *
         * <p>Note that a block entity associated with the block will be removed if block types of previous block
         * state and the block state specified are different.</p>
         *
         * @param position the chunk-palette-relative position
         * @param blockState the block state
         * @since 1.0
         */
        private void setBlockState(@NonNull ChunkPaletteRelativePosition position, @NonNull JetBlockState blockState) {
            if (position.paletteType() != ChunkPaletteType.BLOCK_STATE) {
                throw new IllegalArgumentException(
                        "The position specified has not been created for a block state chunk palette"
                );
            }
            this.blockStates.set(AbstractChunkPalette.calculateElementIndex(position), blockState);
        }

        /**
         * Sets the specified {@linkplain Biome biome} to be present
         * at a {@linkplain ChunkPaletteRelativePosition chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @param biome a holder referencing to the biome that should be present at the specified position
         * @since 1.0
         */
        private void setBiome(@NonNull ChunkPaletteRelativePosition position, Holder.@NonNull Reference<Biome> biome) {
            if (position.paletteType() != ChunkPaletteType.BLOCK_STATE) {
                throw new IllegalArgumentException(
                        "The position specified has not been created for a biome chunk palette"
                );
            }
            this.biomes.set(AbstractChunkPalette.calculateElementIndex(position), biome);
        }

        /**
         * Gets {@linkplain BlockState a block state} that has been set so far
         * to be at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @return the block state
         * @since 1.0
         */
        private @NonNull BlockState getBlockState(@NonNull ChunkPaletteRelativePosition position) {
            if (position.paletteType() != ChunkPaletteType.BLOCK_STATE) {
                throw new IllegalArgumentException(
                        "The position specified has not been created for a block state chunk palette"
                );
            }
            return this.blockStates.get(AbstractChunkPalette.calculateElementIndex(position));
        }

        /**
         * Builds the {@linkplain JetChunkSection chunk section}.
         *
         * @return the chunk section
         * @since 1.0
         */
        private @NonNull JetChunkSection build() {
            return new JetChunkSection(
                    this.blockStatePaletteFactory.createDirect(this.blockStates),
                    this.biomePaletteFactory.createDirect(this.biomes)
            );
        }
    }

    /**
     * Represents a builder of {@linkplain JetLightSection a light section}.
     *
     * @since 1.0
     * @see JetLightSection
     */
    private static final class LightSectionBuilder {

        private final NibbleArray.Builder skyLightArrayBuilder;
        private final NibbleArray.Builder blockLightArrayBuilder;

        /**
         * Constructs the {@linkplain LightSectionBuilder light section builder}.
         *
         * @since 1.0
         */
        private LightSectionBuilder() {
            short length = ChunkPaletteType.BLOCK_STATE.elementCount();
            this.skyLightArrayBuilder = new NibbleArray.Builder(length);
            this.blockLightArrayBuilder = new NibbleArray.Builder(length);
        }

        /**
         * Sets a skylight level specified to be set
         * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @param level the skylight level
         * @since 1.0
         */
        private void setSkyLight(@NonNull ChunkPaletteRelativePosition position, byte level) {
            this.skyLightArrayBuilder.set(AbstractChunkPalette.calculateElementIndex(position), level);
        }

        /**
         * Sets a block light level specified to be set
         * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @param level the block light level
         * @since 1.0
         */
        private void setBlockLight(@NonNull ChunkPaletteRelativePosition position, byte level) {
            this.blockLightArrayBuilder.set(AbstractChunkPalette.calculateElementIndex(position), level);
        }

        /**
         * Builds the {@linkplain JetLightSection light section}.
         *
         * @return the light section
         * @since 1.0
         */
        private @NonNull JetLightSection build() {
            return new JetLightSection(
                    AbstractLightStorage.create(this.skyLightArrayBuilder.build()),
                    AbstractLightStorage.create(this.blockLightArrayBuilder.build())
            );
        }
    }
}