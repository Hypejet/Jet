package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.variant.pig.JsonPigVariant;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain PigVariant pig variants} to a Jet data equivalent.
 *
 * @since 1.0
 * @see PigVariant
 */
public final class PigVariantAdapter {

    public PigVariantAdapter() {}

    /**
     * Converts the specified {@linkplain JsonPigVariant pig variant} to a Jet data equivalent.
     *
     * @param variant the pig variant to convert
     * @return the converted pig variant
     * @since 1.0
     */
    public static @NonNull JsonPigVariant convert(@NonNull PigVariant variant) {
        ModelAndTexture<PigVariant.ModelType> modelAndTexture = variant.modelAndTexture();
        return new JsonPigVariant(
                convertModelType(modelAndTexture.model()),
                KeyAdapter.convert(modelAndTexture.asset().id())
        );
    }

    private static JsonPigVariant.@NonNull ModelType convertModelType(PigVariant.@NonNull ModelType modelType) {
        return switch (modelType) {
            case NORMAL -> JsonPigVariant.ModelType.NORMAL;
            case COLD -> JsonPigVariant.ModelType.COLD;
        };
    }
}