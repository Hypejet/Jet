package net.hypejet.jet.server.network.codec.packet.server.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerPluginMessageRequestLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerPluginMessageRequestLoginPacket a plugin message request login packet}.
 *
 * @since 1.0
 * @see ServerPluginMessageRequestLoginPacket
 * @see NetworkWriter
 */
public final class ServerPluginMessageRequestLoginPacketWriter
        implements NetworkWriter<ServerPluginMessageRequestLoginPacket> {

    /**
     * An instance of the {@linkplain ServerPluginMessageRequestLoginPacketWriter server plugin message request login
     * packet writer}.
     *
     * @since 1.0
     */
    public static final ServerPluginMessageRequestLoginPacketWriter
            INSTANCE = new ServerPluginMessageRequestLoginPacketWriter();

    private ServerPluginMessageRequestLoginPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessageRequestLoginPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.messageId());
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.channel());
        buf.writeBytes(object.data().array());
    }
}
