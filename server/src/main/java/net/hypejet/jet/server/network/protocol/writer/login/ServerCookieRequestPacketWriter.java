package net.hypejet.jet.server.network.protocol.writer.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.cookie.ServerCookieRequestPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which writes a {@linkplain ServerCookieRequestPacket cookie
 * request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestPacket
 * @see PacketWriter
 */
public final class ServerCookieRequestPacketWriter extends PacketWriter<ServerCookieRequestPacket> {
    /**
     * Constructs a {@linkplain ServerCookieRequestPacketWriter cookie request packet writer}.
     *
     * @since 1.0
     */
    public ServerCookieRequestPacketWriter() {
        super(5, ProtocolState.LOGIN, ServerCookieRequestPacket.class);
    }

    @Override
    public void write(@NonNull ServerCookieRequestPacket packet, @NonNull NetworkBuffer buffer) {
        buffer.writeIdentifier(packet.identifier());
    }
}
