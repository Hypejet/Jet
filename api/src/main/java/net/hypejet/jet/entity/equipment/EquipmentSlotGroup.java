package net.hypejet.jet.entity.equipment;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A group of Minecraft equipment slots.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 */
public final class EquipmentSlotGroup {
    /**
     * A group of all equipment slots.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup ANY = new EquipmentSlotGroup("any");

    /**
     * A group of the main hand equipment slot only.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup MAIN_HAND = new EquipmentSlotGroup("main_hand");

    /**
     * A group of the offhand equipment slot only.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup OFFHAND = new EquipmentSlotGroup("offhand");

    /**
     * A group of main hand and offhand equipment slots.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup HAND = new EquipmentSlotGroup("hand");

    /**
     * A group of the feet equipment slot only.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup FEET = new EquipmentSlotGroup("feet");

    /**
     * A group of the legs equipment slot only.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup LEGS = new EquipmentSlotGroup("legs");

    /**
     * A group of the chest equipment slot only.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup CHEST = new EquipmentSlotGroup("chest");

    /**
     * A group of the head equipment slot only.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup HEAD = new EquipmentSlotGroup("head");

    /**
     * A group of all equipment slots that can accept any kind of armor item.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup ARMOR = new EquipmentSlotGroup("armor");

    /**
     * A group of the animal body equipment slot only.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup BODY = new EquipmentSlotGroup("body");

    /**
     * A group of the saddle equipment slot only.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroup SADDLE = new EquipmentSlotGroup("saddle");

    private final String name;

    private EquipmentSlotGroup(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "EquipmentSlotGroup{" +
                "name='" + this.name + '\'' +
                '}';
    }
}