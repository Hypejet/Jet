package net.hypejet.jet.server;

import net.hypejet.jet.event.events.packet.PacketSendEvent;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.kyori.adventure.key.Key;

/**
 * Represents a main class, which provides an instruction for when the program starts executing.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetServerEntrypoint {

    private JetServerEntrypoint() {}

    /**
     * Runs the {@linkplain JetMinecraftServer Jet Minecraft server}.
     *
     * @param args the program arguments
     * @since 1.0
     */
    public static void main(String[] args) {
        JetMinecraftServer server = new JetMinecraftServer();
        server.eventNode().addListener(event -> {
            if (event.getPacket() instanceof ServerRegistryDataConfigurationPacket packet)
                if (packet.registry().equals(Key.key("worldgen/biome")))
                    System.out.println(packet);
        }, PacketSendEvent.class);
        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(server::shutdown));
    }
}