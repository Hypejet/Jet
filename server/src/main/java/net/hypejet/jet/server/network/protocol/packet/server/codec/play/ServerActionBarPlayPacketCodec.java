package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
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
        return new ServerActionBarPlayPacket(ComponentNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerActionBarPlayPacket object) {
        ComponentNetworkCodec.instance().write(buf, object.text());
    }
}