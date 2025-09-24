package net.hypejet.jet.server.registry.codecs.inventory.item.enchantment;

import net.hypejet.jet.entity.equipment.EquipmentSlotGroup;
import net.hypejet.jet.inventory.item.Item;
import net.hypejet.jet.inventory.item.enchatment.Enchantment;
import net.hypejet.jet.registry.holder.HolderSet;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.ComponentBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.ListBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.HolderBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.HolderSetBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain Enchantment enchantments}.
 *
 * @since 1.0
 * @see Enchantment
 * @see BinaryTagCodec
 */
@NullMarked
public final class EnchantmentBinaryTagCodec implements BinaryTagCodec<Enchantment> {

    private static final String DESCRIPTION_FIELD = "description";
    private static final String SUPPORTED_ITEMS_FIELD = "supported_items";
    private static final String PRIMARY_ITEMS_FIELD = "primary_items";
    private static final String WEIGHT_FIELD = "weight";
    private static final String MAX_LEVEL_FIELD = "max_level";
    private static final String MIN_COST_FIELD = "min_cost";
    private static final String MAX_COST_FIELD = "max_cost";
    private static final String ANVIL_COST_FIELD = "anvil_cost";
    private static final String SLOTS_FIELD = "slots";
    private static final String EXCLUSIVE_SET_FIELD = "exclusive_set";
    private static final String EFFECTS_FIELD = "effects";

    private static final BinaryTagCodec<List<EquipmentSlotGroup>> SLOTS_CODEC = new ListBinaryTagCodec<>(
            new IndexBinaryTagCodec<>(
                    IndexUtil.fromMap(Map.ofEntries(
                            Map.entry("any", EquipmentSlotGroup.ANY),
                            Map.entry("mainhand", EquipmentSlotGroup.MAIN_HAND),
                            Map.entry("offhand", EquipmentSlotGroup.OFFHAND),
                            Map.entry("hand", EquipmentSlotGroup.HAND),
                            Map.entry("feet", EquipmentSlotGroup.FEET),
                            Map.entry("legs", EquipmentSlotGroup.LEGS),
                            Map.entry("chest", EquipmentSlotGroup.CHEST),
                            Map.entry("head", EquipmentSlotGroup.HEAD),
                            Map.entry("armor", EquipmentSlotGroup.ARMOR),
                            Map.entry("body", EquipmentSlotGroup.BODY),
                            Map.entry("saddle", EquipmentSlotGroup.SADDLE)
                    )),
                    StringBinaryTagCodec.INSTANCE
            )
    );

    private static final BinaryTagCodec<HolderSet<Item>> ITEM_HOLDER_SET_CODEC = new HolderSetBinaryTagCodec<>(
            new HolderBinaryTagCodec<>(null)
    );

    private static final BinaryTagCodec<HolderSet<Enchantment>> EXCLUSIVE_SET_CODEC = new HolderSetBinaryTagCodec<>(
            new HolderBinaryTagCodec<>(null)
    );

    /**
     * An instance of the {@linkplain EnchantmentBinaryTagCodec enchantment binary-tag codec}.
     *
     * @since 1.0
     */
    public static final EnchantmentBinaryTagCodec INSTANCE = new EnchantmentBinaryTagCodec();

    private EnchantmentBinaryTagCodec() {}

