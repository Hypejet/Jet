package net.hypejet.jet.server.registry.writers.registry.biome.effects.sound;

import net.hypejet.jet.data.model.api.registries.biome.effects.sound.BiomeSoundEvent;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain BiomeSoundEvent a biome sound event}
 * into {@linkplain BinaryTag a binary tag}.
 *
 * @since 1.0
 * @see BiomeSoundEvent
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class BiomeSoundEventBinaryTagWriter implements Writer<BiomeSoundEvent, BinaryTag> {
    /**
     * An instance of the {@linkplain BiomeSoundEventBinaryTagWriter a biome sound event binary tag writer}.
     *
     * @since 1.0
     */
    public static final BiomeSoundEventBinaryTagWriter INSTANCE = new BiomeSoundEventBinaryTagWriter();

    private static final String SOUND_ID = "sound_id";
    private static final String RANGE = "range";

    private BiomeSoundEventBinaryTagWriter() {}

    @Override
    public @NonNull BinaryTag write(@NonNull BiomeSoundEvent object) {
        BinaryTag binarySound = PackedKeyBinaryTagWriter.INSTANCE.write(object.key());

        Float range = object.range();
        if (range == null)
            return binarySound;

        return CompoundBinaryTag.builder()
                .put(SOUND_ID, binarySound)
                .putFloat(RANGE, range)
                .build();
    }
}