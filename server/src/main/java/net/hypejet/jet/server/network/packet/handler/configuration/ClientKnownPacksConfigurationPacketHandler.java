package net.hypejet.jet.server.network.packet.handler.configuration;

import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.ConfigurationSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientKnownPacksConfigurationPacket a client known packs configuration packet}.
 *
 * @since 1.0
 * @see ClientKnownPacksConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientKnownPacksConfigurationPacketHandler
        extends ClientPacketHandler<ClientKnownPacksConfigurationPacket> {
    /**
     * Constructs the {@linkplain ClientKnownPacksConfigurationPacketHandler client known packs configuration packet
     * handler}.
     *
     * @since 1.0
     */
    public ClientKnownPacksConfigurationPacketHandler() {
        super(ClientKnownPacksConfigurationPacket.class);
    }

    @Override
    public void handle(@NonNull ClientKnownPacksConfigurationPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof ConfigurationSessionTask task))
            throw new IllegalArgumentException("The current session task is not a configuration session task");
        task.handleKnownPacks(packet);
    }
}