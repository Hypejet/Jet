package net.hypejet.jet.server.network.protocol.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectPacket;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which reads and writes a {@linkplain ServerDisconnectPacket
 * disconnect packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectPacket
 * @see PacketWriter
 */
public final class ServerDisconnectPacketWriter extends PacketWriter<ServerDisconnectPacket> {
    /**
     * Constructs the {@linkplain ServerDisconnectPacketWriter disconnect packet writer}.
     *
     * @since 1.0
     */
    public ServerDisconnectPacketWriter() {
        super(0, ServerDisconnectPacket.class);
    }

    @Override
    public @NonNull ServerDisconnectPacket read(@NonNull ByteBuf buf) {
        return new ServerDisconnectPacket(NetworkUtil.readJsonComponent(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectPacket object) {
        NetworkUtil.writeJsonComponent(buf, object.reason());
    }
}