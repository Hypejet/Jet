package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain Key keys}.
 *
 * @since 1.0
 * @see Key
 * @see BinaryTagCodec
 */
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

    private final Character prefix;

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
    public @NotNull Key decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof StringBinaryTag tag) {
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
    public @NotNull BinaryTag encode(@NotNull Key decoded) throws Exception {
        String value = decoded.asString();
        if (this.prefix != null)
            value = this.prefix + value;
        return StringBinaryTag.stringBinaryTag(value);
    }
}