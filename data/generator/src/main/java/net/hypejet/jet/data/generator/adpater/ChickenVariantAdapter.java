package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.variant.chicken.JsonChickenVariant;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain ChickenVariant chicken variants} to a Jet data equivalent.
 *
 * @since 1.0
 * @see ChickenVariant
 */
public final class ChickenVariantAdapter {

    private ChickenVariantAdapter() {}

    /**
     * Converts the specified {@linkplain ChickenVariant chicken variant} to a Jet data equivalent.
     *
     * @param variant the chicken variant to convert
     * @return the converted chicken variant
     * @since 1.0
     */
    public static @NonNull JsonChickenVariant convert(@NonNull ChickenVariant variant) {
        ModelAndTexture<ChickenVariant.ModelType> modelAndTexture = variant.modelAndTexture();
        return new JsonChickenVariant(
                convertModelType(modelAndTexture.model()),
                KeyAdapter.convert(modelAndTexture.asset().id())
        );
    }

    private static JsonChickenVariant.@NonNull ModelType convertModelType(
            ChickenVariant.@NonNull ModelType modelType
    ) {
        return switch (modelType) {
            case NORMAL -> JsonChickenVariant.ModelType.NORMAL;
            case COLD -> JsonChickenVariant.ModelType.COLD;
        };
    }
}