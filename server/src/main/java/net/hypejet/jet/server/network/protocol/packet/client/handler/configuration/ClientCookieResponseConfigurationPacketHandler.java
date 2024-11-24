package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientCookieResponseConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientCookieResponseConfigurationPacket a cookie response configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponseConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientCookieResponseConfigurationPacketHandler
        implements ClientPacketHandler<ClientCookieResponseConfigurationPacket> {
    @Override
    public @NonNull ClientCookieResponseConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientCookieResponseConfigurationPacket(
                PackedKeyNetworkCodec.INSTANCE.read(buf),
                buf.readBoolean() ? ByteArrayNetworkReader.INSTANCE.read(buf) : null
        );
    }

    @Override
    public void handle(@NonNull ClientCookieResponseConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task must be a configuration task");
        configurationTask.player().handleCookieResponse(packet.identifier(), packet.data());
    }
}
