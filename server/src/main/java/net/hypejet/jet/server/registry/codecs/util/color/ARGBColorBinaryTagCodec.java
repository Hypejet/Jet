package net.hypejet.jet.server.registry.codecs.util.color;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.util.color.ARGBColor;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.colorComponent;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain ARGBColor ARGB colors}.
 *
 * @since 1.0
 * @see ARGBColor
 * @see BinaryTagCodec
 */
@NullMarked
public final class ARGBColorBinaryTagCodec implements BinaryTagCodec<ARGBColor> {
    /**
     * An instance of the {@linkplain ARGBColorBinaryTagCodec ARGB color binary-tag codec}.
     *
     * @since 1.0
     */
    public static final ARGBColorBinaryTagCodec INSTANCE = new ARGBColorBinaryTagCodec();

    private ARGBColorBinaryTagCodec() {}

    @Override
    public ARGBColor decode(BinaryTag binaryTag) {
        if (binaryTag instanceof IntBinaryTag intTag) {
            return new ARGBColor(intTag.value());
        } else if (binaryTag instanceof ListBinaryTag listTag) {
            if (listTag.size() != 4) {
                throw new IllegalArgumentException(
                        "The list binary tag representing an ARGB color must contain exactly 4 elements"
                );
            }
            return ARGBColor.fromARGB(
                    colorComponent(3, listTag),
                    colorComponent(0, listTag),
                    colorComponent(1, listTag),
                    colorComponent(2, listTag)
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of int of float-list type to decode it to an ARGB color"
            );
        }
    }

    @Override
    public BinaryTag encode(ARGBColor value) {
        return IntBinaryTag.intBinaryTag(value.value());
    }
}