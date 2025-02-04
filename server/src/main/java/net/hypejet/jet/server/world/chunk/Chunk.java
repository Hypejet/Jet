package net.hypejet.jet.server.world.chunk;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.entity.BlockEntity;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMapType;
import net.hypejet.jet.server.world.chunk.light.LightSection;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSection;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.chunk.update.BiomeUpdate;
import net.hypejet.jet.server.world.chunk.update.BlockStateUpdate;
import net.hypejet.jet.server.world.chunk.update.LightUpdate;
import net.hypejet.jet.server.world.coordinate.relative.ChunkRelativePosition;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.ChunkBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents part of {@linkplain JetWorld a world}.
 *
 * @since 1.0
 * @see JetWorld
 */
public final class Chunk {

    private final DimensionType dimensionType;

    private final ChunkSectionList chunkSectionList;
    private final LightSectionList lightSectionList;

    private final Set<HeightMap> heightMaps;
    private final Set<BlockEntity> blockEntities;

    /**
     * Constructs the {@linkplain Chunk chunk}.
     *
     * @param dimensionType a dimension type of world that should be associated with the chunk
     * @param chunkSectionList a chunk section list, which contains all chunk sections that the chunk should have
     * @param lightSectionList a light section list, which contains all light sections that the chunk should have
     * @param heightMaps a set of height maps that the chunk should have
     * @param blockEntities a set of block entities that the chunk should have
     * @since 1.0
     */
    private Chunk(@NonNull DimensionType dimensionType, @NonNull ChunkSectionList chunkSectionList,
                  @NonNull LightSectionList lightSectionList, @NonNull Set<HeightMap> heightMaps,
                  @NonNull Set<BlockEntity> blockEntities) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");

        this.chunkSectionList = NullabilityUtil.requireNonNull(chunkSectionList, "chunk section list");
        this.lightSectionList = NullabilityUtil.requireNonNull(lightSectionList, "light section list");

