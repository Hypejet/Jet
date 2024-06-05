package net.hypejet.jet.server.network.protocol.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestPacket;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which reads and writes a {@linkplain ServerCookieRequestPacket
 * cookie request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestPacket
 * @see PacketWriter
 */
public final class ServerCookieRequestPacketWriter extends PacketWriter<ServerCookieRequestPacket> {
    /**
     * Constructs the {@linkplain ServerCookieRequestPacketWriter cookie request packet writer}.
     *
     * @since 1.0
     */
    public ServerCookieRequestPacketWriter() {
        super(5, ServerCookieRequestPacket.class);
    }

    @Override
    public @NonNull ServerCookieRequestPacket read(@NonNull ByteBuf buf) {
        return new ServerCookieRequestPacket(NetworkUtil.readIdentifier(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCookieRequestPacket object) {
        NetworkUtil.writeIdentifier(buf, object.identifier());
    }
}
