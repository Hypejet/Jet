package net.hypejet.jet.server.world.chunk.light;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.hypejet.jet.util.bitset.UnmodifiableBitSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents light data of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
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
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public record LightData(@NotNull UnmodifiableBitSet skyLightMask, @NotNull UnmodifiableBitSet blockLightMask,
                        @NotNull UnmodifiableBitSet emptySkyLightMask, @NotNull UnmodifiableBitSet emptyBlockLightMask,
                        @NotNull List<UnmodifiableByteArray> skyLightData,
                        @NotNull List<UnmodifiableByteArray> blockLightData) {
    /**
     * Constructs the {@linkplain LightData light data}.
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
     * @since 1.0
     */
    public LightData {
        NullabilityUtil.requireNonNull(skyLightMask, "sky light mask");
        NullabilityUtil.requireNonNull(blockLightMask, "block light mask");
        NullabilityUtil.requireNonNull(emptyBlockLightMask, "empty sky light mask");
        NullabilityUtil.requireNonNull(emptyBlockLightMask, "empty block light mask");
        skyLightData = List.copyOf(NullabilityUtil.requireNonNull(skyLightData, "skylight data"));
        blockLightData = List.copyOf(NullabilityUtil.requireNonNull(blockLightData, "block-light data"));
    }
}