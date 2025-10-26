package net.hypejet.jet.server.registry.codecs.primitive;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain String strings}.
 *
 * @since 1.0
 * @see String
 * @see BinaryTagCodec
 */
@NullMarked
public final class StringBinaryTagCodec implements BinaryTagCodec<String> {
    /**
     * An instance of the {@linkplain StringBinaryTagCodec string binary tag codec}.
     *
     * @since 1.0
     */
    public static final StringBinaryTagCodec INSTANCE = new StringBinaryTagCodec();

    private StringBinaryTagCodec() {}

    @Override
    public String decode(BinaryTag binaryTag) {
        if (binaryTag instanceof StringBinaryTag tag) {
            return tag.value();
        } else {
            throw new IllegalArgumentException("The encoded tag must be of string type to decode it to a string");
        }
    }

    @Override
    public BinaryTag encode(String value) {
        return StringBinaryTag.stringBinaryTag(value);
    }
}