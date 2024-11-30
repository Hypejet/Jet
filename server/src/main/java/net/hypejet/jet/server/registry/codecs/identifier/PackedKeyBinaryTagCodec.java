package net.hypejet.jet.server.registry.codecs.identifier;

import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary-tag codec}, which reads and writes a {@linkplain Key key}
 * from/to a single {@linkplain String string}.
 *
 * @since 1.0
 * @author Codestech
 * @see Key
 * @see BinaryTagCodec
 */
public final class PackedKeyBinaryTagCodec implements BinaryTagCodec<Key> {

    private static final PackedKeyBinaryTagCodec INSTANCE = new PackedKeyBinaryTagCodec();

    private PackedKeyBinaryTagCodec() {}

    @Override
    public @NonNull Key read(@NonNull BinaryTag tag) {
        if (!(tag instanceof StringBinaryTag stringBinaryTag))
            throw new IllegalArgumentException("The binary tag is not a string binary tag");
        return Key.key(stringBinaryTag.value());
    }

    @Override
    public @NonNull BinaryTag write(@NonNull Key object) {
        return StringBinaryTag.stringBinaryTag(object.asString());
    }

    /**
     * Gets an instance of the {@linkplain PackedKeyBinaryTagCodec packed-key binary-tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull PackedKeyBinaryTagCodec instance() {
        return INSTANCE;
    }
}