package net.hypejet.jet.server;

import net.hypejet.jet.event.events.login.LoginSessionInitializeEvent;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.session.LoginSession;
import net.hypejet.jet.session.handler.LoginSessionHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.UUID;

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

        server.eventNode().addListener(event -> event.setSessionHandler(new LoginSessionHandler() {
            @Override
            public void onLoginRequest(@NonNull ClientLoginRequestLoginPacket packet, @NonNull LoginSession session) {
                session.finish(packet.username(), UUID.randomUUID(), List.of());
            }
        }), LoginSessionInitializeEvent.class);
        server.eventNode().addListener(System.out::println, Object.class);

        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(server::shutdown));
    }
}