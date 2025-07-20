package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.JsonEquipmentSlotGroup;
import net.hypejet.jet.data.json.model.enchantment.JsonEnchantment;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents something converting {@linkplain Enchantment enchantments} to a Jet data equivalent.
 *
 * @since 1.0
 */
public final class EnchantmentAdapter {

    private EnchantmentAdapter() {}

    /**
     * Converts the specified {@linkplain Enchantment enchantment} to a Jet data equivalent.
     *
     * @param enchantment the enchantment to convert
     * @param registryAccess access to all Minecraft registries
     * @return the converted enchantment
     * @since 1.0
     */
    public static @NotNull JsonEnchantment convert(@NonNull Enchantment enchantment,
                                                   @NonNull RegistryAccess registryAccess) {
        Enchantment.EnchantmentDefinition definition = enchantment.definition();
        return new JsonEnchantment(
                ComponentAdapter.convert(enchantment.description()),
                HolderSetAdapter.convert(enchantment.exclusiveSet(), value -> convert(value, registryAccess)),
                HolderSetAdapter.convert(definition.supportedItems(), ItemAdapter::convert),
                definition.primaryItems()
                        .map(set -> HolderSetAdapter.convert(set, ItemAdapter::convert))
                        .orElse(null),
                definition.weight(),
                definition.maxLevel(),
                convertCost(definition.minCost()),
                convertCost(definition.maxCost()),
                definition.anvilCost(),
                convertSlots(definition.slots()),
                convertEffects(enchantment.effects(), registryAccess)
        );
    }

    private static @NonNull Map<Key, BinaryTagHolder> convertEffects(@NonNull DataComponentMap effects,
                                                                     @NonNull RegistryAccess registryAccess) {
        Map<Key, BinaryTagHolder> convertedEffects = new HashMap<>();
        effects.forEach(component -> putComponent(convertedEffects, component, registryAccess));
        return convertedEffects;
    }

    private static JsonEnchantment.@NonNull Cost convertCost(Enchantment.@NonNull Cost cost) {
        return new JsonEnchantment.Cost(cost.base(), cost.perLevelAboveFirst());
    }

    private static @NonNull List<JsonEquipmentSlotGroup> convertSlots(@NonNull List<EquipmentSlotGroup> slots) {
        List<JsonEquipmentSlotGroup> convertedSlots = new ArrayList<>();
        for (EquipmentSlotGroup slot : slots) {
            convertedSlots.add(switch (slot) {
                case ANY -> JsonEquipmentSlotGroup.ANY;
                case MAINHAND -> JsonEquipmentSlotGroup.MAIN_HAND;
                case OFFHAND -> JsonEquipmentSlotGroup.OFFHAND;
                case HAND -> JsonEquipmentSlotGroup.HAND;
                case FEET -> JsonEquipmentSlotGroup.FEET;
                case LEGS -> JsonEquipmentSlotGroup.LEGS;
                case CHEST -> JsonEquipmentSlotGroup.CHEST;
                case HEAD -> JsonEquipmentSlotGroup.HEAD;
                case ARMOR -> JsonEquipmentSlotGroup.ARMOR;
                case BODY -> JsonEquipmentSlotGroup.BODY;
                case SADDLE -> JsonEquipmentSlotGroup.SADDLE;
            });
        }
        return convertedSlots;
    }

    private static <T> void putComponent(@NonNull Map<Key, BinaryTagHolder> components,
                                         @NonNull TypedDataComponent<T> component,
                                         @NonNull RegistryAccess registryAccess) {
        DataComponentType<T> type = component.type();
        if (type.isTransient()) return;

        RegistryOps<Tag> registryOps = RegistryOps.create(NbtOps.INSTANCE, registryAccess);
        BinaryTagHolder serializedComponent = component.encodeValue(registryOps)
                .map(Tag::toString)
                .map(BinaryTagHolder::binaryTagHolder)
                .getOrThrow();

        ResourceLocation location = BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.getKey(type);
        components.put(KeyAdapter.convert(location), serializedComponent);
    }
}