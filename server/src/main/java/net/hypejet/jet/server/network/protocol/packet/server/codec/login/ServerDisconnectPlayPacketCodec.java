package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerDisconnectPlayPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes a {@linkplain ServerDisconnectPlayPacket
 * disconnect play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectPlayPacket
 * @see PacketCodec
 */
public final class ServerDisconnectPlayPacketCodec extends PacketCodec<ServerDisconnectPlayPacket> {
    /**
     * Constructs the {@linkplain ServerDisconnectPlayPacketCodec server disconnect play packet codec}.
     *
     * @since 1.0
     */
    public ServerDisconnectPlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_DISCONNECT, ServerDisconnectPlayPacket.class);
    }

    @Override
    public @NonNull ServerDisconnectPlayPacket read(@NonNull ByteBuf buf) {
        return new ServerDisconnectPlayPacket(NetworkUtil.readComponent(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectPlayPacket object) {
        NetworkUtil.writeComponent(buf, object.reason());
    }
}