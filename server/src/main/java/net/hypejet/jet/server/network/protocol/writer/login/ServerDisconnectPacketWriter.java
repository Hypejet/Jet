package net.hypejet.jet.server.network.protocol.writer.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.disconnect.ServerDisconnectPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which writes a {@linkplain ServerDisconnectPacket disconnect
 * packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectPacket
 * @see PacketWriter
 */
public final class ServerDisconnectPacketWriter extends PacketWriter<ServerDisconnectPacket> {
    /**
     * Constructs a {@linkplain ServerDisconnectPacketWriter disconnect packet writer}.
     *
     * @since 1.0
     */
    public ServerDisconnectPacketWriter() {
        super(0, ProtocolState.LOGIN, ServerDisconnectPacket.class);
    }

    @Override
    public void write(@NonNull ServerDisconnectPacket packet, @NonNull NetworkBuffer buffer) {
        buffer.writeJsonTextComponent(packet.reason());
    }
}