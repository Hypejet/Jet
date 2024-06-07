package net.hypejet.jet.ping;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
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
 * @see net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket
 */
public record ServerListPing(@NonNull Version version, @Nullable Players players, @Nullable Component description,
                             @Nullable Favicon favicon, boolean enforcesSecureChat, boolean previewsChat,
                             @Nullable JsonObject customData) {

    private static final String FAVICON_FORMAT_NAME = "png";

    /**
     * Constructs the {@linkplain ServerListPing server list ping}.
     *
     * @param version a version data of the server, which is displayed on the list
     * @param players a data of players connected to the server, which is displayed on the list
     * @param description a server description, which is displayed on the list
     * @param favicon an icon, which is displayed on the list
     * @param enforcesSecureChat whether the server forces a client to send secure chat signature on join
     * @param previewsChat whether the server requires a client to preview a message, which is being typed
     * @param customData an additional data, which will be appended to the server ping data
     * @since 1.0
     */
    public ServerListPing {
        if (customData != null && customData.isEmpty()) customData = null;
    }

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
        /**
         * Constructs the {@linkplain Players players}.
         *
         * @param max a maximum player count allowed by the server
         * @param online a current number of players connected to the server
         * @param players data of players, which are currently connected to the server
         * @since 1.0
         */
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

    /**
     * Represents an icon, which is displayed on the server list.
     *
     * @param image an image of the icon, which is encoded with {@linkplain Base64 base64}
     * @since 1.0
     * @author Codestech
     * @see ServerListPing#createFavicon(BufferedImage)
     */
    public record Favicon(@NonNull String image) {
        /**
         * Constructs the {@linkplain Favicon server list ping favicon}.
         *
         * @param image an image of the icon, which is encoded with {@linkplain Base64 base64}
         * @since 1.0
         */
        public Favicon {
            try {
                // Check if the image was encoded with base64 correctly
                Base64.getDecoder().decode(image);
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("The image was not encoded with base64", exception);
            }
        }
    }

    /**
     * Creates a {@linkplain Favicon server list ping favicon} from a {@linkplain BufferedImage buffered image}.
     *
     * @param image the buffered image
     * @return the favicon
     * @since 1.0
     */
    public static @NonNull Favicon createFavicon(@NonNull BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, FAVICON_FORMAT_NAME, outputStream);
            return new Favicon(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a image format name used by a {@linkplain Favicon server list ping favicon}.
     *
     * @return the image format
     * @since 1.0
     */
    public static @NonNull String faviconFormatName() {
        return FAVICON_FORMAT_NAME;
    }
}