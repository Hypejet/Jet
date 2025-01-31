package net.hypejet.jet.server.world.chunk;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.function.ShortAndObjectToIntFunction;
import net.hypejet.jet.server.util.function.ToByteFunction;
import net.hypejet.jet.server.util.function.ToShortFunction;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.entity.BlockEntity;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMapType;
import net.hypejet.jet.server.world.chunk.light.LightSection;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.chunk.section.ChunkSection;
import net.hypejet.jet.server.world.chunk.update.BiomeUpdate;
import net.hypejet.jet.server.world.chunk.update.BlockStateUpdate;
import net.hypejet.jet.server.world.chunk.update.LightUpdate;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents part of {@linkplain JetWorld a world}.
 *
 * @since 1.0
 * @see JetWorld
 */
// TODO: Handle block entities
public final class Chunk {

    private final DimensionType dimensionType;

    private final Set<HeightMap> heightMaps;
    private final List<ChunkSection> sections;
    private final Set<BlockEntity> blockEntities;
    private final List<LightSection> lightSections;

    /**
     * Constructs the {@linkplain Chunk chunk}.
     *
     * @param dimensionType a dimension type of world that should be associated with the chunk
     * @param heightMaps a set of height maps that the chunk should have
     * @param sections a list of sections that the chunk should have, the list is ordered from lowest to highest
     *                 section
     * @param blockEntities a set of block entities that the chunk should have
     * @param lightSections a list of light sections that the chunk should have, each chunk section should have a light
     *                      section associated in this list and additionally one light section above the highest chunk
     *                      section and one light section below the lowest chunk section
     * @since 1.0
     */
    private Chunk(@NonNull DimensionType dimensionType, @NonNull Set<HeightMap> heightMaps,
                  @NonNull List<ChunkSection> sections, @NonNull Set<BlockEntity> blockEntities,
                  @NonNull List<LightSection> lightSections) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.heightMaps = Set.copyOf(NullabilityUtil.requireNonNull(heightMaps, "height maps"));
        this.sections = List.copyOf(NullabilityUtil.requireNonNull(sections, "sections"));
        this.blockEntities = Set.copyOf(NullabilityUtil.requireNonNull(blockEntities, "block entities"));
        this.lightSections = List.copyOf(NullabilityUtil.requireNonNull(lightSections, "light sections"));
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
     * Gets {@linkplain List a list} of {@linkplain ChunkSection chunk sections} of the chunk.
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<ChunkSection> sections() {
        return this.sections;
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
     * Gets {@linkplain List a list} of {@linkplain LightSection light sections} of the chunk. Each chunk section
     * has a light section associated in the list and additionally one light section above the highest chunk section
     * and one light section below the lowest chunk section
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<LightSection> lightSections() {
        return this.lightSections;
    }

    /**
     * Gets {@linkplain JetBlockState a block state} at a block coordinate with values specified.
     *
     * @param sectionRelativeX a section-relative {@code X} value of the block coordinate
     * @param blockY an absolute {@code Y} value of the block coordinate
     * @param sectionRelativeZ a section-relative {@code Z} value of the block coordinate
     * @return the block state
     * @since 1.0
     */
    public @NonNull JetBlockState getBlockState(byte sectionRelativeX, short blockY, byte sectionRelativeZ) {
        int sectionIndex = createChunkSectionIndex(blockY, this.dimensionType);
        ChunkSection section = this.sections.get(sectionIndex);

        if (section == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a chunk section for block with Y coordinate value of %d", blockY
            ));
        }

        byte sectionRelativeY = createSectionRelativeBlockCoordinate(blockY);
        return section.blockStatePalette().getElement(sectionRelativeX, sectionRelativeY, sectionRelativeZ);
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
        List<ChunkSection> chunkSections = this.createUpdatedChunkSections(blockStateUpdates, biomeUpdates);
        List<LightSection> lightSections = this.createUpdatedLightSections(lightUpdates);