        this.heightMaps = Set.copyOf(NullabilityUtil.requireNonNull(heightMaps, "height maps"));
        this.blockEntities = Set.copyOf(NullabilityUtil.requireNonNull(blockEntities, "block entities"));
    }

    /**
     * Gets {@linkplain ChunkSectionList a chunk section list}, which stores
     * all {@linkplain ChunkSection chunk sections} of this chunk.
     *
     * @return the chunk section list
     * @since 1.0
     */
    public @NonNull ChunkSectionList chunkSectionList() {
        return this.chunkSectionList;
    }

    /**
     * Gets {@linkplain LightSectionList a light section list}, which stores
     * all {@linkplain LightSection light sections} of this chunk.
     *
     * @return the light section list
     * @since 1.0
     */
    public @NonNull LightSectionList lightSectionList() {
        return this.lightSectionList;
    }

    /**
     * Gets {@linkplain Set a set} of {@linkplain HeightMap height maps} of the chunk.
     *
     * @return the set
     * @since 1.0
     */
    public @NonNull Set<HeightMap> heightMaps() {
        return this.heightMaps;
    }

    /**
     * Gets {@linkplain Set a set} of {@linkplain BlockEntity block entities} of the chunk.
     *
     * @return the set
     * @since 1.0
     */
    public @NonNull Set<BlockEntity> blockEntities() {
        return this.blockEntities;
    }

    /**
     * Creates {@linkplain Chunk a chunk}, which is a copy of this chunk with updates specified applied.
     *
     * @param blockStateUpdates updates that should be applied to block states
     * @param biomeUpdates updates that should be applied to biomes
     * @param lightUpdates updates that should be applied to light
     * @return the chunk created
     * @since 1.0
     */
    public @NonNull Chunk withUpdates(@NonNull Collection<BlockStateUpdate> blockStateUpdates,
                                      @NonNull Collection<BiomeUpdate> biomeUpdates,
                                      @NonNull Collection<LightUpdate> lightUpdates) {
        ChunkSectionList chunkSectionList = this.chunkSectionList.withUpdates(blockStateUpdates, biomeUpdates);
        LightSectionList lightSectionList = this.lightSectionList.withUpdates(lightUpdates);

        Set<HeightMap> heightMaps = new HashSet<>();
        for (HeightMap heightMap : this.heightMaps)
            heightMaps.add(heightMap.withUpdates(chunkSectionList, blockStateUpdates));

        // TODO: Handle block entity updates
        return new Chunk(this.dimensionType, chunkSectionList, lightSectionList, heightMaps, this.blockEntities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chunk otherChunk)) return false;
        return Objects.equals(this.dimensionType, otherChunk.dimensionType)
                && Objects.equals(this.chunkSectionList, otherChunk.chunkSectionList)
                && Objects.equals(this.lightSectionList, otherChunk.lightSectionList)
                && Objects.equals(this.heightMaps, otherChunk.heightMaps)
                && Objects.equals(this.blockEntities, otherChunk.blockEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dimensionType, this.chunkSectionList,
                this.lightSectionList, this.heightMaps, this.blockEntities);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "dimensionType=" + this.dimensionType +
                ", chunkSectionList=" + this.chunkSectionList +
                ", lightSectionList=" + this.lightSectionList +
                ", heightMaps=" + this.heightMaps +
                ", blockEntities=" + this.blockEntities +
                '}';
    }

    /**
     * Represents a builder of {@linkplain Chunk a chunk} and implementation
     * of {@linkplain ChunkBuilder a chunk builder}.
     *
     * @since 1.0
     * @see Chunk
     * @see ChunkBuilder
     */
    public static final class Builder implements ChunkBuilder {

        private final DimensionType dimensionType;

        private final ChunkSectionList.Builder chunkSectionListBuilder;
        private final LightSectionList.Builder lightSectionListBuilder;

        /**
         * Constructs the {@linkplain Builder chunk builder}.
         *
         * @param dimensionType a dimension type of world that the chunk is created for
         * @param blockStateOrder an element order of block states that should be used for creation
         *                        of block state palettes
         * @param biomeOrder an element order of biomes that should be used for creation of biome palettes
         * @since 1.0
         */
        public Builder(@NonNull DimensionType dimensionType, @NonNull ElementOrder<JetBlockState> blockStateOrder,
                       @NonNull ElementOrder<JetRegistryEntry<Biome>> biomeOrder) {
            this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");

            NullabilityUtil.requireNonNull(blockStateOrder, "block state order");
            NullabilityUtil.requireNonNull(biomeOrder, "biome order");

            this.chunkSectionListBuilder = new ChunkSectionList.Builder(dimensionType, blockStateOrder, biomeOrder);
            this.lightSectionListBuilder = new LightSectionList.Builder(dimensionType);
        }

        @Override
        public void setBlockState(byte x, short y, byte z, @NonNull BlockState blockState) {
            NullabilityUtil.requireNonNull(blockState, "block state");
            if (!(blockState instanceof JetBlockState validatedBlockState))
                throw new IllegalArgumentException("The block state specified is not a valid block state");
            this.setBlockState(new ChunkRelativePosition(x, y, z, ChunkPaletteType.BLOCK_STATE), validatedBlockState);
        }

        @Override
        public void setBiome(byte x, short y, byte z, @NonNull RegistryEntry<Biome> biome) {
            NullabilityUtil.requireNonNull(biome, "biome");
            if (!(biome instanceof JetRegistryEntry<Biome> validatedBiome))
                throw new IllegalArgumentException("The biome registry entry specified is not a valid registry entry");
            this.setBiome(new ChunkRelativePosition(x, y, z, ChunkPaletteType.BIOME), validatedBiome);
        }

        @Override
        public void setSkyLight(byte x, short y, byte z, byte value) {
            this.setSkyLight(new ChunkRelativePosition(x, y, z, ChunkPaletteType.BLOCK_STATE), value);
        }

        @Override
        public void setBlockLight(byte x, short y, byte z, byte value) {
            this.setBlockLight(new ChunkRelativePosition(x, y, z, ChunkPaletteType.BLOCK_STATE), value);
        }

        /**
         * Sets {@linkplain JetBlockState a block state} that should be present
         * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
         *
         * @param position the chunk-relative position
         * @param blockState the block state
         * @since 1.0
         */
        public void setBlockState(@NonNull ChunkRelativePosition position, @NonNull JetBlockState blockState) {
            NullabilityUtil.requireNonNull(position, "position");
            NullabilityUtil.requireNonNull(blockState, "block state");
            this.chunkSectionListBuilder.setBlockState(position, blockState);
        }

        /**
         * Sets {@linkplain Biome a biome} that should be present
         * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
         *
         * @param position the chunk-relative position
         * @param biome a registry entry of the biome
         * @since 1.0
         */
        public void setBiome(@NonNull ChunkRelativePosition position, @NonNull JetRegistryEntry<Biome> biome) {
            NullabilityUtil.requireNonNull(position, "position");
            NullabilityUtil.requireNonNull(biome, "biome");
            this.chunkSectionListBuilder.setBiome(position, biome);
        }

        /**
         * Sets a skylight value that should be set for a block
         * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
         *
         * @param position the chunk-relative position
         * @param value the skylight value
         * @since 1.0
         */
        public void setSkyLight(@NonNull ChunkRelativePosition position, byte value) {
            NullabilityUtil.requireNonNull(position, "position");
            this.lightSectionListBuilder.setSkyLight(position, value);
        }

        /**
         * Sets a block light value that should be set for a block
         * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
         *
         * @param position the chunk-relative position
         * @param value the block light value
         * @since 1.0
         */
        public void setBlockLight(@NonNull ChunkRelativePosition position, byte value) {
            NullabilityUtil.requireNonNull(position, "position");
            this.lightSectionListBuilder.setBlockLight(position, value);
        }

        /**
         * Builds {@linkplain Chunk a chunk} with data set in this builder.
         *
         * @return the chunk
         * @since 1.0
         */
        public @NonNull Chunk build() {
            ChunkSectionList chunkSectionList = this.chunkSectionListBuilder.build();
            LightSectionList lightSectionList = this.lightSectionListBuilder.build();

            Set<HeightMap> heightMaps = new HashSet<>();
            for (HeightMapType heightMapType : HeightMapType.values())
                heightMaps.add(HeightMap.create(heightMapType, chunkSectionList, this.dimensionType));

            // TODO: Block entities
            return new Chunk(this.dimensionType, chunkSectionList, lightSectionList, Set.copyOf(heightMaps), Set.of());
        }
    }
}