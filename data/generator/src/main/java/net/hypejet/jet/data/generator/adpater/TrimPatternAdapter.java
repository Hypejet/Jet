package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.trim.pattern.JsonTrimPattern;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain TrimPattern trim patterns} to a Jet data equivalent.
 * 
 * @since 1.0
 * @see TrimPattern
 */
public final class TrimPatternAdapter {

    private TrimPatternAdapter() {}

    /**
     * Converts the specified {@linkplain TrimPattern trim pattern} to a Jet data equivalent.
     *
     * @param pattern the trim pattern to convert
     * @return the converted trim pattern
     * @since 1.0
     */
    public static @NonNull JsonTrimPattern convert(@NonNull TrimPattern pattern) {
        return new JsonTrimPattern(
                KeyAdapter.convert(pattern.assetId()),
                ComponentAdapter.convert(pattern.description()),
                pattern.decal()
        );
    }
}