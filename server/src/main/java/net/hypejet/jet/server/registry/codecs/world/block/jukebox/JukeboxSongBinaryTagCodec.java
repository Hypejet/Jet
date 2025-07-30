package net.hypejet.jet.server.registry.codecs.world.block.jukebox;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.ComponentBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.sound.SoundEventBinaryTagCodec;
import net.hypejet.jet.world.block.jukebox.JukeboxSong;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain JukeboxSong jukebox songs}.
 *
 * @since 1.0
 * @see JukeboxSong
 * @see BinaryTagCodec
 */
public final class JukeboxSongBinaryTagCodec implements BinaryTagCodec<JukeboxSong> {

    private static final String SOUND_EVENT_FIELD = "sound_event";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String LENGTH_IN_SECONDS_FIELD = "length_in_seconds";
    private static final String COMPARATOR_OUTPUT_FIELD = "comparator_output";

    /**
     * An instance of the {@linkplain JukeboxSongBinaryTagCodec jukebox song binary-tag codec}.
     *
     * @since 1.0
     */
    public static final JukeboxSongBinaryTagCodec INSTANCE = new JukeboxSongBinaryTagCodec();

    private JukeboxSongBinaryTagCodec() {}

    @Override
    public @NotNull JukeboxSong decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new JukeboxSong(
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(SOUND_EVENT_FIELD, compound)),
                    ComponentBinaryTagCodec.INSTANCE.decode(requiredTag(DESCRIPTION_FIELD, compound)),
                    requiredTag(LENGTH_IN_SECONDS_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                    requiredTag(COMPARATOR_OUTPUT_FIELD, compound, BinaryTagTypes.INT).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to jukebox song"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull JukeboxSong decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(SOUND_EVENT_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.soundEvent()))
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(decoded.description()))
                .putFloat(LENGTH_IN_SECONDS_FIELD, decoded.lengthInSeconds())
                .putInt(COMPARATOR_OUTPUT_FIELD, decoded.comparatorOutput())
                .build();
    }
}