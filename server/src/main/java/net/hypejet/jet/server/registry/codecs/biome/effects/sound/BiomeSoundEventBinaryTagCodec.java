package net.hypejet.jet.server.registry.codecs.biome.effects.sound;

import net.hypejet.jet.registry.registries.biome.effects.sound.BiomeSoundEvent;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec}, which reads and writes
 * a {@linkplain BiomeSoundEvent biome sound event}.
 *
 * @since 1.0
 * @author Codestech
 * @see BiomeSoundEvent
 * @see BinaryTagCodec
 */
public final class BiomeSoundEventBinaryTagCodec implements BinaryTagCodec<BiomeSoundEvent> {

    private static final BiomeSoundEventBinaryTagCodec INSTANCE = new BiomeSoundEventBinaryTagCodec();

    private static final String SOUND_ID = "sound_id";
    private static final String RANGE = "range";

    private BiomeSoundEventBinaryTagCodec() {}

    @Override
    public @NonNull BiomeSoundEvent read(@NonNull BinaryTag tag) {
        if (tag instanceof StringBinaryTag)
            return new BiomeSoundEvent(PackedIdentifierBinaryTagCodec.instance().read(tag), null);

        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified is not a compound binary tag");

        BinaryTag binaryIdentifier = compound.get(SOUND_ID);
        BinaryTag binaryRange = compound.get(RANGE);

        if (binaryIdentifier == null)
            throw new IllegalArgumentException("The sound identifier was not specified");

        Key identifier = PackedIdentifierBinaryTagCodec.instance().read(binaryIdentifier);
        Float range = binaryRange instanceof FloatBinaryTag floatTag ? floatTag.value() : null;

        return new BiomeSoundEvent(identifier, range);
    }

    @Override
    public @NonNull BinaryTag write(@NonNull BiomeSoundEvent object) {
        BinaryTag binarySound = PackedIdentifierBinaryTagCodec.instance().write(object.key());

        Float range = object.range();
        if (range == null)
            return binarySound;

        return CompoundBinaryTag.builder()
                .put(SOUND_ID, binarySound)
                .putFloat(RANGE, range)
                .build();
    }

    /**
     * Gets an instance of the {@linkplain BiomeSoundEventBinaryTagCodec biome sound event binary-tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BiomeSoundEventBinaryTagCodec instance() {
        return INSTANCE;
    }
}