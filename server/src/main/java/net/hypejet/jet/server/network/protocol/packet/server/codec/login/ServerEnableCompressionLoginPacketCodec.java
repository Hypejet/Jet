package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerEnableCompressionLoginPacket enable compression login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEnableCompressionLoginPacket
 * @see PacketCodec
 */
public final class ServerEnableCompressionLoginPacketCodec extends PacketCodec<ServerEnableCompressionLoginPacket> {
    /**
     * Constructs the {@linkplain ServerEnableCompressionLoginPacketCodec enable compression packet codec}.
     *
     * @since 1.0
     */
    public ServerEnableCompressionLoginPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_ENABLE_COMPRESSION, ServerEnableCompressionLoginPacket.class);
    }

    @Override
    public @NonNull ServerEnableCompressionLoginPacket read(@NonNull ByteBuf buf) {
        return new ServerEnableCompressionLoginPacket(VarIntNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEnableCompressionLoginPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.threshold());
    }
}
