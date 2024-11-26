package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.game.pack.PackInfoNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientKnownPacksConfigurationPacket a known packs configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKnownPacksConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientKnownPacksConfigurationPacketHandler
        implements ClientPacketHandler<ClientKnownPacksConfigurationPacket> {
    @Override
    public @NonNull ClientKnownPacksConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientKnownPacksConfigurationPacket(PackInfoNetworkCodec.COLLECTION_CODEC.read(buf));
    }

    @Override
    public void handle(@NonNull ClientKnownPacksConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task is must be a configuration task");
        configurationTask.handleKnownPacks(packet);
    }
}
