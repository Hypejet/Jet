package net.hypejet.jet.server.registry.codecs.util.color;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.util.color.RGBColor;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain RGBColor RGB colors}.
 *
 * @since 1.0
 * @see RGBColor
 * @see BinaryTagCodec
 */
@NullMarked
public final class RGBColorBinaryTagCodec implements BinaryTagCodec<RGBColor> {
    /**
     * An instance of the {@linkplain RGBColorBinaryTagCodec RGB color binary-tag codec}.
     *
     * @since 1.0
     */
    public static final RGBColorBinaryTagCodec INSTANCE = new RGBColorBinaryTagCodec();

    private RGBColorBinaryTagCodec() {}

    @Override
    public RGBColor decode(BinaryTag binaryTag) {
        if (binaryTag instanceof IntBinaryTag intTag) {
            return new RGBColor(intTag.value());
        } else if (binaryTag instanceof ListBinaryTag listTag) {
            if (listTag.size() != 3) {
                throw new IllegalArgumentException(
                        "The list binary tag representing an RGB color must contain exactly 3 elements"
                );
            }
            return RGBColor.fromRGB(component(0, listTag), component(1, listTag), component(2, listTag));
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of int of float-list type to decode it to an RGB color"
            );
        }
    }

    @Override
    public BinaryTag encode(RGBColor value) {
        return IntBinaryTag.intBinaryTag(value.value());
    }

    private static int component(int index, ListBinaryTag listTag) {
        if (!(listTag.get(index) instanceof FloatBinaryTag floatTag))
            throw new IllegalArgumentException("The binary tag at index " + index + " is not of the float type");
        return MathUtil.floor(floatTag.value() * 255f);
    }
}