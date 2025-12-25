package net.hypejet.jet.server.util.game.color;

import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.util.game.color.DyeColor;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain DyeColor dye colors}.
 *
 * @since 1.0
 * @see DyeColor
 */
@NullMarked
public final class DyeColorRegistry {

    private static final Index<DyeColor, Integer> DYE_COLORS = IndexUtil.fromMap(Map.ofEntries(
            Map.entry(0, DyeColor.WHITE),
            Map.entry(1, DyeColor.ORANGE),
            Map.entry(2, DyeColor.MAGENTA),
            Map.entry(3, DyeColor.LIGHT_BLUE),
            Map.entry(4, DyeColor.YELLOW),
            Map.entry(5, DyeColor.LIME),
            Map.entry(6, DyeColor.PINK),
            Map.entry(7, DyeColor.GRAY),
            Map.entry(8, DyeColor.LIGHT_GRAY),
            Map.entry(9, DyeColor.CYAN),
            Map.entry(10, DyeColor.PURPLE),
            Map.entry(11, DyeColor.BLUE),
            Map.entry(12, DyeColor.BROWN),
            Map.entry(13, DyeColor.GREEN),
            Map.entry(14, DyeColor.RED),
            Map.entry(15, DyeColor.BLACK)
    ));

    private DyeColorRegistry() {}

    /**
     * Gets a registered {@linkplain DyeColor dye color} by its numeric identifier.
     *
     * @param id the numeric identifier
     * @return the dye color
     * @since 1.0
     */
    public static DyeColor dyeColor(int id) {
        return DYE_COLORS.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain DyeColor dye color}.
     *
     * @param color the dye color whose numeric identifier should be returned
     * @return the numeric identifier
     * @since 1.0
     */
    public static int dyeColorId(DyeColor color) {
        return DYE_COLORS.valueOrThrow(color);
    }
}