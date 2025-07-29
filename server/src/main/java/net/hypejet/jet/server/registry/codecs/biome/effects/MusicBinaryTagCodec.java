package net.hypejet.jet.server.registry.codecs.biome.effects;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.sound.SoundEventBinaryTagCodec;
import net.hypejet.jet.world.biome.effects.Music;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.booleanValue;
import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Music music}.
 *
 * @since 1.0
 * @see Music
 * @see BinaryTagCodec
 */
public final class MusicBinaryTagCodec implements BinaryTagCodec<Music> {

    private static final String SOUND_EVENT_FIELD = "sound";
    private static final String MIN_DELAY_FIELD = "min_delay";
    private static final String MAX_DELAY_FIELD = "max_delay";
    private static final String REPLACE_CURRENT_MUSIC_FIELD = "replace_current_music";

    /**
     * An instance of the {@linkplain MusicBinaryTagCodec music binary tag codec}.
     *
     * @since 1.0
     */
    public static final MusicBinaryTagCodec INSTANCE = new MusicBinaryTagCodec();

    private MusicBinaryTagCodec() {}

    @Override
    public @NotNull Music decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new Music(
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(SOUND_EVENT_FIELD, compound)),
                    requiredTag(MIN_DELAY_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(MAX_DELAY_FIELD, compound, BinaryTagTypes.INT).value(),
                    booleanValue(requiredTag(REPLACE_CURRENT_MUSIC_FIELD, compound, BinaryTagTypes.BYTE))
            );
        } else {
            throw new IllegalArgumentException("The encoded tag type must be of compound type to decode it to music");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Music decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(SOUND_EVENT_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.soundEvent()))
                .putInt(MIN_DELAY_FIELD, decoded.minDelay())
                .putInt(MAX_DELAY_FIELD, decoded.maxDelay())
                .putBoolean(REPLACE_CURRENT_MUSIC_FIELD, decoded.replaceCurrentMusic())
                .build();
    }
}