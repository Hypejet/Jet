package net.hypejet.jet.server.registry.codecs.biome.effects;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.sound.SoundEventBinaryTagCodec;
import net.hypejet.jet.world.biome.effects.AmbientAdditionsSettings;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain AmbientAdditionsSettings ambient additions settings}.
 *
 * @since 1.0
 * @see AmbientAdditionsSettings
 * @see BinaryTagCodec
 */
public final class AmbientAdditionsSettingsBinaryTagCodec implements BinaryTagCodec<AmbientAdditionsSettings> {

    private static final String SOUND_EVENT_FIELD = "sound";
    private static final String TICK_CHANCE_FIELD = "tick_chance";

    /**
     * An instance
     * of the {@linkplain AmbientAdditionsSettingsBinaryTagCodec ambient additions settings binary tag codec}.
     *
     * @since 1.0
     */
    public static final AmbientAdditionsSettingsBinaryTagCodec INSTANCE = new AmbientAdditionsSettingsBinaryTagCodec();

    private AmbientAdditionsSettingsBinaryTagCodec() {}

    @Override
    public @NotNull AmbientAdditionsSettings decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag tag) {
            return new AmbientAdditionsSettings(
                    SoundEventBinaryTagCodec.HOLDER_CODEC.decode(requiredTag(SOUND_EVENT_FIELD, tag)),
                    requiredTag(TICK_CHANCE_FIELD, tag, BinaryTagTypes.DOUBLE).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to ambient additions settings"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull AmbientAdditionsSettings decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(SOUND_EVENT_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(decoded.soundEvent()))
                .putDouble(TICK_CHANCE_FIELD, decoded.tickChance())
                .build();
    }
}