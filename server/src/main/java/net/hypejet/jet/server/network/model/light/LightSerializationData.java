package net.hypejet.jet.server.network.model.light;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.LightSection;
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

/**
 * Represents a packet serialization data of light of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
 *
 * @param skyLightMask a bitset containing bits for each section of the chunk, a section below the lowest section
 *                     and a section above the highest section, each bit set indicates that an associated section has
 *                     data in the skylight data, the least significant bit is for the lowest section and the most
 *                     significant bit is for the highest section
 * @param blockLightMask a bitset containing bits for each section of the chunk, a section below the lowest section
 *                       and a section above the highest section, each bit set indicates that an associated section has
 *                       data in the block-light data, the least significant bit is for the lowest section and the most
 *                       significant bit is for the highest section
 * @param emptySkyLightMask a bitset containing bits for each section of the chunk, a section below the lowest section
 *                          and a section above the highest section, each bit set indicates that an associated section
 *                          has the lowest skylight level available set for all its blocks, the least significant bit
 *                          is for the lowest section and the most significant bit is for the highest section
 * @param emptyBlockLightMask a bitset containing bits for each section of the chunk, a section below the lowest
 *                            section and a section above the highest section, each bit set indicates that
 *                            an associated section has the lowest block-light level available set for all its blocks,
 *                            the least significant bit is for the lowest section and the most significant bit is for
 *                            the highest section
 * @param skyLightData a list containing an array with skylight data for each section of the chunk with a bit set in
 *                     the skylight mask, starting with the lowest value, half a byte per light value is used
 * @param blockLightData a list containing an array with block-light data for each section of the chunk with a bit set
 *                       in the block-light mask, starting with the lowest value, half a byte per light value is used
 */
public record LightSerializationData(
        @NonNull UnmodifiableBitSet skyLightMask, @NonNull UnmodifiableBitSet blockLightMask,
        @NonNull UnmodifiableBitSet emptySkyLightMask, @NonNull UnmodifiableBitSet emptyBlockLightMask,
        @NonNull List<UnmodifiableByteArray> skyLightData, @NonNull List<UnmodifiableByteArray> blockLightData
) {
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
     *                          the least significant bit    is for the lowest section and the most significant bit
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
     */
    public LightSerializationData {
        NullabilityUtil.requireNonNull(skyLightMask, "skylight mask");
        NullabilityUtil.requireNonNull(blockLightMask, "block light mask");
        NullabilityUtil.requireNonNull(emptySkyLightMask, "empty skylight mask");
        NullabilityUtil.requireNonNull(emptyBlockLightMask, "empty block light mask");

        skyLightData = List.copyOf(NullabilityUtil.requireNonNull(skyLightData, "skylight data"));
        blockLightData = List.copyOf(NullabilityUtil.requireNonNull(blockLightData, "block light data"));
    }

    /**
     * Creates {@linkplain LightSerializationData a light serialization data} for a list
     * of {@linkplain LightSection light sections} specified.
     *
     * @param lightSections the light sections
     * @return the light serialization data
     * @since 1.0
     */
    public static @NonNull LightSerializationData create(@NonNull List<LightSection> lightSections) {
        return create(null, lightSections);
    }

    /**
     * Creates {@linkplain LightSerializationData a light serialization data} for a list
     * of {@linkplain LightSection light sections} specified.
     *
     * <p>If a list of previous {@linkplain LightSection light sections} is also specified, the serialization data
     * contains only data that changed. It means that bits in present and empty bitmasks are set to {@code 0} for
     * data of the light sections in common.</p>
     *
     * @param lightSections the light sections
     * @return the light serialization data
     * @since 1.0
     */
    public static @NonNull LightSerializationData create(@Nullable List<LightSection> previousLightSections,
                                                         @NonNull List<LightSection> lightSections) {
        NullabilityUtil.requireNonNull(lightSections, "light sections");

        BitSet skyLightMask = new BitSet();
        BitSet blockLightMask = new BitSet();

        BitSet emptySkyLightMask = new BitSet();
        BitSet emptyBlockLightMask = new BitSet();

        List<UnmodifiableByteArray> skyLightData = new ArrayList<>();
        List<UnmodifiableByteArray> blockLightData = new ArrayList<>();

        int nextDataIndex = 0;
        for (int index = 0; index < lightSections.size(); index++) {
            LightSection section = lightSections.get(index);

            LightStorage previousSkyLightStorage = null;
            LightStorage previousBlockLightStorage = null;

            if (previousLightSections != null) {
                LightSection previousSection = previousLightSections.get(index);
                previousSkyLightStorage = previousSection.skyLightStorage();
                previousBlockLightStorage = previousSection.blockLightStorage();
            }

            int dataIndex = nextDataIndex++;

            apply(previousSkyLightStorage, section.skyLightStorage(), index,
                    dataIndex, skyLightMask, emptySkyLightMask, skyLightData);
            apply(previousBlockLightStorage, section.blockLightStorage(), index,
                    dataIndex, blockLightMask, emptyBlockLightMask, blockLightData);
        }

        return new LightSerializationData(
                new UnmodifiableBitSet(skyLightMask), new UnmodifiableBitSet(blockLightMask),
                new UnmodifiableBitSet(emptySkyLightMask), new UnmodifiableBitSet(emptyBlockLightMask),
                skyLightData, blockLightData
        );
    }

    private static void apply(@Nullable LightStorage previousLightStorage, @NonNull LightStorage storage, int bitIndex,
                              int dataIndex, @NonNull BitSet lightMask, @NonNull BitSet emptyLightMask,
                              @NonNull List<UnmodifiableByteArray> lightData) {
        if (storage.equals(previousLightStorage))
            return;

        switch (storage) {
            case DirectLightStorage direct -> {
                lightMask.set(bitIndex);
                lightData.add(dataIndex, new UnmodifiableByteArray(direct.data()));
            }
            case EmptyLightStorage ignored -> emptyLightMask.set(bitIndex);
        }
    }
}