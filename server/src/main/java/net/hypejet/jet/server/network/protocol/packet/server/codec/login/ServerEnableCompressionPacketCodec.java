package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionPacket;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.server.codec.ServerPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacketCodec server packet codec}, which reads and writes
 * a {@linkplain ServerEnableCompressionPacket enable compression packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEnableCompressionPacket
 * @see ServerPacketCodec
 */
public final class ServerEnableCompressionPacketCodec extends ServerPacketCodec<ServerEnableCompressionPacket> {
    /**
     * Constructs the {@linkplain ServerEnableCompressionPacketCodec enable compression packet codec}.
     *
     * @since 1.0
     */
    public ServerEnableCompressionPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_ENABLE_COMPRESSION, ServerEnableCompressionPacket.class);
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
