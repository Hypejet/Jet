package net.hypejet.jet.ping;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Minecraft server list ping data.
 *
 * @param version a version data of the server, which is displayed on the list
 * @param players a data of players connected to the server, which is displayed on the list
 * @param description a server description, which is displayed on the list
 * @param favicon an icon, which is displayed on the list
 * @param enforcesSecureChat whether the server forces a client to send secure chat signature on join
 * @param previewsChat whether the server requires a client to preview a message, which is being typed
 * @param customData an additional data, which will be appended to the server ping data
 * @since 1.0
 * @author Codestech
 */
public record ServerListPing(@NonNull Version version, @NonNull Players players, @NonNull Component description,
                             @NonNull BufferedImage favicon, boolean enforcesSecureChat, boolean previewsChat,
                             @NonNull JsonObject customData) {
    /**
     * Represents a version data of the server, which is displayed on a server list.
     *
     * @param versionName a name of the Minecraft version that the server runs on
     * @param protocolVersion a Minecraft protocol version, which is supported on the server
     * @since 1.0
     * @author Codestech
     */
    public record Version(@NonNull String versionName, int protocolVersion) {}

    /**
     * Represents a data of players connected to the server, which is displayed on the list.
     *
     * @param max a maximum player count allowed by the server
     * @param online a current number of players connected to the server
     * @param players data of players, which are currently connected to the server
     * @since 1.0
     * @author Codestech
     */
    public record Players(int max, int online, @NonNull Collection<PingPlayer> players) {
        public Players {
            players = List.copyOf(players);
        }
    }

    /**
     * Represents a player connected to the server, which is displayed on the server list.
     *
     * @param name a name of the player
     * @param uniqueId an unique id of the player
     * @since 1.0
     */
    public record PingPlayer(@NonNull String name, @NonNull UUID uniqueId) {}
}