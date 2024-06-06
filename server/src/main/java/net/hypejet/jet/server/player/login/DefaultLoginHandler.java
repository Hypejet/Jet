package net.hypejet.jet.server.player.login;

import net.hypejet.jet.player.PlayerConnection;
import net.hypejet.jet.player.login.LoginHandler;
import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;

/**
 * Represents a {@linkplain LoginHandler login handler}, which logs in a player with data based on
 * a {@linkplain ClientLoginRequestLoginPacket login request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see LoginHandler
 */
public final class DefaultLoginHandler implements LoginHandler {
    @Override
    public void onPacket(@NonNull ClientLoginPacket packet, @NonNull PlayerConnection connection) {
        switch (packet) {
            case ClientLoginRequestLoginPacket requestPacket -> connection.sendPacket(new ServerLoginSuccessLoginPacket(
                    requestPacket.uniqueId(),
                    requestPacket.username(),
                    Collections.emptySet(),
                    true
            ));
            case ClientEncryptionResponseLoginPacket ignored -> throw new IllegalArgumentException("Client sent an" +
                    "encryption response packet while server did not request it");
            default -> {}
        }
    }
}