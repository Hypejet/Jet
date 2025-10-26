package net.hypejet.jet.server.network.codec.game.component;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Component a component}
 * from/as {@linkplain net.kyori.adventure.nbt.BinaryTag a binary tag}.
 *
 * @since 1.0
 * @see Component
 * @see net.kyori.adventure.nbt.BinaryTag
 * @see NetworkWriter
 */
public final class ComponentNetworkWriter implements NetworkWriter<Component> {

    /**
     * A default instance of the {@linkplain ComponentNetworkWriter component network writer}.
     *
     * @since 1.0
     */
    public static final ComponentNetworkWriter INSTANCE = new ComponentNetworkWriter(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    private ComponentNetworkWriter(@NonNull NBTComponentSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull Component object) {
        BinaryTagNetworkWriter.INSTANCE.write(buf, registryManager, this.serializer.serialize(object));
    }

    /**
     * Creates the {@linkplain ComponentNetworkWriter component network writer}.
     *
     * @param serializer a serializer to serialize components with
     * @return the writer, {@link #INSTANCE} is returned if the serializer specified is the same as in it
     * @since 1.0
     */
    public static @NonNull ComponentNetworkWriter create(@NonNull NBTComponentSerializer serializer) {
        if (serializer == INSTANCE.serializer)
            return INSTANCE;
        return new ComponentNetworkWriter(serializer);
    }
}