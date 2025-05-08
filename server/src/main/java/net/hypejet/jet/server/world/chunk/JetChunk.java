package net.hypejet.jet.server.world.chunk;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMapType;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.light.LightSerializationData;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain Chunk a chunk}.
 *
 * @since 1.0
 * @see Chunk
 */
public final class JetChunk implements Chunk {

    private final ChunkSectionList chunkSectionList;
    private final LightSectionList lightSectionList;

    private final Map<HeightMapType, HeightMap> heightMaps;
    private final Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities;

    /**
     * Constructs the {@linkplain JetChunk chunk implementation}.
     *
     * @param chunkSectionList a chunk section list, which contains all chunk sections that the chunk should have
     * @param lightSectionList a light section list, which contains all light sections that the chunk should have
     * @param heightMaps a set of height maps that the chunk should have
     * @param blockEntities a map, which maps chunk-relative block positions of blocks that the chunk should have
     *                      to data of block entities associated with them
     * @since 1.0
     */
    private JetChunk(@NonNull ChunkSectionList chunkSectionList,
                     @NonNull LightSectionList lightSectionList, @NonNull Set<HeightMap> heightMaps,
                     @NonNull Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities) {
        this.chunkSectionList = NullabilityUtil.requireNonNull(chunkSectionList, "chunk section list");
        this.lightSectionList = NullabilityUtil.requireNonNull(lightSectionList, "light section list");

        NullabilityUtil.requireNonNull(heightMaps, "height maps");
        NullabilityUtil.requireNonNull(blockEntities, "block entities");

        EnumMap<HeightMapType, HeightMap> heightMapsMap = new EnumMap<>(HeightMapType.class);
        for (HeightMap heightMap : heightMaps)
            heightMapsMap.put(heightMap.type(), heightMap);

        this.heightMaps = Maps.immutableEnumMap(heightMapsMap);
        this.blockEntities = Map.copyOf(blockEntities);
    }

    @Override
    public @NonNull List<JetChunkSection> sections() {
        return this.chunkSectionList.sections();
    }

    @Override
    public @NonNull List<JetLightSection> lightSections() {
        return this.lightSectionList.sections();
    }

    @Override
    public @NonNull Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities() {
        return this.blockEntities;
    }

    /**
     * Gets {@linkplain ChunkSectionList a chunk section list} of {@linkplain JetChunkSection chunk sections}
     * of this {@linkplain JetChunk chunk}.
     *
     * @return the chunk section list
     * @since 1.0
     */
    public @NonNull ChunkSectionList chunkSectionList() {
        return this.chunkSectionList;
    }

    /**
     * Gets {@linkplain ChunkSectionList a chunk section list} of {@linkplain JetLightSection light sections}
     * of this {@linkplain JetChunk chunk}.
     *
     * @return the light section list
     * @since 1.0
     */
    public @NonNull LightSectionList lightSectionList() {
        return this.lightSectionList;
    }

    /**
     * Gets {@linkplain Map a map} that maps {@linkplain HeightMapType height map types}
     * to chunk {@linkplain HeightMap height maps} of the corresponding types.
     *
     * @return the set
     * @since 1.0
     */
    public @NonNull Map<HeightMapType, HeightMap> heightMaps() {
        return this.heightMaps;
    }

    /**
     * Gets {@linkplain LightSerializationData a light serialization data} of light data of this chunk.
     *
     * @return the light serialization data
     * @since 1.0
     */
    public @NonNull LightSerializationData lightSerializationData() {
        return this.lightSectionList.serializationData();
    }

    /**
     * Creates {@linkplain JetChunk a chunk}, which is a copy of this chunk with updates specified applied.
     *
     * @param blockStateUpdates a map which maps chunk-relative block positions to new block states
     *                          that should be present there
     * @param blockEntityUpdates a map which maps chunk-relative block positions to new block entities that blocks
     *                           at these block positions should have
     * @param biomeUpdates a map which maps chunk-relative biome positions to registry entries of new biomes
     *                     that should be present there
     * @param skyLightUpdates a map which maps chunk-relative block positions to new skylight level values that blocks
     *                        at these block positions should have
     * @param blockLightUpdates a map which maps chunk-relative block positions to new block-light level values
     *                          that blocks at these block positions should have
     * @return the chunk created
     * @since 1.0
     */
    public @NonNull JetChunk withUpdates(
            @NonNull Map<ChunkRelativeBlockPosition, JetBlockState> blockStateUpdates,
            @NonNull Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntityUpdates,
            @NonNull Map<ChunkRelativeBiomePosition, RegistryEntry<Biome>> biomeUpdates,
            @NonNull Object2ByteMap<ChunkRelativeBlockPosition> skyLightUpdates,
            @NonNull Object2ByteMap<ChunkRelativeBlockPosition> blockLightUpdates
    ) {
        ChunkSectionList chunkSectionList = this.chunkSectionList.withUpdates(blockStateUpdates, biomeUpdates);
        LightSectionList lightSectionList = this.lightSectionList.withUpdates(skyLightUpdates, blockLightUpdates);

        Set<HeightMap> heightMaps = new HashSet<>();
        for (HeightMap heightMap : this.heightMaps.values())
            heightMaps.add(heightMap.withUpdates(chunkSectionList, blockStateUpdates));

        Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntityDataMap = new HashMap<>(this.blockEntities);
        for (Map.Entry<ChunkRelativeBlockPosition, JetBlockState> update : blockStateUpdates.entrySet()) {
            ChunkRelativeBlockPosition position = update.getKey();
            if (update.getValue().blockType() != this.blockState(position).blockType())
                blockEntityDataMap.remove(position);
        }

        blockEntityDataMap.putAll(blockEntityUpdates);
        return new JetChunk(chunkSectionList, lightSectionList, heightMaps, blockEntityDataMap);
    }

