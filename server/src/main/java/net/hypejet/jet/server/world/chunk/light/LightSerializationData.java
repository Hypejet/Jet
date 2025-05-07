package net.hypejet.jet.server.world.chunk.light;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.DirectLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.util.array.NibbleArray;
import net.hypejet.jet.util.bitset.UnmodifiableBitSet;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents a packet serialization data of light of {@linkplain JetChunk a chunk}.
 *
 * @since 1.0
 * @see JetChunk
 */
public final class LightSerializationData {

    private final UnmodifiableBitSet skyLightMask;
    private final UnmodifiableBitSet blockLightMask;

    private final UnmodifiableBitSet emptySkyLightMask;
    private final UnmodifiableBitSet emptyBlockLightMask;

    private final List<NibbleArray> skyLightData;
    private final List<NibbleArray> blockLightData;

    /**
     * Constructs the {@linkplain LightSerializationData light serialization data}.
     *
     * @param skyLightMask a bitset containing bits for each section of the chunk, a section below the lowest section
     *                     and a section above the highest section, each bit set indicates that an associated section
     *                     has data in the skylight data, the least significant bit is for the lowest section
     *                     and the most significant bit is for the highest section
     * @param blockLightMask a bitset containing bits for each section of the chunk, a section below the lowest section
     *                       and a section above the highest section, each bit set indicates that an associated section
     *                       has data in the block-light data, the least significant bit is for the lowest section
     *                       and the most significant bit is for the highest section
     * @param emptySkyLightMask a bitset containing bits for each section of the chunk, a section below the lowest
     *                          section and a section above the highest section, each bit set indicates that
     *                          an associated section has the lowest skylight level available set for all its blocks,
     *                          the least significant bit is for the lowest section and the most significant bit
     *                          is for the highest section
     * @param emptyBlockLightMask a bitset containing bits for each section of the chunk, a section below the lowest
     *                            section and a section above the highest section, each bit set indicates that
     *                            an associated section has the lowest block-light level available set for all its
     *                            blocks, the least significant bit is for the lowest section and the most significant
     *                            bit is for the highest section
     * @param skyLightData a list containing an array with skylight data for each section of the chunk with a bit set
     *                     in the skylight mask, starting with the lowest value, half a byte per light value is used
     * @param blockLightData a list containing an array with block-light data for each section of the chunk with a bit
     *                       set in the block-light mask, starting with the lowest value, half a byte per light value
     *                       is used
     * @since 1.0
     */
    private LightSerializationData(
            @NonNull UnmodifiableBitSet skyLightMask, @NonNull UnmodifiableBitSet blockLightMask,
            @NonNull UnmodifiableBitSet emptySkyLightMask, @NonNull UnmodifiableBitSet emptyBlockLightMask,
            @NonNull List<NibbleArray> skyLightData, @NonNull List<NibbleArray> blockLightData
    ) {
        this.skyLightMask = NullabilityUtil.requireNonNull(skyLightMask, "skylight mask");
        this.blockLightMask = NullabilityUtil.requireNonNull(blockLightMask, "block light mask");
        this.emptySkyLightMask = NullabilityUtil.requireNonNull(emptySkyLightMask, "empty skylight mask");
        this.emptyBlockLightMask = NullabilityUtil.requireNonNull(emptyBlockLightMask, "empty block light mask");
        this.skyLightData = List.copyOf(NullabilityUtil.requireNonNull(skyLightData, "skylight data"));
        this.blockLightData = List.copyOf(NullabilityUtil.requireNonNull(blockLightData, "block light data"));
    }

    /**
     * Gets a bitset containing bits for each section of the chunk, a section below the lowest section
     * and a section above the highest section, each bit set indicates that an associated section has data
     * in the skylight data, the least significant bit is for the lowest section and the most significant bit
     * is for the highest section.
     *
     * @return the bitset
     * @since 1.0
     */
    public @NonNull UnmodifiableBitSet skyLightMask() {
        return this.skyLightMask;
    }

