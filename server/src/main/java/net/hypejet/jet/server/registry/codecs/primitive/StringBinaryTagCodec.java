package net.hypejet.jet.server.registry.codecs.primitive;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain String strings}.
 *
 * @since 1.0
 * @see String
 * @see BinaryTagCodec
 */
public final class StringBinaryTagCodec implements BinaryTagCodec<String> {
    /**
     * An instance of the {@linkplain StringBinaryTagCodec string binary tag codec}.
     *
     * @since 1.0
     */
    public static final StringBinaryTagCodec INSTANCE = new StringBinaryTagCodec();

    private StringBinaryTagCodec() {}

    @Override
    public @NotNull String decode(@NotNull BinaryTag encoded) {
        if (encoded instanceof StringBinaryTag tag) {
            return tag.value();
        } else {
            throw new IllegalArgumentException("The encoded tag must be of string type to decode it to a string");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull String decoded) {
        return StringBinaryTag.stringBinaryTag(decoded);
    }
}