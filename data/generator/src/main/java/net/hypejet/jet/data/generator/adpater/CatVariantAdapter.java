package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.variant.cat.JsonCatVariant;
import net.minecraft.world.entity.animal.CatVariant;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain CatVariant cat variants} to a Jet data equivalent.
 *
 * @since 1.0
 * @see CatVariant
 */
public final class CatVariantAdapter {

    private CatVariantAdapter() {}

    /**
     * Converts the specified {@linkplain JsonCatVariant cat variant} to a Jet data equivalent.
     *
     * @param variant the cat variant to convert
     * @return the converted cat variant
     * @since 1.0
     */
    public static @NonNull JsonCatVariant convert(@NonNull CatVariant variant) {
        return new JsonCatVariant(KeyAdapter.convert(variant.assetInfo().id()));
    }
}