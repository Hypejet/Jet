package net.hypejet.jet.server.registry.codecs.identifier;

import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes a {@linkplain Key key}
 * as a hashed ({@code #}) string.
 *
 * @since 1.0
 * @author Codetech
 * @see Key
 * @see BinaryTagCodec
 */
public final class TagIdentifierBinaryTagCodec implements BinaryTagCodec<Key> {

    public static final char HASH_CHAR = '#';
    public static final String HASH_STRING = String.valueOf(HASH_CHAR);

    private static final TagIdentifierBinaryTagCodec INSTANCE = new TagIdentifierBinaryTagCodec();

    private TagIdentifierBinaryTagCodec() {}

    @Override
    public @NonNull Key read(@NonNull BinaryTag tag) {
        if (!(tag instanceof StringBinaryTag stringTag))
            throw new IllegalArgumentException("The binary tag specified must be a string binary tag");
        return Key.key(stringTag.value().replaceFirst(HASH_STRING, ""));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull Key object) {
        return StringBinaryTag.stringBinaryTag(HASH_CHAR + object.asString());
    }

    /**
     * Gets an instance of the {@linkplain TagIdentifierBinaryTagCodec tag identifier binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull TagIdentifierBinaryTagCodec instance() {
        return INSTANCE;
    }
}