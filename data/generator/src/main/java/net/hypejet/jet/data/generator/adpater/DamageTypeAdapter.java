package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.type.damage.JsonDamageEffects;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageScalingType;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageType;
import net.hypejet.jet.data.json.model.type.damage.JsonDeathMessageType;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain DamageType damage types} top a Jet data equivalent.
 *
 * @since 1.0
 * @see DamageType
 */
public final class DamageTypeAdapter {

    private DamageTypeAdapter() {}

    /**
     * Converts the specified {@linkplain DamageType damage type} to a Jet data equivalent.
     *
     * @param damageType the damage type to convert
     * @return the converted damage type
     * @since 1.0
     */
    public static @NonNull JsonDamageType convert(@NonNull DamageType damageType) {
        return new JsonDamageType(
                damageType.msgId(),
                convertScalingType(damageType.scaling()),
                damageType.exhaustion(),
                convertDamageEffects(damageType.effects()),
                convertMessageType(damageType.deathMessageType())
        );
    }

    private static @NonNull JsonDamageScalingType convertScalingType(@NonNull DamageScaling scaling) {
        return switch (scaling) {
            case NEVER -> JsonDamageScalingType.NEVER;
            case WHEN_CAUSED_BY_LIVING_NON_PLAYER -> JsonDamageScalingType.WHEN_CAUSED_BY_LIVING_NON_PLAYER;
            case ALWAYS -> JsonDamageScalingType.ALWAYS;
        };
    }

    private static @NonNull JsonDamageEffects convertDamageEffects(@NonNull DamageEffects effects) {
        return switch (effects) {
            case HURT -> JsonDamageEffects.HURT;
            case THORNS -> JsonDamageEffects.THORNS;
            case DROWNING -> JsonDamageEffects.DROWNING;
            case BURNING -> JsonDamageEffects.BURNING;
            case POKING -> JsonDamageEffects.POKING;
            case FREEZING -> JsonDamageEffects.FREEZING;
        };
    }

    private static @NonNull JsonDeathMessageType convertMessageType(@NonNull DeathMessageType messageType) {
        return switch (messageType) {
            case DEFAULT -> JsonDeathMessageType.DEFAULT;
            case FALL_VARIANTS -> JsonDeathMessageType.FALL_VARIANTS;
            case INTENTIONAL_GAME_DESIGN -> JsonDeathMessageType.INTENTIONAL_GAME_DESIGN;
        };
    }
}