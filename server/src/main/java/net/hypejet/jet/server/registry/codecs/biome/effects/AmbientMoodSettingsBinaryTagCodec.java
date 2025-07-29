package net.hypejet.jet.server.registry.codecs.biome.effects;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.sound.SoundEventBinaryTagCodec;
import net.hypejet.jet.world.biome.effects.AmbientMoodSettings;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain AmbientMoodSettings ambient mood settings}.
 *
 * @since 1.0
 * @see AmbientMoodSettings
 * @see BinaryTagCodec
 */
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
    public @NotNull AmbientMoodSettings decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag tag) {
            return new AmbientMoodSettings(
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(SOUND_FIELD, tag)),
                    requiredTag(TICK_DELAY_FIELD, tag, BinaryTagTypes.INT).value(),
                    requiredTag(BLOCK_SEARCH_EXTENT_FIELD, tag, BinaryTagTypes.INT).value(),
                    requiredTag(OFFSET_FIELD, tag, BinaryTagTypes.DOUBLE).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to ambient mood settings"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull AmbientMoodSettings decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.soundEvent()))
                .putInt(TICK_DELAY_FIELD, decoded.tickDelay())
                .putInt(BLOCK_SEARCH_EXTENT_FIELD, decoded.blockSearchExtent())
                .putDouble(OFFSET_FIELD, decoded.soundPositionOffset())
                .build();
    }
}