    /**
     * Gets a bitset containing bits for each section of the chunk, a section below the lowest section and a section
     * above the highest section, each bit set indicates that an associated section has data in the block-light data,
     * the least significant bit is for the lowest section and the most significant bit is for the highest section.
     *
     * @return the bitset
     * @since 1.0
     */
    public @NonNull UnmodifiableBitSet blockLightMask() {
        return this.blockLightMask;
    }

    /**
     * Gets a bitset containing bits for each section of the chunk, a section below the lowest section and a section
     * above the highest section, each bit set indicates that an associated section has the lowest skylight level
     * available set for all its blocks, the least significant bit is for the lowest section and the most significant
     * bit is for the highest section.
     *
     * @return the bitset
     * @since 1.0
     */
    public @NonNull UnmodifiableBitSet emptySkyLightMask() {
        return this.emptySkyLightMask;
    }

    /**
     * Gets a bitset containing bits for each section of the chunk, a section below the lowest section and a section
     * above the highest section, each bit set indicates that an associated section has the lowest block-light level
     * available set for all its blocks, the least significant bit is for the lowest section and the most significant
     * bit is for the highest section.
     *
     * @return the bitset
     * @since 1.0
     */
    public @NonNull UnmodifiableBitSet emptyBlockLightMask() {
        return this.emptyBlockLightMask;
    }

    /**
     * Gets {@linkplain List a list} containing an array with skylight data for each section of the chunk with
     * a bit set in the skylight mask, starting with the lowest value, half a byte per light value is used.
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<NibbleArray> skyLightData() {
        return this.skyLightData;
    }

    /**
     * Gets {@linkplain List a list} containing an array with block-light data for each section of the chunk with
     * a bit set in the block-light mask, starting with the lowest value, half a byte per light value is used.
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<NibbleArray> blockLightData() {
        return this.blockLightData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LightSerializationData otherData)) return false;
        return Objects.equals(this.skyLightMask, otherData.skyLightMask)
                && Objects.equals(this.blockLightMask, otherData.blockLightMask)
                && Objects.equals(this.emptySkyLightMask, otherData.emptySkyLightMask)
                && Objects.equals(this.emptyBlockLightMask, otherData.emptyBlockLightMask)
                && Objects.equals(this.skyLightData, otherData.skyLightData)
                && Objects.equals(this.blockLightData, otherData.blockLightData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.skyLightMask, this.blockLightMask, this.emptySkyLightMask,
                this.emptyBlockLightMask, this.skyLightData, this.blockLightData);
    }

    @Override
    public String toString() {
        return "LightSerializationData{" +
                "skyLightMask=" + this.skyLightMask +
                ", blockLightMask=" + this.blockLightMask +
                ", emptySkyLightMask=" + this.emptySkyLightMask +
                ", emptyBlockLightMask=" + this.emptyBlockLightMask +
                ", skyLightData=" + this.skyLightData +
                ", blockLightData=" + this.blockLightData +
                '}';
    }

    /**
     * Creates {@linkplain LightSerializationData a light serialization data}
     * for {@linkplain LightSectionList a light section list} specified.
     *
     * @param lightSectionList the light section list
     * @return the light serialization data
     * @since 1.0
     */
    public static @NonNull LightSerializationData create(@NonNull LightSectionList lightSectionList) {
        NullabilityUtil.requireNonNull(lightSectionList, "light section list");

        IntObjectMap<AbstractLightStorage> indexToSkyLightStorageMap = new IntObjectHashMap<>();
        IntObjectMap<AbstractLightStorage> indexToBlockLightStorageMap = new IntObjectHashMap<>();

        List<JetLightSection> lightSections = lightSectionList.sections();
        for (int index = 0; index < lightSections.size(); index++) {
            JetLightSection lightSection = lightSections.get(index);
            indexToSkyLightStorageMap.put(index, lightSection.skyLightStorage());
            indexToBlockLightStorageMap.put(index, lightSection.blockLightStorage());
        }

        return create(indexToSkyLightStorageMap, indexToBlockLightStorageMap);
    }

