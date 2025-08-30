package net.hypejet.jet.server.util.viewable;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.util.view.Viewable;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

/**
 * An internal complement to {@linkplain Viewable viewable}.
 *
 * @since 1.0
 * @see Viewable
 */
@NullMarked
public interface JetViewable extends Viewable {
    @Override
    Set<JetPlayer> viewers();

    /**
     * Handles the specified {@linkplain JetPlayer player}
     * being removed from the {@linkplain JetMinecraftServer server}.
     *
     * @param player the player that is being removed from the server
     * @since 1.0
     */
    void handleViewerRemoval(JetPlayer player);
}