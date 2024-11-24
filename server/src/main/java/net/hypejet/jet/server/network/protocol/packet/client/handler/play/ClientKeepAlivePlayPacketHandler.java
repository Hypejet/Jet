package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientKeepAlivePlayPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveResponseHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientKeepAlivePlayPacket a keep alive play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKeepAlivePlayPacket
 * @see ClientPacketHandler
 */
public final class ClientKeepAlivePlayPacketHandler implements ClientPacketHandler<ClientKeepAlivePlayPacket> {
    @Override
    public @NonNull ClientKeepAlivePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientKeepAlivePlayPacket(buf.readLong());
    }

    @Override
    public void handle(@NonNull ClientKeepAlivePlayPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof KeepAliveResponseHandler keepAliveResponseHandler))
            throw new IllegalArgumentException("The current session task is must be a keep alive response handler");
        keepAliveResponseHandler.handleKeepAliveResponse(packet.keepAliveIdentifier());
    }
}