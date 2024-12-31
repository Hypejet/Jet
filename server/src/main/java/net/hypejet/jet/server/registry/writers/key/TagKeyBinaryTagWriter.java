package net.hypejet.jet.server.registry.writers.key;

import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain Key a key}, which represents a tag into a single
 * {@linkplain StringBinaryTag string binary tag}.
 *
 * @since 1.0
 * @see Key
 * @see StringBinaryTag
 * @see Writer
 */
public final class TagKeyBinaryTagWriter implements Writer<Key, StringBinaryTag> {

    public static final char HASH_CHAR = '#';
    public static final String HASH_STRING = String.valueOf(HASH_CHAR);

    /**
     * An instance of the {@linkplain TagKeyBinaryTagWriter tag key binary tag writer}.
     *
     * @since 1.0
     */
    public static final TagKeyBinaryTagWriter INSTANCE = new TagKeyBinaryTagWriter();

    private TagKeyBinaryTagWriter() {}

    @Override
    public @NonNull StringBinaryTag write(@NonNull Key object) {
        return StringBinaryTag.stringBinaryTag(HASH_CHAR + object.asString());
    }
}