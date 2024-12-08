package net.hypejet.jet.event.events.ping;

import net.hypejet.jet.network.PlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when {@linkplain PlayerConnection a player connection} responds
 * to {@linkplain PlayerConnection#??? a ping request}.
 *
 * @param connection a player connection that respond to the ping
 * @param pingIdentifier an identifier of the ping that the player connection respond to
 * @since 1.0
 * @author Codestech
 */
public record PongEvent(@NonNull PlayerConnection connection, int pingIdentifier) {}