package net.hypejet.jet.util.game.ping;

import com.google.gson.JsonObject;
import net.hypejet.jet.util.json.UnmodifiableJsonObject;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
 */
public record ServerListPing(@NonNull Version version, @Nullable Players players, @Nullable Component description,
                             @Nullable Favicon favicon, boolean enforcesSecureChat, boolean previewsChat,
                             @Nullable UnmodifiableJsonObject customData) {
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
    public ServerListPing(@NonNull Version version, @Nullable Players players, @Nullable Component description,
                          @Nullable Favicon favicon, boolean enforcesSecureChat, boolean previewsChat,
                          @Nullable JsonObject customData) {
        this(version, players, description, favicon, enforcesSecureChat, previewsChat,
                customData == null ? null : new UnmodifiableJsonObject(customData));
    }

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
        Objects.requireNonNull(version, "version");
        if (customData != null && customData.object().isEmpty())
            customData = null;
    }

    /**
     * Represents a version data of the server, which is displayed on a server list.
     *
     * @param versionName a name of the Minecraft version that the server runs on
     * @param protocolVersion a Minecraft protocol version, which is supported on the server
     * @since 1.0
     */
    public record Version(@NonNull String versionName, int protocolVersion) {
        /**
         * Constructs the {@linkplain Version version}.
         *
         * @param versionName a name of the Minecraft version that the server runs on
         * @param protocolVersion a Minecraft protocol version, which is supported on the server
         * @since 1.0
         */
        public Version {
            Objects.requireNonNull(versionName, "version name");
        }
    }

    /**
     * Represents a data of players connected to the server, which is displayed on the list.
     *
     * @param max a maximum player count allowed by the server
     * @param online a current number of players connected to the server
     * @param players data of players, which are currently connected to the server
     * @since 1.0
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
            players = List.copyOf(Objects.requireNonNull(players, "players"));
        }
    }

    /**
     * Represents a player connected to the server, which is displayed on the server list.
     *
     * @param name a name of the player
     * @param uniqueId a unique id of the player
     * @since 1.0
     */
    public record PingPlayer(@NonNull String name, @NonNull UUID uniqueId) {
        /**
         * Constructs the {@linkplain PingPlayer ping player}.
         *
         * @param name a name of the player
         * @param uniqueId a unique id of the player
         * @since 1.0
         */
        public PingPlayer {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(uniqueId, "unique identifier");
        }
    }

    /**
     * Represents an icon, which is displayed on the server list.
     *
     * @param image an image of the icon, which is encoded with {@linkplain Base64 base64}
     * @since 1.0
     */
    public record Favicon(@NonNull String image) {
        /**
         * Constructs the {@linkplain Favicon server list ping favicon}.
         *
         * @param image an image of the icon, which is encoded with {@linkplain Base64 base64}
         * @since 1.0
         */
        public Favicon {
            Objects.requireNonNull(image, "image");
            try {
                // Check if the image was encoded with base64 correctly
                Base64.getDecoder().decode(image);
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("The image was not encoded with base64", exception);
            }
        }
    }

    /**
     * Creates {@linkplain Favicon a server list ping favicon} from {@linkplain BufferedImage a buffered image}.
     *
     * @param image the buffered image
     * @return the favicon
     * @since 1.0
     */
    public static @NonNull Favicon createFavicon(@NonNull BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return new Favicon(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        } catch (Throwable throwable) {
            throw new RuntimeException("An error occurred while creating a favicon", throwable);
        }
    }
}