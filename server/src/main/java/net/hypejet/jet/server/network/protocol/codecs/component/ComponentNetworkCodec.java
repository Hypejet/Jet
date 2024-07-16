package net.hypejet.jet.server.network.protocol.codecs.component;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.BinaryTagCodec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Component component}
 * from/as a {@linkplain net.kyori.adventure.nbt.BinaryTag binary tag}.
 *
 * @since 1.0
 * @author Codestech
 * @see Component
 * @see net.kyori.adventure.nbt.BinaryTag
 * @see NetworkCodec
 */
public final class ComponentNetworkCodec implements NetworkCodec<Component> {

    private static final ComponentNetworkCodec INSTANCE = new ComponentNetworkCodec(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    private ComponentNetworkCodec(@NonNull NBTComponentSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public @NonNull Component read(@NonNull ByteBuf buf) {
        return this.serializer.deserialize(BinaryTagCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Component object) {
        BinaryTagCodec.instance().write(buf, this.serializer.serialize(object));
    }

    /**
     * Creates the {@linkplain ComponentNetworkCodec component network codec}.
     *
     * @param serializer a serializer to serialize components with
     * @return the codec, {@link #INSTANCE} is returned if the serializer specified
     *         is {@linkplain NBTComponentSerializer#nbt()}
     * @since 1.0
     */
    public static @NonNull ComponentNetworkCodec create(@NonNull NBTComponentSerializer serializer) {
        if (serializer == NBTComponentSerializer.nbt())
            return INSTANCE;
        return new ComponentNetworkCodec(serializer);
    }

    /**
     * Gets an instance of the {@linkplain ComponentNetworkCodec component network codec}, whose serializer
     * is {@linkplain NBTComponentSerializer#nbt()}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ComponentNetworkCodec instance() {
        return INSTANCE;
    }
}