package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerDisconnectPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerDisconnectPacket a server
 * disconnect packet}.
 *
 * @since 1.0
 * @see ServerDisconnectPacket
 * @see NetworkWriter
 */
public final class ServerDisconnectPacketWriter implements NetworkWriter<ServerDisconnectPacket> {

    /**
     * An instance of the {@linkplain ServerDisconnectPacketWriter server disconnect packet writer}.
     *
     * @since 1.0
     */
    public static final ServerDisconnectPacketWriter INSTANCE = new ServerDisconnectPacketWriter();

    private ServerDisconnectPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectPacket object) {
        ComponentNetworkWriter.INSTANCE.write(buf, object.reason());
    }
}