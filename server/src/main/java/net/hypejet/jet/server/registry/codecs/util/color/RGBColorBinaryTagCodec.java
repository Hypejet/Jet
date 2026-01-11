package net.hypejet.jet.server.registry.codecs.util.color;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.util.color.RGBColor;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.colorComponent;

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
            return RGBColor.fromRGB(
                    colorComponent(0, listTag),
                    colorComponent(1, listTag),
                    colorComponent(2, listTag)
            );
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
}