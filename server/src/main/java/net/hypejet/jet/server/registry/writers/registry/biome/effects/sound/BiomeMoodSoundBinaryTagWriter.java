package net.hypejet.jet.server.registry.writers.registry.biome.effects.sound;

import net.hypejet.jet.data.model.api.registries.biome.effects.sound.BiomeMoodSound;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain BiomeMoodSound a biome mood sound}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see BiomeMoodSound
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class BiomeMoodSoundBinaryTagWriter implements Writer<BiomeMoodSound, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain BiomeMoodSoundBinaryTagWriter biome mood sound binary tag writer}.
     *
     * @since 1.0
     */
    public static final BiomeMoodSoundBinaryTagWriter INSTANCE = new BiomeMoodSoundBinaryTagWriter();

    private static final String SOUND = "sound";
    private static final String TICK_DELAY = "tick_delay";
    private static final String BLOCK_SEARCH_EXTENT = "block_search_extent";
    private static final String OFFSET = "offset";

    private BiomeMoodSoundBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull BiomeMoodSound object) {
        return CompoundBinaryTag.builder()
                .put(SOUND, BiomeSoundEventBinaryTagWriter.INSTANCE.write(object.event()))
                .putInt(TICK_DELAY, object.tickDelay())
                .putInt(BLOCK_SEARCH_EXTENT, object.blockSearchExtent())
                .putDouble(OFFSET, object.offset())
                .build();
    }
}