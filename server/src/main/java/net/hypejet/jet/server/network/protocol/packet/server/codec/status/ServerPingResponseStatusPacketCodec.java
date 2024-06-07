package net.hypejet.jet.server.network.protocol.packet.server.codec.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.status.ServerPingResponseStatusPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerPingResponseStatusPacket ping response status packet}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ServerPingResponseStatusPacketCodec extends PacketCodec<ServerPingResponseStatusPacket> {
    /**
     * Constructs the {@linkplain ServerPingResponseStatusPacket ping response status packet codec}.
     *
     * @since 1.0
     */
    public ServerPingResponseStatusPacketCodec() {
        super(ServerPacketIdentifiers.STATUS_PING_RESPONSE, ServerPingResponseStatusPacket.class);
    }

    @Override
    public @NonNull ServerPingResponseStatusPacket read(@NonNull ByteBuf buf) {
        return new ServerPingResponseStatusPacket(buf.readLong());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPingResponseStatusPacket object) {
        buf.writeLong(object.payload());
    }
}
