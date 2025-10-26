package net.hypejet.jet.server.registry.codecs.world.biome.effects;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.sound.SoundEventBinaryTagCodec;
import net.hypejet.jet.world.biome.effects.AmbientMoodSettings;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain AmbientMoodSettings ambient mood settings}.
 *
 * @since 1.0
 * @see AmbientMoodSettings
 * @see BinaryTagCodec
 */
@NullMarked
public final class AmbientMoodSettingsBinaryTagCodec implements BinaryTagCodec<AmbientMoodSettings> {

    private static final String SOUND_FIELD = "sound";
    private static final String TICK_DELAY_FIELD = "tick_delay";
    private static final String BLOCK_SEARCH_EXTENT_FIELD = "block_search_extent";
    private static final String OFFSET_FIELD = "offset";

    /**
     * An instance of the {@linkplain AmbientMoodSettingsBinaryTagCodec ambient mood settings binary tag codec}.
     *
     * @since 1.0
     */
    public static final AmbientMoodSettingsBinaryTagCodec INSTANCE = new AmbientMoodSettingsBinaryTagCodec();

    private AmbientMoodSettingsBinaryTagCodec() {}

    @Override
    public AmbientMoodSettings decode(BinaryTag binaryTag) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return new AmbientMoodSettings(
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(SOUND_FIELD, compound)),
                    requiredTag(TICK_DELAY_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(BLOCK_SEARCH_EXTENT_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(OFFSET_FIELD, compound, BinaryTagTypes.DOUBLE).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to ambient mood settings"
            );
        }
    }

    @Override
    public BinaryTag encode(AmbientMoodSettings value) {
        return CompoundBinaryTag.builder()
                .put(SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(value.soundEvent()))
                .putInt(TICK_DELAY_FIELD, value.tickDelay())
                .putInt(BLOCK_SEARCH_EXTENT_FIELD, value.blockSearchExtent())
                .putDouble(OFFSET_FIELD, value.soundPositionOffset())
                .build();
    }
}