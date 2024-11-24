package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKeepAliveConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveResponseHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientKeepAliveConfigurationPacket a keep alive configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKeepAliveConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientKeepAliveConfigurationPacketHandler
        implements ClientPacketHandler<ClientKeepAliveConfigurationPacket> {
    @Override
    public @NonNull ClientKeepAliveConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientKeepAliveConfigurationPacket(buf.readLong());
    }

    @Override
    public void handle(@NonNull ClientKeepAliveConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof KeepAliveResponseHandler keepAliveResponseHandler))
            throw new IllegalArgumentException("The current session task is must be a keep alive response handler");
        keepAliveResponseHandler.handleKeepAliveResponse(packet.keepAliveIdentifier());
    }
}