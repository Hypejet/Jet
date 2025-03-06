package net.hypejet.jet.session.configuration;

import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.util.game.audience.CommonAudience;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that manages configuration of {@linkplain net.hypejet.jet.entity.player.Player a player}
 * before it is created.
 *
 * <p>Note that only resource pack management methods of {@linkplain Audience an audience} are implemented, however
 * all methods provided by {@linkplain CommonAudience a common audience} itself are available.</p>
 *
 * @since 1.0
 * @see net.hypejet.jet.entity.player.Player
 */
public interface ConfigurationManager extends CommonAudience {
    /**
     * Requests a client associated with the player to remove all chat messages that it has received before joining
     * the server.
     *
     * @since 1.0
     */
    void resetChat();

    /**
     * Gets {@linkplain PlayerConnection a player connection} that is going to be associated with the player.
     *
     * @return the player connection
     * @since 1.0
     */
    @NonNull PlayerConnection connection();
}