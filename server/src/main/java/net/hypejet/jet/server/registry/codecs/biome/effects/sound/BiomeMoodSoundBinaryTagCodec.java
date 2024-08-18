package net.hypejet.jet.server.registry.codecs.biome.effects.sound;

import net.hypejet.jet.biome.effects.sound.BiomeMoodSound;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec}, which reads and writes
 * a {@linkplain BiomeMoodSound biome mood sound}.
 *
 * @since 1.0
 * @author Codestech
 * @see BiomeMoodSound
 * @see BinaryTagCodec
 */
public final class BiomeMoodSoundBinaryTagCodec implements BinaryTagCodec<BiomeMoodSound> {

    private static final BiomeMoodSoundBinaryTagCodec INSTANCE = new BiomeMoodSoundBinaryTagCodec();

    private static final String SOUND = "sound";
    private static final String TICK_DELAY = "tick_delay";
    private static final String BLOCK_SEARCH_EXTENT = "block_search_extent";
    private static final String OFFSET = "offset";

    private BiomeMoodSoundBinaryTagCodec() {}

    @Override
    public @NonNull BiomeMoodSound read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag is not a compound binary tag");

        BinaryTag binarySound = compound.get(SOUND);
        if (binarySound == null)
            throw new IllegalArgumentException("The sound was not specified");

        return new BiomeMoodSound(PackedIdentifierBinaryTagCodec.instance().read(binarySound),
                compound.getInt(TICK_DELAY), compound.getInt(BLOCK_SEARCH_EXTENT), compound.getDouble(OFFSET));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull BiomeMoodSound object) {
        return CompoundBinaryTag.builder()
                .put(SOUND, PackedIdentifierBinaryTagCodec.instance().write(object.key()))
                .putInt(TICK_DELAY, object.tickDelay())
                .putInt(BLOCK_SEARCH_EXTENT, object.blockSearchExtent())
                .putDouble(OFFSET, object.offset())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain BiomeMoodSoundBinaryTagCodec biome mood sound binary-tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BiomeMoodSoundBinaryTagCodec instance() {
        return INSTANCE;
    }
}