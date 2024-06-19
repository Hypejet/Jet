package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKeepAliveConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientKeepAliveConfigurationPacket keep alive configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKeepAliveConfigurationPacket
 * @see PacketCodec
 */
public final class ClientKeepAliveConfigurationPacketCodec extends PacketCodec<ClientKeepAliveConfigurationPacket> {
    /**
     * Constructs the {@linkplain ClientKeepAliveConfigurationPacketCodec keep alive configuration packet codec}.
     *
     * @since 1.0
     */
    public ClientKeepAliveConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_KEEP_ALIVE, ClientKeepAliveConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientKeepAliveConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientKeepAliveConfigurationPacket(buf.readLong());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientKeepAliveConfigurationPacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }
}