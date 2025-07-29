package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Key keys}.
 *
 * @since 1.0
 * @see Key
 * @see BinaryTagCodec
 */
public final class KeyBinaryTagCodec implements BinaryTagCodec<Key> {
    /**
     * An instance of the {@linkplain KeyBinaryTagCodec key binary tag codec}.
     *
     * @since 1.0
     */
    public static final KeyBinaryTagCodec INSTANCE = new KeyBinaryTagCodec();

    private KeyBinaryTagCodec() {}

    @Override
    public @NotNull Key decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof StringBinaryTag tag) {
            return Key.key(tag.value());
        } else {
            throw new IllegalArgumentException("The encoded tag must be of string type to decode it to a key");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Key decoded) throws Exception {
        return StringBinaryTag.stringBinaryTag(decoded.asString());
    }
}