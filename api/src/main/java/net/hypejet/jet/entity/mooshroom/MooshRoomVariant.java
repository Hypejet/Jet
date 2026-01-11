package net.hypejet.jet.entity.mooshroom;

import org.jspecify.annotations.NonNull;

import net.hypejet.jet.entity.Entity;

/**
 * A variant of an mooshroom {@linkplain Entity entity}.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Entity
 */
public final class MooshRoomVariant {

    /**
     * A red variant.
     *
     * @since 1.0
     */
    public static final MooshRoomVariant RED = new MooshRoomVariant("red");

    /**
     * A brown variant.
     *
     * @since 1.0
     */
    public static final MooshRoomVariant BROWN = new MooshRoomVariant("brown");

    private final String name;

    private MooshRoomVariant(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AxolotlVariant{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
