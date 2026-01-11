package net.hypejet.jet.server.inventory.item;

import net.hypejet.jet.inventory.item.Item;
import net.hypejet.jet.inventory.item.ItemStack;
import net.hypejet.jet.registry.holder.Holder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.serializer.nbt.NBTDataComponentValue;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;

/**
 * A specification of an {@linkplain Item item} to be used in inventory.
 *
 * @param item the item that this item stack represents
 * @param count the quantity of this item stack
 * @param components additional components of the item
 * @since 1.0
 * @see Item
 */
// TODO: Reference inventory in javadocs when the inventory system is implemented
// TODO: Replace NBT data component values with high-level data component values
@NullMarked
public record JetItemStack(Holder.Reference<Item> item, int count, Map<Key, NBTDataComponentValue> components)
        implements ItemStack {
    /**
     * Constructs the {@linkplain JetItemStack item stack}.
     *
     * @param item the item that the constructed item stack should represent
     * @param count the quantity that the item stack should have
     * @param components additional components that the item stack should have
     * @since 1.0
     */
    public JetItemStack {
        Objects.requireNonNull(item, "item");
        components = Map.copyOf(Objects.requireNonNull(components, "components"));
    }
}