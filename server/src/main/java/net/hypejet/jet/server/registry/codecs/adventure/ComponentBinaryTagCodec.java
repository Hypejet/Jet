package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Component components}.
 *
 * @since 1.0
 * @see Component
 * @see BinaryTagCodec
 */
@NullMarked
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

    private ComponentBinaryTagCodec(NBTComponentSerializer serializer) {
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    @Override
    public Component decode(BinaryTag binaryTag, JetMinecraftServer server) {
        return this.serializer.deserialize(binaryTag);
    }

    @Override
    public BinaryTag encode(Component value, JetMinecraftServer server) {
        return this.serializer.serialize(value);
    }
}