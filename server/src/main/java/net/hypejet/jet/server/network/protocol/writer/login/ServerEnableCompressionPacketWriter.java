package net.hypejet.jet.server.network.protocol.writer.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.compression.ServerEnableCompressionPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which writes a {@linkplain ServerEnableCompressionPacket
 * enable compression packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEnableCompressionPacket
 * @see PacketWriter
 */
public final class ServerEnableCompressionPacketWriter extends PacketWriter<ServerEnableCompressionPacket> {
    /**
     * Constructs a {@linkplain ServerEnableCompressionPacketWriter enable compression packet writer}.
     *
     * @since 1.0
     */
    public ServerEnableCompressionPacketWriter() {
        super(3, ProtocolState.LOGIN, ServerEnableCompressionPacket.class);
    }

    @Override
    public void write(@NonNull ServerEnableCompressionPacket packet, @NonNull NetworkBuffer buffer) {
        buffer.writeVarInt(packet.threshold());
    }
}
