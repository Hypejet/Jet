package net.hypejet.jet.server.registry.writers.registry.component;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain Style a style} into
 * {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see Style
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class StyleBinaryTagWriter implements Writer<Style, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain StyleBinaryTagWriter style binary tag writer}.
     *
     * @since 1.0
     */
    public static final StyleBinaryTagWriter INSTANCE = new StyleBinaryTagWriter(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    private StyleBinaryTagWriter(@NonNull NBTComponentSerializer serializer) {
        this.serializer = NullabilityUtil.requireNonNull(serializer, "serializer");
    }

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull Style object) {
        return this.serializer.serializeStyle(object);
    }
}