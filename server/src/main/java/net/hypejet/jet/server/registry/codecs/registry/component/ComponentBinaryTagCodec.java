package net.hypejet.jet.server.registry.codecs.registry.component;

import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain Component component}.
 *
 * @since 1.0
 * @author Codestech
 * @see Component
 * @see BinaryTagCodec
 */
public final class ComponentBinaryTagCodec implements BinaryTagCodec<Component> {

    private static final ComponentBinaryTagCodec INSTANCE = new ComponentBinaryTagCodec(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    private ComponentBinaryTagCodec(@NonNull NBTComponentSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public @NonNull Component read(@NonNull BinaryTag tag) {
        return this.serializer.deserialize(tag);
    }

    @Override
    public @NonNull BinaryTag write(@NonNull Component object) {
        return this.serializer.serialize(object);
    }

    /**
     * Gets an instance of the {@linkplain ComponentBinaryTagCodec component binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ComponentBinaryTagCodec instance() {
        return INSTANCE;
    }
}