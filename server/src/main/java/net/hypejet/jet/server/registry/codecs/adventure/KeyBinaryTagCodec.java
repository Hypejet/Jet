package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain Key keys}.
 *
 * @since 1.0
 * @see Key
 * @see BinaryTagCodec
 */
@NullMarked
public final class KeyBinaryTagCodec implements BinaryTagCodec<Key> {

    /**
     * An instance of the {@linkplain KeyBinaryTagCodec key binary-tag codec} without a prefix.
     *
     * @since 1.0
     */
    public static final KeyBinaryTagCodec INSTANCE = new KeyBinaryTagCodec(null);

    /**
     * An instance of the {@linkplain KeyBinaryTagCodec key binary-tag codec} with a {@code #} prefix.
     *
     * @since 1.0
     */
    public static final KeyBinaryTagCodec HASHED_INSTANCE = new KeyBinaryTagCodec('#');

    private final @Nullable Character prefix;

    /**
     * Constructs the {@linkplain KeyBinaryTagCodec key binary-tag codec}.
     *
     * @param prefix a prefix to be applied before the key string, {@code null} if nothing should be applied
     * @since 1.0
     */
    private KeyBinaryTagCodec(@Nullable Character prefix) {
        this.prefix = prefix;
    }

    @Override
    public Key decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof StringBinaryTag tag) {
            String value = tag.value();
            if (this.prefix != null) {
                if (value.startsWith(String.valueOf(this.prefix))) {
                    value = value.substring(1);
                } else {
                    throw new IllegalArgumentException(String.format(
                            "The key is expected to be prefixed with '%s' character",
                            this.prefix
                    ));
                }
            }
            return Key.key(value);
        } else {
            throw new IllegalArgumentException("The encoded tag must be of string type to decode it to a key");
        }
    }

    @Override
    public BinaryTag encode(Key value, JetMinecraftServer server) {
        String stringValue = value.asString();
        if (this.prefix != null)
            stringValue = this.prefix + stringValue;
        return StringBinaryTag.stringBinaryTag(stringValue);
    }
}