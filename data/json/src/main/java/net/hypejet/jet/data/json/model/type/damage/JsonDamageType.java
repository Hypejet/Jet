package net.hypejet.jet.data.json.model.type.damage;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A type of Minecraft entity damage.
 *
 * @param messageId a death message translation key to be used when the damage type is using
 *                  a {@linkplain JsonDeathMessageType#DEFAULT default death message type}
 * @param scalingType a type of difficulty-based damage scaling that damage using this damage type should use
 * @param exhaustion the amount of hunger exhaustion caused by the damage using this damage type
 * @param effects an effect that should be displayed during damage using this damage type
 * @param messageType a kind of death message that damage using this damage type should use
 * @since 1.0
 */
public record JsonDamageType(@NonNull String messageId, @NonNull JsonDamageScalingType scalingType, float exhaustion,
                             @NonNull JsonDamageEffects effects, @NonNull JsonDeathMessageType messageType) {
    /**
     * Constructs the {@linkplain JsonDamageType damage type}.
     *
     * @param messageId a death message translation key to be used when the damage type is using
     *                  a {@linkplain JsonDeathMessageType#DEFAULT default death message type}
     * @param scalingType a type of difficulty-based damage scaling that damage using this damage type should use
     * @param exhaustion the amount of hunger exhaustion caused by the damage using this damage type
     * @param effects an effect that should be displayed during damage using this damage type
     * @param messageType a kind of death message that damage using this damage type should use
     * @since 1.0
     */
    public JsonDamageType {
        Objects.requireNonNull(messageId, "message id");
        Objects.requireNonNull(scalingType, "scaling type");
        Objects.requireNonNull(effects, "effects");
        Objects.requireNonNull(messageType, "message type");
    }
}