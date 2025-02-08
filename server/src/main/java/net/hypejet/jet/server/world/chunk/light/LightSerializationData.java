package net.hypejet.jet.server.world.chunk.light;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.storage.DirectLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.LightStorage;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.hypejet.jet.util.bitset.UnmodifiableBitSet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

/**
 * Represents a packet serialization data of light of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public final class LightSerializationData {

    private final UnmodifiableBitSet skyLightMask;
    private final UnmodifiableBitSet blockLightMask;

    private final UnmodifiableBitSet emptySkyLightMask;
    private final UnmodifiableBitSet emptyBlockLightMask;

    private final List<UnmodifiableByteArray> skyLightData;
    private final List<UnmodifiableByteArray> blockLightData;

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
            @NonNull List<UnmodifiableByteArray> skyLightData, @NonNull List<UnmodifiableByteArray> blockLightData
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
    public @NonNull List<UnmodifiableByteArray> skyLightData() {
        return this.skyLightData;
    }

    /**
     * Gets {@linkplain List a list} containing an array with block-light data for each section of the chunk with
     * a bit set in the block-light mask, starting with the lowest value, half a byte per light value is used.
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<UnmodifiableByteArray> blockLightData() {
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
        return create(null, lightSectionList);
    }

    /**
     * Creates {@linkplain LightSerializationData a light serialization data}
     * for {@linkplain LightSectionList a light section list} specified.
     *
     * <p>If a previous {@linkplain LightSectionList light section list} is also specified, the serialization data
     * contains only data that changed. It means that bits in present and empty bitmasks are set to {@code 0} for
     * data of the light sections in common.</p>
     *
     * @param previousLightSectionList the previous light section list, {@code null} if none
     * @param lightSectionList the light section list
     * @return the light serialization data
     * @since 1.0
     */
    public static @NonNull LightSerializationData create(@Nullable LightSectionList previousLightSectionList,
                                                         @NonNull LightSectionList lightSectionList) {
        NullabilityUtil.requireNonNull(lightSectionList, "light section list");

        BitSet skyLightMask = new BitSet();
        BitSet blockLightMask = new BitSet();

        BitSet emptySkyLightMask = new BitSet();
        BitSet emptyBlockLightMask = new BitSet();

        List<UnmodifiableByteArray> skyLightData = new ArrayList<>();
        List<UnmodifiableByteArray> blockLightData = new ArrayList<>();

        List<LightSection> lightSections = lightSectionList.sections();
        for (int index = 0; index < lightSections.size(); index++) {
            LightSection section = lightSections.get(index);

            LightStorage previousSkyLightStorage = null;
            LightStorage previousBlockLightStorage = null;

            if (previousLightSectionList != null) {
                LightSection previousSection = previousLightSectionList.sections().get(index);
                previousSkyLightStorage = previousSection.skyLightStorage();
                previousBlockLightStorage = previousSection.blockLightStorage();
            }

            add(previousSkyLightStorage, section.skyLightStorage(), index, skyLightMask, emptySkyLightMask,
                    skyLightData);
            add(previousBlockLightStorage, section.blockLightStorage(), index, blockLightMask, emptyBlockLightMask,
                    blockLightData);
        }

        return new LightSerializationData(
                new UnmodifiableBitSet(skyLightMask), new UnmodifiableBitSet(blockLightMask),
                new UnmodifiableBitSet(emptySkyLightMask), new UnmodifiableBitSet(emptyBlockLightMask),
                skyLightData, blockLightData
        );
    }

    private static void add(@Nullable LightStorage previousLightStorage, @NonNull LightStorage storage, int bitIndex,
                            @NonNull BitSet lightMask, @NonNull BitSet emptyLightMask,
                            @NonNull List<UnmodifiableByteArray> lightData) {
        if (storage.equals(previousLightStorage))
            return;

        switch (storage) {
            case DirectLightStorage direct -> {
                lightMask.set(bitIndex);
                lightData.add(new UnmodifiableByteArray(direct.data()));
            }
            case EmptyLightStorage ignored -> emptyLightMask.set(bitIndex);
        }
    }
}