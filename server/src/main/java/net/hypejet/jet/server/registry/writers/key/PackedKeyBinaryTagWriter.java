package net.hypejet.jet.server.registry.writers.key;

import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain Key a key} into a single
 * {@linkplain StringBinaryTag string binary tag}.
 *
 * @since 1.0
 * @see Key
 * @see StringBinaryTag
 * @see Writer
 */
public final class PackedKeyBinaryTagWriter implements Writer<Key, StringBinaryTag> {
    /**
     * An instance of the {@linkplain PackedKeyBinaryTagWriter packet key binary tag writer}.
     *
     * @since 1.0
     */
    public static final PackedKeyBinaryTagWriter INSTANCE = new PackedKeyBinaryTagWriter();

    private PackedKeyBinaryTagWriter() {}

    @Override
    public @NonNull StringBinaryTag write(@NonNull Key object) {
        return StringBinaryTag.stringBinaryTag(object.asString());
    }
}