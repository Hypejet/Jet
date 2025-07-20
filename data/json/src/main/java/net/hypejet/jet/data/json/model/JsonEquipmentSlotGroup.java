package net.hypejet.jet.data.json.model;

/**
 * A group of Minecraft equipment slots.
 *
 * @since 1.0
 */
public enum JsonEquipmentSlotGroup {
    /**
     * A group of all equipment slots.
     *
     * @since 1.0
     */
    ANY,
    /**
     * A group of the main hand equipment slot only.
     *
     * @since 1.0
     */
    MAIN_HAND,
    /**
     * A group of the offhand equipment slot only.
     *
     * @since 1.0
     */
    OFFHAND,
    /**
     * A group of main hand and offhand equipment slots.
     *
     * @since 1.0
     */
    HAND,
    /**
     * A group of the feet equipment slot only.
     *
     * @since 1.0
     */
    FEET,
    /**
     * A group of the legs equipment slot only.
     *
     * @since 1.0
     */
    LEGS,
    /**
     * A group of the chest equipment slot only.
     *
     * @since 1.0
     */
    CHEST,
    /**
     * A group of the head equipment slot only.
     *
     * @since 1.0
     */
    HEAD,
    /**
     * A group of all equipment slots that can accept any kind of armor item.
     *
     * @since 1.0
     */
    ARMOR,
    /**
     * A group of the animal body equipment slot only.
     *
     * @since 1.0
     */
    BODY,
    /**
     * A group of the saddle equipment slot only.
     *
     * @since 1.0
     */
    SADDLE
}