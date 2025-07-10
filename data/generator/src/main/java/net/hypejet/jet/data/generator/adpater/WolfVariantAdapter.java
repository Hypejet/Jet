package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain WolfVariant wolf variants} to a Jet data equivalent.
 *
 * @since 1.0
 * @see WolfVariant
 */
public final class WolfVariantAdapter {

    private WolfVariantAdapter() {}

    /**
     * Converts the specified {@linkplain JsonWolfVariant wolf variant} to a Jet data equivalent.
     *
     * @param variant the wolf variant to convert
     * @return the converted wolf variant
     * @since 1.0
     */
    public static @NonNull JsonWolfVariant convert(@NonNull WolfVariant variant) {
        WolfVariant.AssetInfo assetInfo = variant.assetInfo();
        return new JsonWolfVariant(
                KeyAdapter.convert(assetInfo.wild().id()),
                KeyAdapter.convert(assetInfo.tame().id()),
                KeyAdapter.convert(assetInfo.angry().id())
        );
    }
}