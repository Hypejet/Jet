package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPluginMessageConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * a {@linkplain ClientPluginMessageConfigurationPacket plugin message configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessageConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientPluginMessageConfigurationPacketHandler
        implements ClientPacketHandler<ClientPluginMessageConfigurationPacket> {
    @Override
    public @NonNull ClientPluginMessageConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessageConfigurationPacket(
                PackedKeyNetworkCodec.INSTANCE.read(buf),
                NetworkUtil.readRemainingBytes(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientPluginMessageConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task must be a configuration task");
        configurationTask.player().handlePluginMessage(packet.identifier(), packet.data());
    }
}