    /**
     * Gets {@linkplain BlockState a block state} of a block, which is in this {@linkplain JetChunk chunk}
     * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified.
     *
     * @param position the chunk-relative block position
     * @return the block state
     * @since 1.0
     */
    public @NonNull BlockState blockState(@NonNull ChunkRelativeBlockPosition position) {
        JetChunkSection chunkSection = this.chunkSectionList.sectionFor(position);
        ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);
        return chunkSection.blockStatePalette().getElement(palettePosition);
    }

    /**
     * Gets {@linkplain RegistryEntry a registry entry} of {@linkplain Biome a biome} which is in this
     * {@linkplain JetChunk chunk} at {@linkplain ChunkRelativeBiomePosition a chunk-relative biome position}
     * specified.
     *
     * @param position the chunk-relative biome position
     * @return the registry entry
     * @since 1.0
     */
    public @NonNull RegistryEntry<Biome> biome(@NonNull ChunkRelativeBiomePosition position) {
        JetChunkSection chunkSection = this.chunkSectionList.sectionFor(position);
        ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);
        return chunkSection.biomePalette().getElement(palettePosition);
    }

    /**
     * Gets data of a block entity of a block which is in this {@linkplain JetChunk chunk}
     * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified.
     *
     * @param position the chunk-relative block position
     * @return the block entity data
     * @since 1.0
     */
    public @Nullable CompoundBinaryTag blockEntityData(@NonNull ChunkRelativeBlockPosition position) {
        return this.blockEntities.get(position);
    }

    /**
     * Gets a value specifying a skylight level of a block which is in this {@linkplain JetChunk chunk}
     * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified.
     *
     * @param position the chunk-relative block position
     * @return the value
     * @since 1.0
     */
    public byte skyLightLevel(@NonNull ChunkRelativeBlockPosition position) {
        ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);
        return this.lightSection(position).skyLightStorage().getValue(palettePosition);
    }

    /**
     * Gets a value specifying a block light level of a block which is in this {@linkplain JetChunk chunk}
     * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified.
     *
     * @param position the chunk-relative block position
     * @return the value
     * @since 1.0
     */
    public byte blockLightLevel(@NonNull ChunkRelativeBlockPosition position) {
        ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);
        return this.lightSection(position).blockLightStorage().getValue(palettePosition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JetChunk otherChunk)) return false;
        return Objects.equals(this.chunkSectionList, otherChunk.chunkSectionList)
                && Objects.equals(this.lightSectionList, otherChunk.lightSectionList)
                && Objects.equals(this.heightMaps, otherChunk.heightMaps)
                && Objects.equals(this.blockEntities, otherChunk.blockEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chunkSectionList, this.lightSectionList, this.heightMaps, this.blockEntities);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "chunkSectionList=" + this.chunkSectionList +
                ", lightSectionList=" + this.lightSectionList +
                ", heightMaps=" + this.heightMaps +
                ", blockEntities=" + this.blockEntities +
                '}';
    }

    /**
     * Gets {@linkplain JetLightSection a light section}, which owns a value specifying light level of a block
     * which is in this {@linkplain JetChunk chunk}
     * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified.
     *
     * @param position the chunk-relaitve block position
     * @return the light section
     * @since 1.0
     */
    private @NonNull JetLightSection lightSection(@NonNull ChunkRelativeBlockPosition position) {
        return this.lightSectionList.sectionFor(position);
    }

    /**
     * Creates {@linkplain JetChunk a chunk}.
     *
     * @param dimensionType a registry entry of a dimension type of worlds that the chunk is created for
     * @param chunkSections a list of chunk sections that the chunk should have, where the lowest index is the lowest
     *                      section and the highest index is the highest section
     * @param lightSections a list of light sections that the chunk should have, where the lowest index is the lowest
     *                      section and the highest index is the highest section
     * @param blockEntities a map, which maps chunk-relative block positions of blocks to data of block entities that
     *                      the blocks should have
     * @return the chunk
     * @since 1.0
     */
    public static @NonNull JetChunk create(
            @NonNull DimensionType dimensionType,
            @NonNull List<JetChunkSection> chunkSections, @NonNull List<JetLightSection> lightSections,
            @NonNull Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities
    ) {
        NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        NullabilityUtil.requireNonNull(chunkSections, "chunk sections");
        NullabilityUtil.requireNonNull(lightSections, "light sections");
        NullabilityUtil.requireNonNull(blockEntities, "block entities");

        ChunkSectionList chunkSectionList = new ChunkSectionList(dimensionType, chunkSections);
        LightSectionList lightSectionList = new LightSectionList(dimensionType, lightSections);

        Set<HeightMap> heightMaps = new HashSet<>();
        for (HeightMapType type : HeightMapType.values())
            heightMaps.add(HeightMap.create(type, chunkSectionList));

        return new JetChunk(chunkSectionList, lightSectionList, heightMaps, blockEntities);
    }
}