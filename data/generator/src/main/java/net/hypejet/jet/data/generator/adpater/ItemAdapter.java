package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.item.JsonItem;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain Item items} to a Jet data equivalent.
 *
 * @since 1.0
 * @see Item
 */
public final class ItemAdapter {

    private ItemAdapter() {}

    /**
     * Converts the specified {@linkplain Item item} to a Jet data equivalent.
     *
     * @param item the item to convert
     * @return the converted item
     * @since 1.0
     */
    public static @NonNull JsonItem convert(@NonNull Item item) {
        return new JsonItem(KeyAdapter.convertSet(FeatureFlags.REGISTRY.toNames(item.requiredFeatures())));
    }
}