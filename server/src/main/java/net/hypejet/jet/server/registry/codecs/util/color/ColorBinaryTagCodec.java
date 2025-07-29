package net.hypejet.jet.server.registry.codecs.util.color;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.util.color.Color;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import org.jetbrains.annotations.NotNull;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Color colors}.
 *
 * @since 1.0
 * @see Color
 * @see BinaryTagCodec
 */
public final class ColorBinaryTagCodec implements BinaryTagCodec<Color> {
    /**
     * An instance of the {@linkplain ColorBinaryTagCodec color binary tag codec}.
     *
     * @since 1.0
     */
    public static final ColorBinaryTagCodec INSTANCE = new ColorBinaryTagCodec();

    private ColorBinaryTagCodec() {}

    @Override
    public @NotNull Color decode(@NotNull BinaryTag encoded) {
        if (!(encoded instanceof IntBinaryTag tag))
            throw new IllegalArgumentException("The encoded tag must be of int type to decode it to a color");
        return new Color(tag.value());
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Color decoded) {
        return IntBinaryTag.intBinaryTag(decoded.value());
    }
}