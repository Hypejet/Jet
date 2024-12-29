package net.hypejet.jet.server.registry.writers.registry.biome.effects.sound;

import net.hypejet.jet.data.model.api.registries.biome.effects.sound.BiomeAdditionalSound;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain BiomeAdditionalSound a biome additional sound}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see BiomeAdditionalSound
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class BiomeAdditionalSoundBinaryTagWriter implements Writer<BiomeAdditionalSound, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain BiomeAdditionalSoundBinaryTagWriter biome additional sound binary tag writer}.
     *
     * @since 1.0
     */
    public static final BiomeAdditionalSoundBinaryTagWriter INSTANCE = new BiomeAdditionalSoundBinaryTagWriter();

    private static final String SOUND = "sound";
    private static final String TICK_CHANCE = "tick_chance";

    private BiomeAdditionalSoundBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull BiomeAdditionalSound object) {
        return CompoundBinaryTag.builder()
                .put(SOUND, BiomeSoundEventBinaryTagWriter.INSTANCE.write(object.event()))
                .putDouble(TICK_CHANCE, object.tickChance())
                .build();
    }
}