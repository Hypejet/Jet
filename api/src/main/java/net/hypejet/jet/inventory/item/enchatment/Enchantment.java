package net.hypejet.jet.inventory.item.enchatment;

import net.hypejet.jet.entity.equipment.EquipmentSlotGroup;
import net.hypejet.jet.inventory.item.Item;
import net.hypejet.jet.registry.HolderSet;
import net.hypejet.jet.util.range.RangeUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;


/**
 * A Minecraft enchantment.
 *
 * @param description a component that is used to display the enchantment on items
 * @param definition a definition of the enchantment
 * @param exclusiveSet a set of enchantment holders incompatible with this enchantment
 * @param effects a map associating effect keys with serialized effects of this enchantment
 * @since 1.0
 */
// TODO: Implement data component map and replace the type of "effects" field with it
public record Enchantment(@NonNull Component description, @NonNull Definition definition,
                          @NonNull HolderSet<Enchantment> exclusiveSet, @NonNull BinaryTag effects) {
    /**
     * Constructs the {@linkplain Enchantment enchantment}.
     *
     * @param description a component that is used to display the enchantment on items
     * @param definition a definition of the enchantment
     * @param exclusiveSet a set of enchantment holders incompatible with this enchantment
     * @param effects a map associating effect keys with serialized effects of this enchantment
     * @since 1.0
     */
    public Enchantment {
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(definition, "definition");
        Objects.requireNonNull(exclusiveSet, "exclusive set");
        Objects.requireNonNull(effects, "effects");
    }

    /**
     * A definition of an {@linkplain Enchantment enchantment}.
     *
     * @param supportedItems items that this enchantment can be applied to using an anvil
     * @param primaryItems items for which this enchantment appears in an enchanting table, {@code null} if supported
     *                     items should be used instead
     * @param weight the probability of this enchantment when enchanting
     * @param maxLevel the maximum level of this enchantment
     * @param minCost the minimum base cost of this enchantment in levels
     * @param maxCost the maximum base cost of this enchantment in levels
     * @param anvilCost the base cost when applying this enchantment to another item using an anvil
     * @param slots a list of equipment slots that this enchantment works on
     * @since 1.0
     * @see Enchantment
     */
    public record Definition(@NonNull HolderSet<Item> supportedItems, @Nullable HolderSet<Item> primaryItems,
                             int weight, int maxLevel, @NonNull Cost minCost, @NonNull Cost maxCost, int anvilCost,
                             @NonNull List<EquipmentSlotGroup> slots) {
        /**
         * Constructs the {@linkplain Definition definition}.
         *
         * @param supportedItems items that this enchantment can be applied to using an anvil
         * @param primaryItems items for which this enchantment appears in an enchanting table, {@code null} if supported
         *                     items should be used instead
         * @param weight the probability of this enchantment when enchanting
         * @param maxLevel the maximum level of this enchantment
         * @param minCost the minimum base cost of this enchantment in levels
         * @param maxCost the maximum base cost of this enchantment in levels
         * @param anvilCost the base cost when applying this enchantment to another item using an anvil
         * @param slots a list of equipment slots that this enchantment works on
         * @since 1.0
         */
        public Definition {
            Objects.requireNonNull(supportedItems, "supported items");
            Objects.requireNonNull(minCost, "min cost");
            Objects.requireNonNull(maxCost, "max cost");
            slots = List.copyOf(Objects.requireNonNull(slots, "slots"));
            RangeUtil.ensureInRange(1, 1024, weight);
            RangeUtil.ensureInRange(1, 255, maxLevel);
            RangeUtil.ensureNotNegative(anvilCost);
        }

    }
    /**
     * Level cost of an {@linkplain Enchantment enchantment}.
     *
     * @param base the cost for a first enchantment level
     * @param perLevelAboveFirst the cost for each enchantment level after the first level
     * @since 1.0
     * @see Enchantment
     */
    public record Cost(int base, int perLevelAboveFirst) {}
}