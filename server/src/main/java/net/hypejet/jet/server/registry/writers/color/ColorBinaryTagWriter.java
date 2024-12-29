package net.hypejet.jet.server.registry.writers.color;

import net.hypejet.jet.data.model.api.color.Color;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.IntBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain Color a color} into
 * {@linkplain IntBinaryTag an integer binary tag}.
 *
 * @since 1.0
 * @see Color
 * @see IntBinaryTag
 * @see Writer
 */
public final class ColorBinaryTagWriter implements Writer<Color, IntBinaryTag> {
    /**
     * An instance of the {@linkplain ColorBinaryTagWriter color binary tag writer}.
     *
     * @since 1.0
     */
    public static final ColorBinaryTagWriter INSTANCE = new ColorBinaryTagWriter();

    private ColorBinaryTagWriter() {}

    @Override
    public @NonNull IntBinaryTag write(@NonNull Color object) {
        return IntBinaryTag.intBinaryTag(object.value());
    }
}