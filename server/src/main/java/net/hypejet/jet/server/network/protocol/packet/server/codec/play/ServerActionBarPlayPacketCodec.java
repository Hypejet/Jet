package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes a {@linkplain ServerActionBarPlayPacket
 * action bar play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerActionBarPlayPacket
 * @see PacketCodec
 */
public final class ServerActionBarPlayPacketCodec extends PacketCodec<ServerActionBarPlayPacket> {
    /**
     * Constructs the {@linkplain ServerActionBarPlayPacketCodec action bar play packet codec}.
     *
     * @since 1.0
     */
    public ServerActionBarPlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_ACTION_BAR, ServerActionBarPlayPacket.class);
    }

    @Override
    public @NonNull ServerActionBarPlayPacket read(@NonNull ByteBuf buf) {
        return new ServerActionBarPlayPacket(NetworkUtil.readComponent(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerActionBarPlayPacket object) {
        NetworkUtil.writeComponent(buf, object.text());
    }
}