package net.hypejet.jet.server.network.packet.server.writer.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.common.ServerDisconnectPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerDisconnectPacket a server
 * disconnect packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectPacket
 * @see NetworkWriter
 */
public final class ServerDisconnectPacketWriter implements NetworkWriter<ServerDisconnectPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectPacket object) {
        ComponentNetworkWriter.INSTANCE.write(buf, object.reason());
    }
}