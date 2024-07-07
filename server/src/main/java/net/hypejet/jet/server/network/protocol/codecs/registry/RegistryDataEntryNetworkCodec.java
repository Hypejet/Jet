package net.hypejet.jet.server.network.protocol.codecs.registry;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket.Entry;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Entry registry data
 * entry}.
 *
 * @since 1.0
 * @author Codestech
 * @see Entry
 * @see NetworkCodec
 */
public final class RegistryDataEntryNetworkCodec implements NetworkCodec<Entry> {

    private static final RegistryDataEntryNetworkCodec INSTANCE = new RegistryDataEntryNetworkCodec();

    private RegistryDataEntryNetworkCodec() {}

    @Override
    public @NonNull Entry read(@NonNull ByteBuf buf) {
        return new Entry(
                IdentifierNetworkCodec.instance().read(buf),
                buf.readBoolean() ? BinaryTagCodec.instance().read(buf) : null
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Entry object) {
        IdentifierNetworkCodec.instance().write(buf, object.identifier());

        BinaryTag data = object.data();
        buf.writeBoolean(data != null);

        if (data != null) {
            BinaryTagCodec.instance().write(buf, data);
        }
    }

    /**
     * Gets an instance of the {@linkplain RegistryDataEntryNetworkCodec registry data entry network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull RegistryDataEntryNetworkCodec instance() {
        return INSTANCE;
    }
}