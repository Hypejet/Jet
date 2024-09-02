package net.hypejet.jet.server.registry.codecs;

import net.hypejet.jet.data.model.color.Color;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec}, which reads and writes a {@linkplain Color color}.
 *
 * @since 1.0
 * @author Codestech
 * @see Color
 * @see BinaryTagCodec
 */
public final class ColorBinaryTagCodec implements BinaryTagCodec<Color> {

    private static final ColorBinaryTagCodec INSTANCE = new ColorBinaryTagCodec();

    private ColorBinaryTagCodec() {}

    @Override
    public @NonNull Color read(@NonNull BinaryTag tag) {
        if (!(tag instanceof IntBinaryTag binaryTag))
            throw new IllegalArgumentException("The binary tag is not an integer binary tag");
        return new Color(binaryTag.value());
    }

    @Override
    public @NonNull BinaryTag write(@NonNull Color object) {
        return IntBinaryTag.intBinaryTag(object.value());
    }

    /**
     * Gets an instance of the {@linkplain ColorBinaryTagCodec color binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ColorBinaryTagCodec instance() {
        return INSTANCE;
    }
}