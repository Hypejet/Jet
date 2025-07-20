package net.hypejet.jet.data.json.model.enchantment;

import net.hypejet.jet.data.json.model.JsonEquipmentSlotGroup;
import net.hypejet.jet.data.json.model.JsonHolderSet;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.util.RangeUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A Minecraft enchantment.
 *
 * @param description a component that is used to display the enchantment on items
 * @param exclusiveSet a set of enchantment holders incompatible with this enchantment
 * @param supportedItems items that this enchantment can be applied to using an anvil
 * @param primaryItems items for which this enchantment appears in an enchanting table, {@code null} if supported
 *                     items should be used instead
 * @param weight the probability of this enchantment when enchanting
 * @param maxLevel the maximum level of this enchantment
 * @param minCost the minimum base cost of this enchantment in levels
 * @param maxCost the maximum base cost of this enchantment in levels
 * @param anvilCost the base cost when applying this enchantment to another item using an anvil
 * @param slots a list of equipment slots that this enchantment works on
 * @param effects a map associating effect keys with serialized effects of this enchantment
 * @since 1.0
 */
public record JsonEnchantment(@NonNull Component description,
                              @NonNull JsonHolderSet<JsonEnchantment> exclusiveSet,
                              @NonNull JsonHolderSet<JsonItem> supportedItems,
                              @Nullable JsonHolderSet<JsonItem> primaryItems,
                              int weight, int maxLevel, @NonNull Cost minCost,
                              @NonNull Cost maxCost, int anvilCost,
                              @NonNull List<JsonEquipmentSlotGroup> slots,
                              @NonNull Map<Key, BinaryTagHolder> effects) {
    /**
     * Constructs the {@linkplain JsonEnchantment enchantment}.
     *
     * @param description a component that is used to display the enchantment on items
     * @param exclusiveSet a set of enchantment holders incompatible with this enchantment
     * @param supportedItems items that this enchantment can be applied to using an anvil
     * @param primaryItems items for which this enchantment appears in an enchanting table, {@code null} if supported
     *                     items should be used instead
     * @param weight the probability of this enchantment when enchanting
     * @param maxLevel the maximum level of this enchantment
     * @param minCost the minimum base cost of this enchantment in levels
     * @param maxCost the maximum base cost of this enchantment in levels
     * @param anvilCost the base cost when applying this enchantment to another item using an anvil
     * @param slots a list of equipment slots that this enchantment works on
     * @param effects a map associating effect keys with serialized effects of this enchantment
     * @since 1.0
     */
    public JsonEnchantment {
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(exclusiveSet, "exclusive set");
        Objects.requireNonNull(supportedItems, "supported items");
        Objects.requireNonNull(minCost, "min cost");
        Objects.requireNonNull(maxCost, "max cost");
        slots = List.copyOf(Objects.requireNonNull(slots, "slots"));
        effects = Map.copyOf(Objects.requireNonNull(effects, "effects"));
        RangeUtil.ensureInRange(1, 1024, weight);
        RangeUtil.ensureInRange(1, 255, maxLevel);
        RangeUtil.ensureNotNegative(anvilCost);
    }

    /**
     * Represents level cost of an {@linkplain JsonEnchantment enchantment}.
     *
     * @param base the cost for a first enchantment level
     * @param perLevelAboveFirst the cost for per enchantment level after the first level
     * @since 1.0
     * @see JsonEnchantment
     */
    public record Cost(int base, int perLevelAboveFirst) {}
}