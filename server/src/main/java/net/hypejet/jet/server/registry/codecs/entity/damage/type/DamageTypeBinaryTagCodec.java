package net.hypejet.jet.server.registry.codecs.entity.damage.type;

import net.hypejet.jet.entity.damage.type.DamageEffects;
import net.hypejet.jet.entity.damage.type.DamageScalingType;
import net.hypejet.jet.entity.damage.type.DamageType;
import net.hypejet.jet.entity.damage.type.DeathMessageType;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain DamageType damage types}.
 *
 * @since 1.0
 * @see DamageType
 * @see BinaryTagCodec
 */
public final class DamageTypeBinaryTagCodec implements BinaryTagCodec<DamageType> {

    private static final String MESSAGE_ID_FIELD = "message_id";
    private static final String SCALING_TYPE_FIELD = "scaling";
    private static final String EXHAUSTION_FIELD = "exhaustion";
    private static final String EFFECTS_FIELD = "effects";
    private static final String DEATH_MESSAGE_TYPE_FIELD = "death_message_field";

    private static final BinaryTagCodec<DamageScalingType> SCALING_TYPE_CODEC = new IndexBinaryTagCodec<>(
            IndexUtil.fromMap(Map.of(
                    "never", DamageScalingType.NEVER,
                    "when_caused_by_living_non_player", DamageScalingType.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                    "always", DamageScalingType.ALWAYS
            )),
            StringBinaryTagCodec.INSTANCE
    );

    private static final BinaryTagCodec<DamageEffects> DAMAGE_EFFECTS_CODEC = new IndexBinaryTagCodec<>(
            IndexUtil.fromMap(Map.of(
                    "hurt", DamageEffects.HURT,
                    "thorns", DamageEffects.THORNS,
                    "drowning", DamageEffects.DROWNING,
                    "burning", DamageEffects.BURNING,
                    "poking", DamageEffects.POKING,
                    "freezing", DamageEffects.FREEZING
            )),
            StringBinaryTagCodec.INSTANCE
    );

    private static final BinaryTagCodec<DeathMessageType> DEATH_MESSAGE_TYPE_CODEC = new IndexBinaryTagCodec<>(
            IndexUtil.fromMap(Map.of(
                    "default", DeathMessageType.DEFAULT,
                    "fall_variants", DeathMessageType.FALL_VARIANTS,
                    "intentional_game_design", DeathMessageType.INTENTIONAL_GAME_DESIGN
            )),
            StringBinaryTagCodec.INSTANCE
    );

    /**
     * An instance of the {@linkplain DamageTypeBinaryTagCodec damage type binary-tag codec}.
     *
     * @since 1.0
     */
    public static final DamageTypeBinaryTagCodec INSTANCE = new DamageTypeBinaryTagCodec();

    private DamageTypeBinaryTagCodec() {}

    @Override
    public @NotNull DamageType decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            BinaryTag effectsTag = compound.get(EFFECTS_FIELD);
            BinaryTag deathMessageTypeTag = compound.get(DEATH_MESSAGE_TYPE_FIELD);
            return new DamageType(
                    requiredTag(MESSAGE_ID_FIELD, compound, BinaryTagTypes.STRING).value(),
                    SCALING_TYPE_CODEC.decode(requiredTag(SCALING_TYPE_FIELD, compound)),
                    requiredTag(EXHAUSTION_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                    effectsTag == null
                            ? DamageEffects.HURT
                            : DAMAGE_EFFECTS_CODEC.decode(effectsTag),
                    deathMessageTypeTag == null
                            ? DeathMessageType.DEFAULT
                            : DEATH_MESSAGE_TYPE_CODEC.decode(deathMessageTypeTag)
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a damage type"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull DamageType decoded) throws Exception {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(MESSAGE_ID_FIELD, decoded.messageId())
                .put(SCALING_TYPE_FIELD, SCALING_TYPE_CODEC.encode(decoded.scalingType()))
                .putFloat(EXHAUSTION_FIELD, decoded.exhaustion());

        DamageEffects effects = decoded.effects();
        if (effects != DamageEffects.HURT) {
            builder.put(EFFECTS_FIELD, DAMAGE_EFFECTS_CODEC.encode(effects));
        }

        DeathMessageType deathMessageType = decoded.deathMessageType();
        if (deathMessageType != DeathMessageType.DEFAULT) {
            builder.put(DEATH_MESSAGE_TYPE_FIELD, DEATH_MESSAGE_TYPE_CODEC.encode(deathMessageType));
        }

        return builder.build();
    }
}