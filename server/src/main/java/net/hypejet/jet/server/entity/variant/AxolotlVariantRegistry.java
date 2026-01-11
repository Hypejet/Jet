package net.hypejet.jet.server.entity.variant;

import net.hypejet.jet.entity.variant.axolotl.AxolotlVariant;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain AxolotlVariant axolotl variants}.
 *
 * @since 1.0
 * @see AxolotlVariant
 */
@NullMarked
public final class AxolotlVariantRegistry {

    private static final Index<AxolotlVariant, Integer> AXOLOTL_VARIANTS = IndexUtil.fromMap(Map.of(
            0, AxolotlVariant.LUCY,
            1, AxolotlVariant.WILD,
            2, AxolotlVariant.GOLD,
            3, AxolotlVariant.CYAN,
            4, AxolotlVariant.BLUE
    ));

    private AxolotlVariantRegistry() {}

    /**
     * Gets a registered {@linkplain AxolotlVariant axolotl variant} by its numeric identifier.
     *
     * @param id the numeric identifier
     * @return the axolotl variant
     * @since 1.0
     */
    public static AxolotlVariant axolotlVariant(int id) {
        return AXOLOTL_VARIANTS.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain AxolotlVariant axolotl variant}.
     *
     * @param variant the axolotl variant whose numeric identifier should be returned
     * @return the numeric identifier
     * @since 1.0
     */
    public static int axolotlVariantId(AxolotlVariant variant) {
        return AXOLOTL_VARIANTS.valueOrThrow(variant);
    }
}