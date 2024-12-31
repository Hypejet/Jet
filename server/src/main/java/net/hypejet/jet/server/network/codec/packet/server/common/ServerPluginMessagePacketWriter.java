package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPluginMessagePacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerPluginMessagePacket a server
 * plugin message packet}.
 *
 * @since 1.0
 * @see ServerPluginMessagePacket
 * @see NetworkWriter
 */
public final class ServerPluginMessagePacketWriter implements NetworkWriter<ServerPluginMessagePacket> {

    /**
     * An instance of the {@linkplain ServerPluginMessagePacketWriter server plugin message packet writer}.
     *
     * @since 1.0
     */
    public static final ServerPluginMessagePacketWriter INSTANCE = new ServerPluginMessagePacketWriter();

    private ServerPluginMessagePacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessagePacket object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.key());
        buf.writeBytes(object.data().array());
    }
}