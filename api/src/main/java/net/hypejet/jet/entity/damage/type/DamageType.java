package net.hypejet.jet.entity.damage.type;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A type of Minecraft entity damage.
 *
 * @param messageId a death message translation key to be used when the damage type is using
 *                  a {@linkplain DeathMessageType#DEFAULT default death message type}
 * @param scalingType a type of difficulty-based damage scaling that damage using this damage type should use
 * @param exhaustion the amount of hunger exhaustion caused by the damage using this damage type
 * @param effects an effect that should be displayed during damage using this damage type
 * @param deathMessageType a kind of death message that damage using this damage type should use
 * @since 1.0
 */
public record DamageType(@NonNull String messageId, @NonNull DamageScalingType scalingType, float exhaustion,
                         @NonNull DamageEffects effects, @NonNull DeathMessageType deathMessageType) {
    /**
     * Constructs the {@linkplain DamageType damage type}.
     *
     * @param messageId a death message translation key to be used when the damage type is using
     *                  a {@linkplain DeathMessageType#DEFAULT default death message type}
     * @param scalingType a type of difficulty-based damage scaling that damage using this damage type should use
     * @param exhaustion the amount of hunger exhaustion caused by the damage using this damage type
     * @param effects an effect that should be displayed during damage using this damage type
     * @param deathMessageType a kind of death message that damage using this damage type should use
     * @since 1.0
     */
    public DamageType {
        Objects.requireNonNull(messageId, "message id");
        Objects.requireNonNull(scalingType, "scaling type");
        Objects.requireNonNull(effects, "effects");
        Objects.requireNonNull(deathMessageType, "death message type");
    }
}