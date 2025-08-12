package net.hypejet.jet.entity.damage.type;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A difficulty scaling type of {@linkplain DamageType damage type}.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see DamageType
 */
public final class DamageScalingType {
    /**
     * A scaling type making the damage always the same, regardless of the difficulty.
     *
     * @since 1.0
     */
    public static final DamageScalingType NEVER = new DamageScalingType("never");

    /**
     * A scaling type scaling the damage only if an attacker is not a player, but is a living entity.
     *
     * @since 1.0
     */
    public static final DamageScalingType WHEN_CAUSED_BY_LIVING_NON_PLAYER = new DamageScalingType(
            "when_caused_by_living_non_player"
    );

    /**
     * A scaling type always scaling damage with the difficulty.
     *
     * @since 1.0
     */
    public static final DamageScalingType ALWAYS = new DamageScalingType("always");

    private final String name;

    private DamageScalingType(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "DamageScaling{" +
                "name='" + this.name + '\'' +
                '}';
    }
}