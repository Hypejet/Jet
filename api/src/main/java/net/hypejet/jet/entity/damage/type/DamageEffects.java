package net.hypejet.jet.entity.damage.type;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An effect displayed to players witnessing damage.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 */
public final class DamageEffects {
    /**
     * An effect playing a default hurt sound.
     *
     * @since 1/0
     */
    public static final DamageEffects HURT = new DamageEffects("hurt");

    /**
     * An effect playing a thorns hurt sound.
     *
     * @since 1.0
     */
    public static final DamageEffects THORNS = new DamageEffects("thorns");

    /**
     * An effect playing a drowning hurt sound.
     *
     * @since 1.0
     */
    public static final DamageEffects DROWNING = new DamageEffects("drowning");

    /**
     * An effect playing a single tick of burning sound.
     *
     * @since 1.0
     */
    public static final DamageEffects BURNING = new DamageEffects("burning");

    /**
     * An effect playing a poking hurt sound.
     *
     * @since 1.0
     */
    public static final DamageEffects POKING = new DamageEffects("poking");

    /**
     * An effect playing a freezing tick sound.
     *
     * @since 1.0
     */
    public static final DamageEffects FREEZING = new DamageEffects("freezing");

    private final String name;

    private DamageEffects(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "DamageEffects{" +
                "name='" + this.name + '\'' +
                '}';
    }
}