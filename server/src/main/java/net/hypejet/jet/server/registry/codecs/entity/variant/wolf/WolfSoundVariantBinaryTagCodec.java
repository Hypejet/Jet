package net.hypejet.jet.server.registry.codecs.entity.variant.wolf;

import net.hypejet.jet.entity.variant.wolf.WolfSoundVariant;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.sound.SoundEventBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain WolfSoundVariant wolf sound variants}.
 *
 * @since 1.0
 * @see WolfSoundVariant
 * @see BinaryTagCodec
 */
public final class WolfSoundVariantBinaryTagCodec implements BinaryTagCodec<WolfSoundVariant> {

    private static final String AMBIENT_SOUND_FIELD = "ambient_sound";
    private static final String DEATH_SOUND_FIELD = "death_sound";
    private static final String GROWL_SOUND_FIELD = "growl_sound";
    private static final String HURT_SOUND_FIELD = "hurt_sound";
    private static final String PANT_SOUND_FIELD = "pant_sound";
    private static final String WHINE_SOUND_FIELD = "whine_sound";

    /**
     * An instance of the {@linkplain WolfSoundVariantBinaryTagCodec wolf sound variant binary-tag codec}.
     *
     * @since 1.0
     */
    public static final WolfSoundVariantBinaryTagCodec INSTANCE = new WolfSoundVariantBinaryTagCodec();

    private WolfSoundVariantBinaryTagCodec() {}

    @Override
    public @NotNull WolfSoundVariant decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new WolfSoundVariant(
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(AMBIENT_SOUND_FIELD, compound)),
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(DEATH_SOUND_FIELD, compound)),
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(GROWL_SOUND_FIELD, compound)),
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(HURT_SOUND_FIELD, compound)),
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(PANT_SOUND_FIELD, compound)),
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(WHINE_SOUND_FIELD, compound))
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a wolf sound variant"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull WolfSoundVariant decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(AMBIENT_SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.ambientSound()))
                .put(DEATH_SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.deathSound()))
                .put(GROWL_SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.growlSound()))
                .put(HURT_SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.hurtSound()))
                .put(PANT_SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.pantSound()))
                .put(WHINE_SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.whineSound()))
                .build();
    }
}