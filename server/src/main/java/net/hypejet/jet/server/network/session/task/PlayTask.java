package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.registry.session.RegistryTagUpdateFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain SessionTask a session task}, which handles
 * {@linkplain ProtocolState#PLAY a play protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see SessionTask
 */
public final class PlayTask implements SessionTask, RegistryTagUpdateFunction {

    private static final Component NOT_IMPLEMENTED_DISCONNECTION_MESSAGE = Component.text(
            "The play session has been not implemented yet.", NamedTextColor.RED
    );

    private final JetPlayer player;

    /**
     * Constructs the {@linkplain PlayTask play task}.
     *
     * @param player a player that the session task should be handled for
     * @since 1.0
     */
    public PlayTask(@NonNull JetPlayer player) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
    }

    @Override
    public void start() {
        this.player.disconnect(NOT_IMPLEMENTED_DISCONNECTION_MESSAGE);
    }

    @Override
    public void handleDisconnection() {
        // NOOP
    }

    @Override
    public void updateTags(@NonNull Runnable tagUpdateTask) {
        tagUpdateTask.run();
    }

    /**
     * Gets {@linkplain JetPlayer a player} that the task is handled for.
     *
     * @return the player
     * @since 1.0
     */
    public @NonNull JetPlayer player() {
        return this.player;
    }
}