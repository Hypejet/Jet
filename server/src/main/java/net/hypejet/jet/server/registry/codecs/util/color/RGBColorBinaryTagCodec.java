package net.hypejet.jet.server.registry.codecs.util.color;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.util.color.RGBColor;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
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
        if (!(binaryTag instanceof IntBinaryTag tag))
            throw new IllegalArgumentException("The encoded tag must be of int type to decode it to a color");
        return new RGBColor(tag.value());
    }

    @Override
    public BinaryTag encode(RGBColor value) {
        return IntBinaryTag.intBinaryTag(value.value());
    }
}