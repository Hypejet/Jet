package net.hypejet.jet.server.world.chunk.builder;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.server.world.block.BlockType;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.chunk.builder.ChunkBuilder;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an implementation of {@linkplain ChunkBuilder a chunk builder}.
 *
 * @since 1.0
 * @see ChunkBuilder
 */
public final class JetChunkBuilder implements ChunkBuilder<BlockState> {

    private final Int2ObjectMap<ChunkSectionBuilder> chunkSectionBuilders = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<LightSectionBuilder> lightSectionBuilders = new Int2ObjectOpenHashMap<>();

    private final Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities = new HashMap<>();

    private final DimensionType dimensionType;
    private final JetMinecraftServer server;

    private final BlockState defaultBlockState;
    private final JetRegistryEntry<Biome> defaultBiome;

    /**
     * Constructs the {@linkplain JetChunkBuilder chunk builder implementation}.
     *
     * @param dimensionType a dimension type of world that the chunk is created for
     * @param server a server that should own the chunk
     * @param defaultBlockState a default block state that the block state list of chunk sections
     *                          should be initially filled with
     * @param defaultBiome a registry entry of a default biome that the biome list of chunk sections
     *                     should be initially filled with
     * @param defaultBlockEntity data of a default block entity that the block entity map should be initially filled
     *                           with, {@code null} if the map should not be filled
     * @since 1.0
     */
    public JetChunkBuilder(@NonNull DimensionType dimensionType, @NonNull JetMinecraftServer server,
                           @NonNull BlockState defaultBlockState, @NonNull JetRegistryEntry<Biome> defaultBiome,
                           @Nullable CompoundBinaryTag defaultBlockEntity) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.server = NullabilityUtil.requireNonNull(server, "server");

        this.defaultBlockState = NullabilityUtil.requireNonNull(defaultBlockState, "default block state");
        this.defaultBiome = NullabilityUtil.requireNonNull(defaultBiome, "default biome");

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
    public @NonNull JetChunkBuilder setBlock(@NonNull ChunkRelativeBlockPosition position, @NonNull Key blockTypeKey) {
        return this.setBlock(position, blockTypeKey, null);
    }