        Set<HeightMap> heightMaps = new HashSet<>();
        for (HeightMap heightMap : this.heightMaps)
            heightMaps.add(heightMap.withUpdates(chunkSections, blockStateUpdates));

        return new Chunk(this.dimensionType, Set.copyOf(heightMaps), chunkSections, this.blockEntities, lightSections);
    }

    private @NonNull List<ChunkSection> createUpdatedChunkSections(
            @NonNull Collection<BlockStateUpdate> blockStateUpdates,
            @NonNull Collection<BiomeUpdate> biomeUpdates
    ) {
        if (blockStateUpdates.isEmpty())
            return this.sections;

        List<ChunkSection> chunkSections = new ArrayList<>(this.sections);

        IntObjectMap<List<ChunkPaletteUpdate<JetBlockState>>> blockStatePaletteUpdates;
        IntObjectMap<List<ChunkPaletteUpdate<JetRegistryEntry<Biome>>>> biomePaletteUpdates;

        blockStatePaletteUpdates = createChunkSectionPaletteUpdateMap(
                blockStateUpdates, this.dimensionType, BlockStateUpdate::blockX, BlockStateUpdate::blockY,
                BlockStateUpdate::blockZ, BlockStateUpdate::blockState, Chunk::createSectionRelativeBlockCoordinate,
                Chunk::createChunkSectionIndex
        );

        biomePaletteUpdates = createChunkSectionPaletteUpdateMap(
                biomeUpdates, this.dimensionType, BiomeUpdate::biomeX, BiomeUpdate::biomeY, BiomeUpdate::biomeZ,
                BiomeUpdate::biome, Chunk::createSectionRelativeBiomeCoordinate, Chunk::createSectionIndexForBiome
        );

        boolean sectionListUpdated = false;
        for (int index = 0; index < chunkSections.size(); index++) {
            List<ChunkPaletteUpdate<JetBlockState>> blockStateUpdateList = blockStatePaletteUpdates.get(index);
            List<ChunkPaletteUpdate<JetRegistryEntry<Biome>>> biomeUpdateList = biomePaletteUpdates.get(index);

            if (blockStateUpdateList == null && biomeUpdateList == null)
                continue;

            if (blockStateUpdateList == null)
                blockStateUpdateList = List.of();
            if (biomeUpdateList == null)
                biomeUpdateList = List.of();

            ChunkSection section = chunkSections.get(index);
            ChunkSection updatedSection = section.withUpdates(blockStateUpdateList, biomeUpdateList);

            if (section.equals(updatedSection))
                continue;
            if (!sectionListUpdated)
                sectionListUpdated = true;

            chunkSections.set(index, updatedSection);
        }

        if (!sectionListUpdated)
            return this.sections;
        return List.copyOf(chunkSections);
    }

    private @NonNull List<LightSection> createUpdatedLightSections(@NonNull Collection<LightUpdate> updates) {
        if (updates.isEmpty())
            return this.lightSections;

        List<LightSection> lightSections = new ArrayList<>(this.lightSections);

        IntObjectMap<List<LightStorageUpdate>> indexToSkyLightUpdateMap = new IntObjectHashMap<>();
        IntObjectMap<List<LightStorageUpdate>> indexToBlockLightUpdateMap = new IntObjectHashMap<>();

        for (LightUpdate update : updates) {
            byte blockX = update.blockX();
            short blockY = update.blockY();
            byte blockZ = update.blockZ();

            /* Light section index is a chunk section index + 1, since chunk stores also light sections for
               blocks below the lowest chunk section and blocks above the highest chunk section. */
            int sectionIndex = createChunkSectionIndex(blockY, this.dimensionType) + 1;
            byte sectionRelativeY = createSectionRelativeBlockCoordinate(blockY);

            IntObjectMap<List<LightStorageUpdate>> indexToLightUpdateMap = switch (update.lightType()) {
                case SKY -> indexToSkyLightUpdateMap;
                case BLOCK -> indexToBlockLightUpdateMap;
            };

            List<LightStorageUpdate> lightStorageUpdates;
            if (indexToLightUpdateMap.containsKey(sectionIndex)) {
                lightStorageUpdates = indexToLightUpdateMap.get(sectionRelativeY);
            } else {
                lightStorageUpdates = new ArrayList<>();
                indexToLightUpdateMap.put(sectionRelativeY, lightStorageUpdates);
            }

            lightStorageUpdates.add(new LightStorageUpdate(blockX, sectionRelativeY, blockZ, update.lightValue()));
        }

        boolean sectionListUpdated = false;
        for (int index = 0; index < lightSections.size(); index++) {
            List<LightStorageUpdate> skyLightUpdates = indexToSkyLightUpdateMap.get(index);
            List<LightStorageUpdate> blockLightUpdates = indexToBlockLightUpdateMap.get(index);

            if (skyLightUpdates == null && blockLightUpdates == null)
                continue;

            if (skyLightUpdates == null)
                skyLightUpdates = List.of();
            if (blockLightUpdates == null)
                blockLightUpdates = List.of();

            LightSection section = lightSections.get(index);
            LightSection updatedSection = section.withUpdates(skyLightUpdates, blockLightUpdates);

            if (section.equals(updatedSection))
                continue;
            if (!sectionListUpdated)
                sectionListUpdated = true;

            lightSections.set(index, section);
        }

        if (!sectionListUpdated)
            return this.lightSections;
        return List.copyOf(lightSections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chunk otherChunk)) return false;
        return Objects.equals(this.dimensionType, otherChunk.dimensionType)
                && Objects.equals(this.heightMaps, otherChunk.heightMaps)
                && Objects.equals(this.sections, otherChunk.sections)
                && Objects.equals(this.blockEntities, otherChunk.blockEntities)
                && Objects.equals(this.lightSections, otherChunk.lightSections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dimensionType, this.heightMaps,
                this.sections, this.blockEntities, this.lightSections);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "dimensionType=" + this.dimensionType +
                ", heightMaps=" + this.heightMaps +
                ", sections=" + this.sections +
                ", blockEntities=" + this.blockEntities +
                ", lightSections=" + this.lightSections +
                '}';
    }

    /**
     * Creates {@linkplain Chunk a chunk}.
     *
     * @param dimensionType a dimension type of world that should be associated with the chunk
     * @param sections a list of sections that the chunk should have, the list is ordered from lowest to highest
     *                 section
     * @param blockEntities a set of block entities that the chunk should have
     * @param lightSections a list of light sections that the chunk should have, each chunk section should have a light
     *                      section associated in this list and additionally one light section above the highest chunk
     *                      section and one light section below the lowest chunk section
     * @return the chunk
     * @since 1.0
     */
    public static @NonNull Chunk create(
            @NonNull DimensionType dimensionType, @NonNull List<ChunkSection> sections,
            @NonNull List<LightSection> lightSections, @NonNull Set<BlockEntity> blockEntities
    ) {
        NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        NullabilityUtil.requireNonNull(sections, "sections");
        NullabilityUtil.requireNonNull(lightSections, "light sections");
        NullabilityUtil.requireNonNull(blockEntities, "block entities");

        Set<HeightMap> heightMaps = new HashSet<>();
        for (HeightMapType heightMapType : HeightMapType.values())
            heightMaps.add(HeightMap.create(heightMapType, sections, dimensionType));

        return new Chunk(dimensionType, Set.copyOf(heightMaps), sections, blockEntities, lightSections);
    }

    /**
     * Creates an index of {@linkplain ChunkSection a chunk section} where block with height specified is stored.
     *
     * <p>The index can be applied to get the chunk section in any {@linkplain Chunk chunk} that belongs
     * to {@linkplain JetWorld a world} with {@linkplain DimensionType dimension type} specified.</p>
     *
     * @param blockY the block height
     * @param dimensionType the dimension type
     * @return the index
     * @since 1.0
     */
    public static int createChunkSectionIndex(int blockY, @NonNull DimensionType dimensionType) {
        byte blockStatePaletteAxisLength = ChunkPaletteType.BLOCK_STATE.axisLength();
        int minimumSectionY = Math.floorDiv(dimensionType.minY(), blockStatePaletteAxisLength);
        return Math.floorDiv(blockY, blockStatePaletteAxisLength) - minimumSectionY;
    }

    /**
     * Creates a section-relative coordinate value for an absolute block coordinate value specified.
     *
     * @param blockCoordinate the absolute block coordinate value
     * @return the section-relative coordinate value
     * @since 1.0
     */
    public static byte createSectionRelativeBlockCoordinate(int blockCoordinate) {
        return createSectionRelativeCoordinate(blockCoordinate, ChunkPaletteType.BLOCK_STATE.axisLength());
    }

    /**
     * Creates a section-relative coordinate value for an absolute biome coordinate value specified.
     *
     * @param blockCoordinate the absolute biome coordinate value
     * @return the section-relative coordinate value
     * @since 1.0
     */
    public static byte createSectionRelativeBiomeCoordinate(int blockCoordinate) {
        return createSectionRelativeCoordinate(blockCoordinate, ChunkPaletteType.BIOME.axisLength());
    }

    private static <E, U> @NonNull IntObjectMap<List<ChunkPaletteUpdate<E>>> createChunkSectionPaletteUpdateMap(
            @NonNull Collection<U> updates, @NonNull DimensionType dimensionType,
            @NonNull ToByteFunction<U> sectionRelativeXFunction, @NonNull ToShortFunction<U> absoluteYFunction,
            @NonNull ToByteFunction<U> sectionRelativeZFunction, @NonNull Function<U, E> elementFunction,
            @NonNull Short2ByteFunction sectionRelativeYFunction,
            @NonNull ShortAndObjectToIntFunction<DimensionType> sectionIndexFunction
    ) {
        IntObjectMap<List<ChunkPaletteUpdate<E>>> map = new IntObjectHashMap<>();

        for (U update : updates) {
            byte sectionRelativeX = sectionRelativeXFunction.apply(update);
            short absoluteY = absoluteYFunction.apply(update);
            byte sectionRelativeZ = sectionRelativeZFunction.apply(update);

            int sectionIndex = sectionIndexFunction.apply(absoluteY, dimensionType);
            byte sectionRelativeY = sectionRelativeYFunction.get(absoluteY);

            List<ChunkPaletteUpdate<E>> paletteUpdates;
            if (map.containsKey(sectionIndex)) {
                paletteUpdates = map.get(sectionRelativeY);
            } else {
                paletteUpdates = new ArrayList<>();
                map.put(sectionRelativeY, paletteUpdates);
            }

            paletteUpdates.add(new ChunkPaletteUpdate<>(
                    sectionRelativeX, sectionRelativeY,
                    sectionRelativeZ, elementFunction.apply(update)
            ));
        }

        return map;
    }

    private static int createSectionIndexForBiome(short biomeY, @NonNull DimensionType dimensionType) {
        int biomeAxisLength = ChunkPaletteType.BIOME.axisLength();
        int blockStateAxisLength = ChunkPaletteType.BLOCK_STATE.axisLength();

        int biomeToBlockYMultiplier = blockStateAxisLength / biomeAxisLength;
        int blockY = biomeY * biomeToBlockYMultiplier;

        return createChunkSectionIndex(blockY, dimensionType);
    }

    private static byte createSectionRelativeCoordinate(int blockCoordinate, byte axisLength) {
        byte result = (byte) (blockCoordinate % axisLength);
        return result < 0 ? (byte) (axisLength + result) : result;
    }
}