package net.hypejet.jet.server.registry.codecs.world.sound;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.ComponentBinaryTagCodec;
import net.hypejet.jet.world.sound.Instrument;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain Instrument instruments}.
 *
 * @since 1.0
 * @see Instrument
 * @see BinaryTagCodec
 */
public final class InstrumentBinaryTagCodec implements BinaryTagCodec<Instrument> {

    private static final String SOUND_EVENT_FIELD = "sound_event";
    private static final String USE_DURATION_FIELD = "use_duration";
    private static final String RANGE_FIELD = "range";
    private static final String DESCRIPTION_FIELD = "description";

    /**
     * An instance of the {@linkplain InstrumentBinaryTagCodec instrument binary-tag codec}.
     *
     * @since 1.0
     */
    public static final InstrumentBinaryTagCodec INSTANCE = new InstrumentBinaryTagCodec();

    private InstrumentBinaryTagCodec() {}

    @Override
    public @NotNull Instrument decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new Instrument(
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(SOUND_EVENT_FIELD, compound)),
                    requiredTag(USE_DURATION_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                    requiredTag(RANGE_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                    ComponentBinaryTagCodec.INSTANCE.decode(requiredTag(DESCRIPTION_FIELD, compound))
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to an instrument"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Instrument decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(SOUND_EVENT_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.soundEvent()))
                .putFloat(USE_DURATION_FIELD, decoded.useDuration())
                .putFloat(RANGE_FIELD, decoded.range())
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(decoded.description()))
                .build();
    }
}