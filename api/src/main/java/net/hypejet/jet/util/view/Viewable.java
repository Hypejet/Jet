package net.hypejet.jet.util.view;

import net.hypejet.jet.entity.player.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

/**
 * Something that can be viewed by a {@linkplain Player player}.
 *
 * @since 1.0
 * @see Player
 */
@NullMarked
public interface Viewable {
    /**
     * Gets a copy of a {@linkplain Set set} of {@linkplain Player players} that are currently able to see this object.
     *
     * @return the viewer set copy
     * @since 1.0
     */
    Set<? extends Player> viewers();

    /**
     * Makes the specified {@linkplain Player player} be able to see this object.
     *
     * @param player the player that should be able to see this object
     * @return {@code true} if the specified player was made be able to see this object,
     *         {@code false} if it has already been able to see the object
     * @since 1.0
     */
    boolean addViewer(Player player);

    /**
     * Makes the specified {@linkplain Player player} not be able to see this object.
     *
     * @param player the player that should be not able to see this object
     * @return {@code true} if the specified player was made not be able to see this object,
     *         {@code false} if it has already not been able to see the object
     * @since 1.0
     */
    boolean removeViewer(Player player);
}