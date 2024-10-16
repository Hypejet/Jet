package net.hypejet.jet.server.util;

import net.hypejet.jet.ping.ServerListPing;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents an utility foR managing {@linkplain net.hypejet.jet.ping.ServerListPing server list ping}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.ping.ServerListPing
 */
public final class ServerPingUtil {

    private static final String SERVER_ICON_FILE_NAME = "server-icon.png";

    private static final int SERVER_ICON_WIDTH = 64;
    private static final int SERVER_ICON_HEIGHT = 64;

    private ServerPingUtil() {}

    /**
     * Loads a {@linkplain ServerListPing.Favicon server list ping favicon} of the server.
     *
     * @param logger a logger to log errors to
     * @return the icon
     * @since 1.0
     */
    public static ServerListPing.@Nullable Favicon loadServerIcon(@NonNull Logger logger) {
        File serverIconFile = new File(SERVER_ICON_FILE_NAME);
        if (!serverIconFile.exists()) return null;

        try {
            BufferedImage image = ImageIO.read(serverIconFile);

            if (image == null) {
                logger.error("The server icon could not be loaded properly");
                return null;
            }

            if (image.getWidth() == SERVER_ICON_WIDTH && image.getHeight() == SERVER_ICON_HEIGHT) {
                return ServerListPing.createFavicon(image);
            } else {
                logger.warn("The server icon is not an image with dimensions of {}x{}", SERVER_ICON_WIDTH,
                        SERVER_ICON_HEIGHT);
            }
        } catch (IOException exception) {
            logger.error("An error occurred while loading a server icon", exception);
        }

        return null;
    }
}