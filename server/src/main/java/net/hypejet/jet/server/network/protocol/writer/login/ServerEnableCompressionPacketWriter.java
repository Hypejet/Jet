package net.hypejet.jet.server.network.protocol.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionPacket;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which reads and writes
 * a {@linkplain ServerEnableCompressionPacket enable compression packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEnableCompressionPacket
 * @see PacketWriter
 */
public final class ServerEnableCompressionPacketWriter extends PacketWriter<ServerEnableCompressionPacket> {
    /**
     * Constructs the {@linkplain ServerEnableCompressionPacketWriter enable compression packet writer}.
     *
     * @since 1.0
     */
    public ServerEnableCompressionPacketWriter() {
        super(3, ServerEnableCompressionPacket.class);
    }

    @Override
    public @NonNull ServerEnableCompressionPacket read(@NonNull ByteBuf buf) {
        return new ServerEnableCompressionPacket(NetworkUtil.readVarInt(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEnableCompressionPacket object) {
        NetworkUtil.writeVarInt(buf, object.threshold());
    }
}
