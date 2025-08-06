package net.hypejet.jet.server.inventory.item;

import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.inventory.item.Item;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * An implementation of an {@linkplain Item item}.
 *
 * @param requiredFeatureFlags a set of feature flag keys required to enable this item
 * @since 1.0
 * @see Item
 */
public record JetItem(@NonNull Set<Key> requiredFeatureFlags) implements Item {
    /**
     * Constructs the {@linkplain JetItem item implementation}.
     *
     * @param requiredFeatureFlags a set of feature flag keys that should be required to enable the constructed item
     * @since 1.0
     */
    public JetItem {
        requiredFeatureFlags = Set.copyOf(Objects.requireNonNull(requiredFeatureFlags, "required feature flags"));
    }

    /**
     * Converts the specified {@linkplain JsonItem Jet data item} to a Jet equivalent.
     *
     * @param item the item to convert
     * @return the converted item
     * @since 1.0
     */
    public static @NonNull JetItem convert(@NonNull JsonItem item) {
        return new JetItem(item.requiredFeatureFlags());
    }
}