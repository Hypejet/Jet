package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.variant.painting.JsonPaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain PaintingVariant painting variants} to a Jet data equivalent.
 *
 * @since 1.0
 * @see PaintingVariant
 */
public final class PaintingVariantAdapter {

    private PaintingVariantAdapter() {}

    /**
     * Converts the specified {@linkplain PaintingVariant painting variant} to a Jet data equivalent.
     *
     * @param variant the painting variant to convert
     * @return the converted painting variant
     * @since 1.0
     */
    public static @NonNull JsonPaintingVariant convert(@NonNull PaintingVariant variant) {
        return new JsonPaintingVariant(
                variant.width(),
                variant.height(),
                KeyAdapter.convert(variant.assetId()),
                variant.title().map(ComponentAdapter::convert).orElse(null),
                variant.author().map(ComponentAdapter::convert).orElse(null)
        );
    }
}