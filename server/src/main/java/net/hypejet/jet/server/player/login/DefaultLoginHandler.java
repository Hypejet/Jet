package net.hypejet.jet.server.player.login;

import net.hypejet.jet.player.PlayerConnection;
import net.hypejet.jet.player.login.LoginHandler;
import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponsePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;

/**
 * Represents a {@linkplain LoginHandler login handler}, which logs in a player with data based on
 * a {@linkplain ClientLoginRequestPacket login request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see LoginHandler
 */
public final class DefaultLoginHandler implements LoginHandler {
    @Override
    public void onPacket(@NonNull ClientLoginPacket packet, @NonNull PlayerConnection connection) {
        switch (packet) {
            case ClientLoginRequestPacket requestPacket -> connection.sendPacket(new ServerLoginSuccessPacket(
                    requestPacket.uniqueId(),
                    requestPacket.username(),
                    Collections.emptySet(),
                    true
            ));
            case ClientEncryptionResponsePacket ignored -> throw new IllegalArgumentException("Client sent an" +
                    "encryption response packet while server did not request it");
            default -> {}
        }
    }
}