package net.hypejet.jet.server.registry.codecs.registry.damage;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.api.registry.registries.damage.DamageEffectType;
import net.hypejet.jet.data.model.api.registry.registries.damage.DamageScalingType;
import net.hypejet.jet.data.model.api.registry.registries.damage.DamageType;
import net.hypejet.jet.data.model.api.registry.registries.damage.DeathMessageType;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.mapper.MapperBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain DamageType damage type}.
 *
 * @since 1.0
 * @author Codestech
 * @see DamageType
 * @see BinaryTagCodec
 */
public final class DamageTypeBinaryTagCodec implements BinaryTagCodec<DamageType> {

    private static final DamageTypeBinaryTagCodec INSTANCE = new DamageTypeBinaryTagCodec();

    private static final String MESSAGE_ID_FIELD = "message_id";
    private static final String SCALING_FIELD = "scaling";
    private static final String EXHAUSTION_FIELD = "exhaustion";
    private static final String EFFECTS_FIELD = "effects";
    private static final String DEATH_MESSAGE_TYPE_FIELD = "death_message_type";
    
    private static final BinaryTagCodec<DamageScalingType> SCALING_TYPE_CODEC = MapperBinaryTagCodec.stringCodec(
            Mapper.builder(DamageScalingType.class, String.class)
                    .register(DamageScalingType.NEVER, "never")
                    .register(DamageScalingType.WHEN_CAUSED_BY_LIVING_NON_PLAYER, "when_caused_by_living_non_player")
                    .register(DamageScalingType.ALWAYS, "always")
                    .build()
    );
    
    private static final BinaryTagCodec<DamageEffectType> EFFECT_TYPE_CODEC = MapperBinaryTagCodec.stringCodec(
            Mapper.builder(DamageEffectType.class, String.class)
                    .register(DamageEffectType.HURT, "hurt")
                    .register(DamageEffectType.THORNS, "thorns")
                    .register(DamageEffectType.DROWNING, "drowning")
                    .register(DamageEffectType.BURNING, "burning")
                    .register(DamageEffectType.POKING, "poking")
                    .register(DamageEffectType.FREEZING, "freezing")
                    .build()
    );

    private static final BinaryTagCodec<DeathMessageType> MESSAGE_TYPE_CODEC = MapperBinaryTagCodec.stringCodec(
            Mapper.builder(DeathMessageType.class, String.class)
                    .register(DeathMessageType.DEFAULT, "default")
                    .register(DeathMessageType.FALL_VARIANTS, "fall_variants")
                    .register(DeathMessageType.INTENTIONAL_GAME_DESIGN, "intentional_game_design")
                    .build()
    );
    
    private DamageTypeBinaryTagCodec() {}
    
    @Override
    public @NonNull DamageType read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified is not a compound binary tag");
        return new DamageType(compound.getString(MESSAGE_ID_FIELD),
                BinaryTagUtil.read(SCALING_FIELD, compound, SCALING_TYPE_CODEC),
                compound.getFloat(EXHAUSTION_FIELD),
                BinaryTagUtil.readOptional(EFFECTS_FIELD, compound, EFFECT_TYPE_CODEC),
                BinaryTagUtil.readOptional(DEATH_MESSAGE_TYPE_FIELD, compound, MESSAGE_TYPE_CODEC));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull DamageType object) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(MESSAGE_ID_FIELD, object.messageId())
                .put(SCALING_FIELD, SCALING_TYPE_CODEC.write(object.damageScalingType()))
                .putFloat(EXHAUSTION_FIELD, object.exhaustion());

        BinaryTagUtil.writeOptional(EFFECTS_FIELD, object.damageEffectType(), builder, EFFECT_TYPE_CODEC);
        BinaryTagUtil.writeOptional(DEATH_MESSAGE_TYPE_FIELD, object.deathMessageType(), builder, MESSAGE_TYPE_CODEC);

        return builder.build();
    }

    /**
     * Gets an instance of the {@linkplain DamageTypeBinaryTagCodec damage type binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull DamageTypeBinaryTagCodec instance() {
        return INSTANCE;
    }
}