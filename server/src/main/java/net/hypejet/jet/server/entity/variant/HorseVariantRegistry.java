package net.hypejet.jet.server.entity.variant;

import net.hypejet.jet.entity.variant.horse.HorseVariant;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain HorseVariant horse variants}.
 *
 * @since 1.0
 * @see HorseVariant
 */
@NullMarked
public final class HorseVariantRegistry {

    private static final Index<HorseVariant, Integer> HORSE_VARIANTS = IndexUtil.fromMap(Map.of(
            0, HorseVariant.WHITE,
            1, HorseVariant.CREAMY,
            2, HorseVariant.CHESTNUT,
            3, HorseVariant.BROWN,
            4, HorseVariant.BLACK,
            5, HorseVariant.GRAY,
            6, HorseVariant.DARK_BROWN
    ));

    private HorseVariantRegistry() {}

    /**
     * Gets a registered {@linkplain HorseVariant horse variant} by its numeric identifier.
     *
     * @param id the numeric identifier
     * @return the horse variant
     * @since 1.0
     */
    public static HorseVariant horseVariant(int id) {
        return HORSE_VARIANTS.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain HorseVariant horse variant}.
     *
     * @param variant the horse variant whose numeric identifier should be returned
     * @return the numeric identifier
     * @since 1.0
     */
    public static int horseVariantId(HorseVariant variant) {
        return HORSE_VARIANTS.valueOrThrow(variant);
    }
}