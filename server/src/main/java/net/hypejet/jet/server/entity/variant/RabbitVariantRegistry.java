package net.hypejet.jet.server.entity.variant;

import net.hypejet.jet.entity.variant.rabbit.RabbitVariant;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain RabbitVariant rabbit variants}.
 *
 * @since 1.0
 * @see RabbitVariant
 */
@NullMarked
public final class RabbitVariantRegistry {

    private static final Index<RabbitVariant, Integer> RABBIT_VARIANTS = IndexUtil.fromMap(Map.of(
            0, RabbitVariant.BROWN,
            1, RabbitVariant.WHITE,
            2, RabbitVariant.BLACK,
            3, RabbitVariant.WHITE_SPLOTCHED,
            4, RabbitVariant.GOLD,
            5, RabbitVariant.SALT,
            99, RabbitVariant.EVIL
    ));

    private RabbitVariantRegistry() {}

    /**
     * Gets a registered {@linkplain RabbitVariant rabbit variant} by its numeric identifier.
     *
     * @param id the numeric identifier
     * @return the rabbit variant
     * @since 1.0
     */
    public static RabbitVariant rabbitVariant(int id) {
        return RABBIT_VARIANTS.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain RabbitVariant rabbit variant}.
     *
     * @param variant the rabbit variant whose numeric identifier should be returned
     * @return the numeric identifier
     * @since 1.0
     */
    public static int rabbitVariantId(RabbitVariant variant) {
        return RABBIT_VARIANTS.valueOrThrow(variant);
    }
}