    @Override
    public @NonNull JetChunkBuilder setBlock(@NonNull ChunkRelativeBlockPosition position, @NonNull Key blockTypeKey,
                                             @Nullable Map<String, String> properties) {
        JetRegistryManager registryManager = this.server.registryManager();

        JetMinecraftRegistry<BlockType> blockTypeRegistry = registryManager.blockTypeRegistry();
        BlockStateRegistry blockStateRegistry = registryManager.blockStateRegistry();

        JetRegistryEntry<BlockType> blockTypeEntry = blockTypeRegistry.get(blockTypeKey);
        if (blockTypeEntry == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block type with ket of %s",
                    blockTypeKey
            ));
        }

        BlockType blockType = blockTypeEntry.value();
        BlockState blockState = properties == null ? blockType.defaultState() : blockType.state(properties);

        int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);
        int sectionIndex = ChunkSectionList.createSectionIndex(sectionY, this.dimensionType);

        ChunkPaletteRelativePosition paletteRelativePosition = ChunkPaletteRelativePosition.from(position);
        ChunkSectionBuilder chunkSectionBuilder = this.chunkSectionBuilder(sectionIndex);

        BlockState previousBlockState = chunkSectionBuilder.getBlockState(paletteRelativePosition);
        if (blockStateRegistry.blockType(previousBlockState) != blockTypeEntry)
            this.blockEntities.remove(position);

        chunkSectionBuilder.setBlockState(paletteRelativePosition, blockState);
        return this;
    }

    @Override
    public @NonNull ChunkBuilder<BlockState> setBlockEntity(@NonNull ChunkRelativeBlockPosition position,
                                                            @NonNull CompoundBinaryTag blockEntityData) {
        this.blockEntities.put(position, blockEntityData);
        return this;
    }

    @Override
    public @NonNull JetChunkBuilder setBiome(@NonNull ChunkRelativeBiomePosition position,
                                             @NonNull RegistryEntry<Biome> biome) {
        int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BIOME);
        int sectionIndex = ChunkSectionList.createSectionIndex(sectionY, this.dimensionType);

        ChunkPaletteRelativePosition paletteRelativePosition = ChunkPaletteRelativePosition.from(position);
        this.chunkSectionBuilder(sectionIndex).setBiome(paletteRelativePosition, biome);

        return this;
    }

    @Override
    public @NonNull JetChunkBuilder setSkyLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level) {
        ChunkPaletteRelativePosition paletteRelativePosition = ChunkPaletteRelativePosition.from(position);
        this.lightSectionBuilder(position).setSkyLight(paletteRelativePosition, level);
        return this;
    }

    @Override
    public @NonNull JetChunkBuilder setBlockLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level) {
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

        return JetChunk.create(this.server, this.dimensionType, chunkSections, lightSections, this.blockEntities);
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
        return new ChunkSectionBuilder(this.server, this.defaultBlockState, this.defaultBiome);
    }

    /**
     * Represents a builder of {@linkplain JetChunkSection a chunk section}.
     *
     * @since 1.0
     * @see JetChunkSection
     */
    private static final class ChunkSectionBuilder {

        private final List<BlockState> blockStates;
        private final List<RegistryEntry<Biome>> biomes;

        private final JetMinecraftServer server;

        /**
         * Constructs the {@linkplain ChunkSectionBuilder chunk section builder}.
         *
         * @param server a server that should own the chunk section
         * @param defaultBlockState a default block state that the block state list should be initially filled with
         * @param defaultBiome a registry entry of a default biome that the biome list should be initially filled with
         * @since 1.0
         */
        private ChunkSectionBuilder(@NonNull JetMinecraftServer server, @NonNull BlockState defaultBlockState,
                                    @NonNull JetRegistryEntry<Biome> defaultBiome) {
            int blockStateCount = ChunkPaletteType.BLOCK_STATE.elementCount();
            this.blockStates = new ArrayList<>(blockStateCount);

            for (int index = 0; index < blockStateCount; index++)
                this.blockStates.add(index, defaultBlockState);

            int biomeCount = ChunkPaletteType.BIOME.elementCount();
            this.biomes = new ArrayList<>(biomeCount);

            for (int index = 0; index < biomeCount; index++)
                this.biomes.add(index, defaultBiome);

            this.server = server;
        }

        /**
         * Sets {@linkplain BlockState a block state} specified to be present
         * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @param blockState the block state
         * @since 1.0
         */
        private void setBlockState(@NonNull ChunkPaletteRelativePosition position, @NonNull BlockState blockState) {
            if (position.paletteType() != ChunkPaletteType.BLOCK_STATE) {
                throw new IllegalArgumentException(
                        "The position specified has not been created for a block state chunk palette"
                );
            }
            this.blockStates.set(AbstractChunkPalette.calculateElementIndex(position), blockState);
        }

        /**
         * Sets {@linkplain Biome a biome} specified to be present
         * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @param biome a registry entry of the biome
         * @since 1.0
         */
        private void setBiome(@NonNull ChunkPaletteRelativePosition position, @NonNull RegistryEntry<Biome> biome) {
            if (!(biome instanceof JetRegistryEntry<Biome>))
                throw new IllegalArgumentException("The biome registry entry specified is not a valid registry entry");

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
            JetRegistryManager registryManager = this.server.registryManager();
            return new JetChunkSection(
                    this.server,
                    AbstractChunkPalette.create(
                            ChunkPaletteType.BLOCK_STATE,
                            registryManager.blockStateRegistry().order(),
                            this.blockStates
                    ),
                    AbstractChunkPalette.create(
                            ChunkPaletteType.BIOME,
                            registryManager.biomeRegistry().elementOrder(),
                            this.biomes
                    )
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
        /**
         * Sets a skylight level specified to be set
         * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @param level the skylight level
         * @since 1.0
         */
        private void setSkyLight(@NonNull ChunkPaletteRelativePosition position, byte level) {
            // TODO
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
            // TODO
        }

        /**
         * Builds the {@linkplain JetLightSection light section}.
         *
         * @return the light section
         * @since 1.0
         */
        private @NonNull JetLightSection build() {
            // TODO
            return new JetLightSection(EmptyLightStorage.INSTANCE, EmptyLightStorage.INSTANCE);
        }
    }
}