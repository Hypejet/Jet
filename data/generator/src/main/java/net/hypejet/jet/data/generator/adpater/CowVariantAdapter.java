package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.variant.cow.JsonCowVariant;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain CowVariant cow variants} to a Jet data equivalent.
 *
 * @since 1.0
 * @see CowVariant
 */
public final class CowVariantAdapter {

    private CowVariantAdapter() {}

    /**
     * Converts the specified {@linkplain CowVariant cow variant} to a Jet data equivalent.
     *
     * @param variant the cow variant to convert
     * @return the converted cow variant
     * @since 1.0
     */
    public static @NonNull JsonCowVariant convert(@NonNull CowVariant variant) {
        ModelAndTexture<CowVariant.ModelType> modelAndTexture = variant.modelAndTexture();
        return new JsonCowVariant(
                convertModelType(modelAndTexture.model()),
                KeyAdapter.convert(modelAndTexture.asset().id())
        );
    }

    private static JsonCowVariant.@NonNull ModelType convertModelType(CowVariant.@NonNull ModelType modelType) {
        return switch (modelType) {
            case NORMAL -> JsonCowVariant.ModelType.NORMAL;
            case COLD -> JsonCowVariant.ModelType.COLD;
            case WARM -> JsonCowVariant.ModelType.WARM;
        };
    }
}