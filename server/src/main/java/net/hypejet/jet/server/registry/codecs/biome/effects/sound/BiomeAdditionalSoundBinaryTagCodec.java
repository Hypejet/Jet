package net.hypejet.jet.server.registry.codecs.biome.effects.sound;

import net.hypejet.jet.biome.effects.sound.BiomeAdditionalSound;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary-tag codec}, which reads and writes
 * a {@linkplain BiomeAdditionalSound biome additional sound}.
 *
 * @since 1.0
 * @author Codestech
 * @see BiomeAdditionalSound
 * @see BinaryTagCodec
 */
public final class BiomeAdditionalSoundBinaryTagCodec implements BinaryTagCodec<BiomeAdditionalSound> {

    private static final BiomeAdditionalSoundBinaryTagCodec INSTANCE = new BiomeAdditionalSoundBinaryTagCodec();

    private static final String SOUND = "sound";
    private static final String TICK_CHANCE = "tick_chance";

    private BiomeAdditionalSoundBinaryTagCodec() {}

    @Override
    public @NonNull BiomeAdditionalSound read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag is not a compound binary tag");

        BinaryTag binarySound = compound.get(SOUND);
        if (binarySound == null)
            throw new IllegalArgumentException("The sound was not specified");

        return new BiomeAdditionalSound(PackedIdentifierBinaryTagCodec.instance().read(binarySound),
                compound.getDouble(TICK_CHANCE));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull BiomeAdditionalSound object) {
        return CompoundBinaryTag.builder()
                .put(SOUND, PackedIdentifierBinaryTagCodec.instance().write(object.key()))
                .putDouble(TICK_CHANCE, object.tickChance())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain BiomeAdditionalSoundBinaryTagCodec biome additional sound binary-tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BiomeAdditionalSoundBinaryTagCodec instance() {
        return INSTANCE;
    }
}