    @Override
    public Enchantment decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            BinaryTag primaryItemsTag = compound.get(PRIMARY_ITEMS_FIELD);
            BinaryTag exclusiveSetTag = compound.get(EXCLUSIVE_SET_FIELD);
            BinaryTag effectsTag = compound.get(EFFECTS_FIELD);
            return new Enchantment(
                    ComponentBinaryTagCodec.INSTANCE.decode(requiredTag(DESCRIPTION_FIELD, compound), server),
                    new Enchantment.Definition(
                            ITEM_HOLDER_SET_CODEC.decode(requiredTag(SUPPORTED_ITEMS_FIELD, compound), server),
                            primaryItemsTag == null ? null : ITEM_HOLDER_SET_CODEC.decode(primaryItemsTag, server),
                            requiredTag(WEIGHT_FIELD, compound, BinaryTagTypes.INT).value(),
                            requiredTag(MAX_LEVEL_FIELD, compound, BinaryTagTypes.INT).value(),
                            CostBinaryTagCodec.INSTANCE.decode(requiredTag(MIN_COST_FIELD, compound), server),
                            CostBinaryTagCodec.INSTANCE.decode(requiredTag(MAX_COST_FIELD, compound), server),
                            requiredTag(ANVIL_COST_FIELD, compound, BinaryTagTypes.INT).value(),
                            SLOTS_CODEC.decode(requiredTag(SLOTS_FIELD, compound), server)
                    ),
                    exclusiveSetTag == null
                            ? new HolderSet.Direct<>(List.of())
                            : EXCLUSIVE_SET_CODEC.decode(exclusiveSetTag, server),
                    effectsTag == null
                            ? CompoundBinaryTag.empty() // FIXME: Temporal solution
                            : effectsTag
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to an enchantment"
            );
        }
    }

    @Override
    public BinaryTag encode(Enchantment value, JetMinecraftServer server) {
        Enchantment.Definition definition = value.definition();
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(value.description(), server))
                .put(SUPPORTED_ITEMS_FIELD, ITEM_HOLDER_SET_CODEC.encode(definition.supportedItems(), server))
                .putInt(WEIGHT_FIELD, definition.weight())
                .putInt(MAX_LEVEL_FIELD, definition.maxLevel())
                .put(MIN_COST_FIELD, CostBinaryTagCodec.INSTANCE.encode(definition.minCost(), server))
                .put(MAX_COST_FIELD, CostBinaryTagCodec.INSTANCE.encode(definition.maxCost(), server))
                .putInt(ANVIL_COST_FIELD, definition.anvilCost())
                .put(SLOTS_FIELD, SLOTS_CODEC.encode(definition.slots(), server));

        HolderSet<Item> primaryItems = definition.primaryItems();
        if (primaryItems != null) {
            builder.put(PRIMARY_ITEMS_FIELD, ITEM_HOLDER_SET_CODEC.encode(primaryItems, server));
        }

        HolderSet<Enchantment> exclusiveSet = value.exclusiveSet();
        if (!(exclusiveSet instanceof HolderSet.Direct<?>(List<?> contents) && contents.isEmpty())) {
            builder.put(EXCLUSIVE_SET_FIELD, EXCLUSIVE_SET_CODEC.encode(exclusiveSet, server));
        }

        // TODO: Use data component map ; check whether the data component map is empty
        builder.put(EFFECTS_FIELD, value.effects());

        return builder.build();
    }

    /**
     * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain Enchantment.Cost enchantment costs}.
     *
     * @since 1.0
     * @see Enchantment.Cost
     * @see BinaryTagCodec
     */
    private static final class CostBinaryTagCodec implements BinaryTagCodec<Enchantment.Cost> {

        private static final String BASE_FIELD = "base";
        private static final String PER_LEVEL_ABOVE_FIRST_FIELD = "per_level_above_first";

        /**
         * An instance of the {@linkplain CostBinaryTagCodec cost binary-tag codec}.
         *
         * @since 1.0
         */
        private static final CostBinaryTagCodec INSTANCE = new CostBinaryTagCodec();

        private CostBinaryTagCodec() {}

        @Override
        public Enchantment.Cost decode(BinaryTag binaryTag, JetMinecraftServer server) {
            if (binaryTag instanceof CompoundBinaryTag compound) {
                return new Enchantment.Cost(
                        requiredTag(BASE_FIELD, compound, BinaryTagTypes.INT).value(),
                        requiredTag(PER_LEVEL_ABOVE_FIRST_FIELD, compound, BinaryTagTypes.INT).value()
                );
            } else {
                throw new IllegalArgumentException(
                        "The encoded tag must be of compound type to decode it to an enchantment cost"
                );
            }
        }

        @Override
        public BinaryTag encode(Enchantment.Cost value, JetMinecraftServer server) {
            return CompoundBinaryTag.builder()
                    .putInt(BASE_FIELD, value.base())
                    .putInt(PER_LEVEL_ABOVE_FIRST_FIELD, value.perLevelAboveFirst())
                    .build();
        }
    }
}