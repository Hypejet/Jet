package net.hypejet.jet.server.registry.writers.registry.biome.effects.music;

import net.hypejet.jet.data.model.api.registries.biome.effects.music.BiomeMusic;
import net.hypejet.jet.server.registry.writers.registry.biome.effects.sound.BiomeSoundEventBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain BiomeMusic a biome music}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see BiomeMusic
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class BiomeMusicBinaryTagWriter implements Writer<BiomeMusic, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain BiomeMusicBinaryTagWriter biome music binary tag writer}.
     *
     * @since 1.0
     */
    public static final BiomeMusicBinaryTagWriter INSTANCE = new BiomeMusicBinaryTagWriter();

    private static final String SOUND = "sound";
    private static final String MIN_DELAY = "min_delay";
    private static final String MAX_DELAY = "max_delay";
    private static final String REPLACE_CURRENT_MUSIC = "replace_current_music";

    private BiomeMusicBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull BiomeMusic object) {
        return CompoundBinaryTag.builder()
                .put(SOUND, BiomeSoundEventBinaryTagWriter.INSTANCE.write(object.event()))
                .putInt(MIN_DELAY, object.minimumDelay())
                .putInt(MAX_DELAY, object.maximumDelay())
                .putBoolean(REPLACE_CURRENT_MUSIC, object.replaceCurrentMusic())
                .build();
    }
}