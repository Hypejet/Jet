package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerPluginMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerPluginMessagePlayPacket plugin message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessagePlayPacket
 * @see PacketCodec
 */
public final class ServerPluginMessagePlayPacketCodec extends PacketCodec<ServerPluginMessagePlayPacket> {
    /**
     * Constructs the {@linkplain ServerPluginMessagePlayPacketCodec plugin message play packet codec}.
     *
     * @since 1.0
     */
    public ServerPluginMessagePlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_PLUGIN_MESSAGE, ServerPluginMessagePlayPacket.class);
    }

    @Override
    public @NonNull ServerPluginMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ServerPluginMessagePlayPacket(IdentifierNetworkCodec.instance().read(buf),
                NetworkUtil.readRemainingBytes(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessagePlayPacket object) {
        IdentifierNetworkCodec.instance().write(buf, object.identifier());
        buf.writeBytes(object.data());
    }
}
