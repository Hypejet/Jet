package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.variant.frog.JsonFrogVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain FrogVariant frog variants} to a Jet data equivalent.
 *
 * @since 1.0
 * @see FrogVariant
 */
public final class FrogVariantAdapter {

    private FrogVariantAdapter() {}

    /**
     * Converts the specified {@linkplain FrogVariant frog variant} to a Jet data equivalent.
     *
     * @param variant the frog variant to convert
     * @return the converted frog variant
     * @since 1.0
     */
    public static @NonNull JsonFrogVariant convert(@NonNull FrogVariant variant) {
        return new JsonFrogVariant(KeyAdapter.convert(variant.assetInfo().id()));
    }
}