    /**
     * Creates {@linkplain LightSerializationData a light serialization data} acknowledging light updates of blocks
     * with {@linkplain ChunkRelativeBlockPosition chunk-relative block positions} specified.
     *
     * @param affectedBlockPositions the chunk-relative block positions
     * @param updatedChunkSectionList a light section list with the light updates specified performed
     * @param dimensionType a dimension type of world of a chunk that the light serialization data is created for
     * @return the light serialization data
     * @since 1.0
     */
    public static @NonNull LightSerializationData create(
            @NonNull Set<ChunkRelativeBlockPosition> skyLightAffectedBlockPositions,
            @NonNull Set<ChunkRelativeBlockPosition> blockLightAffectedBlockPositions,
            @NonNull LightSectionList updatedChunkSectionList, @NonNull DimensionType dimensionType
    ) {
        return create(
                createIndexToLightStorageMap(
                        skyLightAffectedBlockPositions, dimensionType,
                        JetLightSection::skyLightStorage, updatedChunkSectionList
                ),
                createIndexToLightStorageMap(
                        blockLightAffectedBlockPositions, dimensionType,
                        JetLightSection::blockLightStorage, updatedChunkSectionList
                )
        );
    }

    private static @NonNull LightSerializationData create(
            @NonNull IntObjectMap<AbstractLightStorage> indexToSkyLightStorageMap,
            @NonNull IntObjectMap<AbstractLightStorage> indexToBlockLightStorageMap
    ) {
        BitSet skyLightMask = new BitSet();
        BitSet blockLightMask = new BitSet();

        BitSet emptySkyLightMask = new BitSet();
        BitSet emptyBlockLightMask = new BitSet();

        List<NibbleArray> skyLightData = new ArrayList<>();
        List<NibbleArray> blockLightData = new ArrayList<>();

        set(indexToSkyLightStorageMap, skyLightMask, emptySkyLightMask, skyLightData);
        set(indexToBlockLightStorageMap, blockLightMask, emptyBlockLightMask, blockLightData);

        return new LightSerializationData(
                new UnmodifiableBitSet(skyLightMask), new UnmodifiableBitSet(blockLightMask),
                new UnmodifiableBitSet(emptySkyLightMask), new UnmodifiableBitSet(emptyBlockLightMask),
                skyLightData, blockLightData
        );
    }

    private static void set(@NonNull IntObjectMap<AbstractLightStorage> indexToLightStorageMap,
                            @NonNull BitSet lightMask, @NonNull BitSet emptyLightMask,
                            @NonNull List<NibbleArray> lightData) {
        IntList keyList = new IntArrayList(indexToLightStorageMap.keySet());
        keyList.sort(Integer::compare);

        for (int keyIndex = 0; keyIndex < keyList.size(); keyIndex++) {
            int sectionIndex = keyList.getInt(keyIndex);
            AbstractLightStorage lightStorage = indexToLightStorageMap.get(sectionIndex);

            switch (lightStorage) {
                case DirectLightStorage direct -> {
                    lightMask.set(sectionIndex);
                    lightData.add(direct.data());
                }
                case EmptyLightStorage ignored -> emptyLightMask.set(sectionIndex);
            }
        }
    }

    private static @NonNull IntObjectMap<AbstractLightStorage> createIndexToLightStorageMap(
            @NonNull Set<ChunkRelativeBlockPosition> affectedBlockPositions, @NonNull DimensionType dimensionType,
            @NonNull Function<JetLightSection, AbstractLightStorage> lightStorageFunction,
            @NonNull LightSectionList updatedChunkSectionList
    ) {
        IntObjectMap<AbstractLightStorage> indexToLightStorageMap = new IntObjectHashMap<>();
        for (ChunkRelativeBlockPosition position : affectedBlockPositions) {
            int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);
            int index = LightSectionList.createSectionIndex(sectionY, dimensionType);

            if (indexToLightStorageMap.containsKey(index)) continue;
            indexToLightStorageMap.put(index, lightStorageFunction.apply(updatedChunkSectionList.section(sectionY)));
        }
        return indexToLightStorageMap;
    }
}