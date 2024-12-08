package net.hypejet.jet.server.network.packet.handler.configuration;

import net.hypejet.jet.server.network.packet.packets.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientKnownPacksConfigurationPacket a client known packs configuration packet}.
 *
 * @author Codestech
 * @see ClientKnownPacksConfigurationPacket
 * @see ClientPacketHandler
 * @since 1.0
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
        if (!(session.sessionTask() instanceof ConfigurationTask task))
            throw new IllegalArgumentException("The current session task is not a configuration task");
        task.handleKnownPacks(packet);
    }
}