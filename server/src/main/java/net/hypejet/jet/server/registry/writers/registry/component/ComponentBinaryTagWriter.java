package net.hypejet.jet.server.registry.writers.registry.component;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain Component a component} into
 * {@linkplain BinaryTag a binary tag}.
 *
 * @since 1.0
 * @see Component
 * @see BinaryTag
 * @see Writer
 */
public final class ComponentBinaryTagWriter implements Writer<Component, BinaryTag> {
    /**
     * An instance of the {@linkplain ComponentBinaryTagWriter component binary tag writer}.
     *
     * @since 1.0
     */
    public static final ComponentBinaryTagWriter INSTANCE = new ComponentBinaryTagWriter(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    private ComponentBinaryTagWriter(@NonNull NBTComponentSerializer serializer) {
        this.serializer = NullabilityUtil.requireNonNull(serializer, "serializer");
    }

    @Override
    public @NonNull BinaryTag write(@NonNull Component object) {
        return this.serializer.serialize(object);
    }
}