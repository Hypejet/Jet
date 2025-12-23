package net.hypejet.jet.server.entity.cow;

import net.hypejet.jet.entity.variant.cow.MushroomCowVariant;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain MushroomCowVariant mushroom cow variants}.
 *
 * @since 1.0
 * @see MushroomCowVariant
 */
@NullMarked
public final class MushroomCowVariantRegistry {

    private static final Index<MushroomCowVariant, Integer> VARIANTS = IndexUtil.fromMap(Map.of(
            0, MushroomCowVariant.RED,
            1, MushroomCowVariant.BROWN
    ));

    private MushroomCowVariantRegistry() {}

    /**
     * Gets a registered {@linkplain MushroomCowVariant mushroom cow variant} by its numeric identifier.
     *
     * @param id the numeric identifier
     * @return the registered mushroom cow variant
     * @since 1.0
     */
    public static MushroomCowVariant variantById(int id) {
        return VARIANTS.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain MushroomCowVariant mushroom cow variant}.
     *
     * @param variant the mushroom cow variant whose numeric identifier should be returned
     * @return the numeric identifier
     * @since 1.0
     */
    public static int variantId(MushroomCowVariant variant) {
        return VARIANTS.valueOrThrow(variant);
    }
}