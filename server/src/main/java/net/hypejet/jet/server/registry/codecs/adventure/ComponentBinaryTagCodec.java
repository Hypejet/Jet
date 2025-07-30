package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Component components}.
 *
 * @since 1.0
 * @see Component
 * @see BinaryTagCodec
 */
public final class ComponentBinaryTagCodec implements BinaryTagCodec<Component> {
    /**
     * An instance of the {@linkplain ComponentBinaryTagCodec component binary tag codec}
     * using the {@linkplain NBTComponentSerializer#nbt() default NBT component serializer}.
     *
     * @since 1.0
     * @see NBTComponentSerializer#nbt()
     */
    public static final ComponentBinaryTagCodec INSTANCE = new ComponentBinaryTagCodec(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    private ComponentBinaryTagCodec(@NonNull NBTComponentSerializer serializer) {
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    @Override
    public @NotNull Component decode(@NotNull BinaryTag encoded) {
        return this.serializer.deserialize(encoded);
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Component decoded) {
        return this.serializer.serialize(decoded);
    }
}