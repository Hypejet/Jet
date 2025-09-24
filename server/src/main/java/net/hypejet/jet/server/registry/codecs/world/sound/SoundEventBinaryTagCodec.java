package net.hypejet.jet.server.registry.codecs.world.sound;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.HolderBinaryTagCodec;
import net.hypejet.jet.world.sound.SoundEvent;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.optionalTag;
import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain SoundEvent sound events}.
 *
 * @since 1.0
 * @see SoundEvent
 * @see BinaryTagCodec
 */
@NullMarked
public final class SoundEventBinaryTagCodec implements BinaryTagCodec<SoundEvent> {
    /**
     * An instance of the {@linkplain SoundEventBinaryTagCodec sound event binary tag codec}.
     *
     * @since 1.0
     */
    public static final SoundEventBinaryTagCodec INSTANCE = new SoundEventBinaryTagCodec();

    /**
     * An instance of the {@linkplain HolderBinaryTagCodec holder binary tag codec}
     * with holder value type of {@linkplain SoundEvent sound event}.
     *
     * @since 1.0
     * @see HolderBinaryTagCodec
     */
    public static final HolderBinaryTagCodec<SoundEvent> HOLDER_CODEC = new HolderBinaryTagCodec<>(INSTANCE, true);

    private static final String SOUND_KEY_FIELD = "sound";
    private static final String RANGE_FIELD = "range";

    private SoundEventBinaryTagCodec() {}

    @Override
    public SoundEvent decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            FloatBinaryTag rangeTag = optionalTag(RANGE_FIELD, compound, BinaryTagTypes.FLOAT);
            return new SoundEvent(
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(SOUND_KEY_FIELD, compound), server),
                    rangeTag == null ? null : rangeTag.value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a sound event"
            );
        }
    }

    @Override
    public BinaryTag encode(SoundEvent value, JetMinecraftServer server) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(SOUND_KEY_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.sound(), server));

        Float range = value.range();
        if (range != null) {
            builder.putFloat(RANGE_FIELD, range);
        }

        return builder.build();
    }
}