package net.hypejet.jet.server.network.session.common;

import net.hypejet.jet.entity.player.Player;
import net.kyori.adventure.resource.ResourcePackStatus;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents an interface defining common methods handling packets
 * of {@linkplain net.hypejet.jet.server.network.session.task.SessionTask session tasks}.
 *
 * @since 1.0
 */
public interface CommonSessionPacketHandler {
    /**
     * Handles a deserialized plugin message containing a brand name of a client.
     *
     * @param name the brand name
     * @since 1.0
     */
    void handleClientBrand(@NonNull String name);

    /**
     * Handles an update of clientside settings.
     *
     * @param settings the new settings
     * @since 1.0
     */
    void handleClientInformation(Player.@NonNull Settings settings);

    /**
     * Handles a client response for a keep alive requested by a server.
     *
     * @param keepAliveIdentifier an identifier of the keep alive
     * @since 1.0
     */
    void handleKeepAliveResponse(long keepAliveIdentifier);

    /**
     * Handles a client {@linkplain ResourcePackStatus status} of loading a resource pack.
     *
     * @param uniqueId a unique identifier of the resource pack
     * @param status the status
     * @since 1.0
     */
    void handleResourcePackStatus(@NonNull UUID uniqueId, @NonNull ResourcePackStatus status);
}