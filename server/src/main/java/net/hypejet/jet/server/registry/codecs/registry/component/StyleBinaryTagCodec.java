package net.hypejet.jet.server.registry.codecs.registry.component;

import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain Style style}.
 *
 * @since 1.0
 * @author Codestech
 * @see Style
 * @see BinaryTagCodec
 */
public final class StyleBinaryTagCodec implements BinaryTagCodec<Style> {

    private static final StyleBinaryTagCodec INSTANCE = new StyleBinaryTagCodec(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    private StyleBinaryTagCodec(@NonNull NBTComponentSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public @NonNull Style read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");
        return this.serializer.deserializeStyle(compound);
    }

    @Override
    public @NonNull BinaryTag write(@NonNull Style object) {
        return this.serializer.serializeStyle(object);
    }

    /**
     * Gets an instance of the {@linkplain StyleBinaryTagCodec style binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull StyleBinaryTagCodec instance() {
        return INSTANCE;
    }
}