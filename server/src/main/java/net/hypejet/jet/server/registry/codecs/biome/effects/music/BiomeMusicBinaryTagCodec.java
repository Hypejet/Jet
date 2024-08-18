package net.hypejet.jet.server.registry.codecs.biome.effects.music;

import net.hypejet.jet.biome.effects.music.BiomeMusic;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec}, which reads and writes a {@linkplain BiomeMusic biome
 * music}.
 *
 * @since 1.0
 * @author Codestech
 * @see BiomeMusic
 * @see BinaryTagCodec
 */
public final class BiomeMusicBinaryTagCodec implements BinaryTagCodec<BiomeMusic> {

    private static final BiomeMusicBinaryTagCodec INSTANCE = new BiomeMusicBinaryTagCodec();

    private static final String SOUND = "sound";
    private static final String MIN_DELAY = "min_delay";
    private static final String MAX_DELAY = "max_delay";
    private static final String REPLACE_CURRENT_MUSIC = "replace_current_music";

    private BiomeMusicBinaryTagCodec() {}

    @Override
    public @NonNull BiomeMusic read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag is not a compound binary tag");

        BinaryTag binarySound = compound.get(SOUND);
        if (binarySound == null)
            throw new IllegalArgumentException("The sound was not specified");

        return new BiomeMusic(PackedIdentifierBinaryTagCodec.instance().read(binarySound), compound.getInt(MIN_DELAY),
                compound.getInt(MAX_DELAY), compound.getBoolean(REPLACE_CURRENT_MUSIC));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull BiomeMusic object) {
        return CompoundBinaryTag.builder()
                .put(SOUND, PackedIdentifierBinaryTagCodec.instance().write(object.key()))
                .putInt(MIN_DELAY, object.minimumDelay())
                .putInt(MAX_DELAY, object.maximumDelay())
                .putBoolean(REPLACE_CURRENT_MUSIC, object.replaceCurrentMusic())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain BiomeMusicBinaryTagCodec biome music binary-tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BiomeMusicBinaryTagCodec instance() {
        return INSTANCE;
    }
}