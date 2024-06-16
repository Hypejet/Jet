package net.hypejet.jet.protocol.connection.login;

import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that handles Minecraft login phase.
 *
 * @since 1.0
 * @author Codestech
 */
public interface LoginHandler {
    /**
     * Called when a client sends a {@linkplain ClientLoginPacket login packet}.
     *
     * @param packet the packet
     * @param connection a connection from which the packet was received
     * @since 1.0
     * @see ClientLoginPacket
     */
    void onPacket(@NonNull ClientLoginPacket packet, @NonNull PlayerConnection connection);
}