package net.hypejet.jet.server.registry.writers.registry.damage;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.api.registries.damage.DamageEffectType;
import net.hypejet.jet.data.model.api.registries.damage.DamageScalingType;
import net.hypejet.jet.data.model.api.registries.damage.DamageType;
import net.hypejet.jet.data.model.api.registries.damage.DeathMessageType;
import net.hypejet.jet.server.registry.writers.mapper.MapperBinaryTagWriter;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain DamageType a damage type} into
 * {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see DamageType
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class DamageTypeBinaryTagWriter implements Writer<DamageType, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain DamageTypeBinaryTagWriter damage type binary tag writer}.
     *
     * @since 1.0
     */
    public static final DamageTypeBinaryTagWriter INSTANCE = new DamageTypeBinaryTagWriter();

    private static final String MESSAGE_ID_FIELD = "message_id";
    private static final String SCALING_FIELD = "scaling";
    private static final String EXHAUSTION_FIELD = "exhaustion";
    private static final String EFFECTS_FIELD = "effects";
    private static final String DEATH_MESSAGE_TYPE_FIELD = "death_message_type";
    
    private static final Writer<DamageScalingType, StringBinaryTag> SCALING_TYPE_CODEC =
            MapperBinaryTagWriter.stringCodec(Mapper.builder(DamageScalingType.class, String.class)
                    .register(DamageScalingType.NEVER, "never")
                    .register(DamageScalingType.WHEN_CAUSED_BY_LIVING_NON_PLAYER, "when_caused_by_living_non_player")
                    .register(DamageScalingType.ALWAYS, "always")
                    .build());
    
    private static final Writer<DamageEffectType, StringBinaryTag> EFFECT_TYPE_CODEC =
            MapperBinaryTagWriter.stringCodec(Mapper.builder(DamageEffectType.class, String.class)
                    .register(DamageEffectType.HURT, "hurt")
                    .register(DamageEffectType.THORNS, "thorns")
                    .register(DamageEffectType.DROWNING, "drowning")
                    .register(DamageEffectType.BURNING, "burning")
                    .register(DamageEffectType.POKING, "poking")
                    .register(DamageEffectType.FREEZING, "freezing")
                    .build());

    private static final Writer<DeathMessageType, StringBinaryTag> MESSAGE_TYPE_CODEC =
            MapperBinaryTagWriter.stringCodec(Mapper.builder(DeathMessageType.class, String.class)
                    .register(DeathMessageType.DEFAULT, "default")
                    .register(DeathMessageType.FALL_VARIANTS, "fall_variants")
                    .register(DeathMessageType.INTENTIONAL_GAME_DESIGN, "intentional_game_design")
                    .build());
    
    private DamageTypeBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull DamageType object) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(MESSAGE_ID_FIELD, object.messageId())
                .put(SCALING_FIELD, SCALING_TYPE_CODEC.write(object.damageScalingType()))
                .putFloat(EXHAUSTION_FIELD, object.exhaustion());

        BinaryTagUtil.writeOptional(EFFECTS_FIELD, object.damageEffectType(), builder, EFFECT_TYPE_CODEC);
        BinaryTagUtil.writeOptional(DEATH_MESSAGE_TYPE_FIELD, object.deathMessageType(), builder, MESSAGE_TYPE_CODEC);

        return builder.build();
    }
}