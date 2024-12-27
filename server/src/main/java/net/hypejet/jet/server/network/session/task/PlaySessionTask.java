package net.hypejet.jet.server.network.session.task;

import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.registry.session.RegistryTagUpdateFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain SessionTask a session task}, which handles
 * {@linkplain net.hypejet.jet.server.network.ProtocolState#PLAY a play protocol state}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#PLAY
 * @see SessionTask
 */
public final class PlaySessionTask implements SessionTask, RegistryTagUpdateFunction {

    private static final Component NOT_IMPLEMENTED_DISCONNECTION_MESSAGE = Component.text(
            "The play session has been not implemented yet.", NamedTextColor.RED
    );

    private final JetPlayer player;
    private final BooleanAcquirable commandsSent = new BooleanAcquirable();

    /**
     * Constructs the {@linkplain PlaySessionTask play session task}.
     *
     * @param player a player that the session task should be handled for
     * @since 1.0
     */
    public PlaySessionTask(@NonNull JetPlayer player) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
    }

    @Override
    public void start() {
        try (WriteBooleanAcquisition commandsSentAcquisition = this.commandsSent.acquireWrite()) {
            this.player.server().commandManager().sendDeclarationPacket(this.player);
            commandsSentAcquisition.set(true);
        }
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

    /**
     * Updates commands for {@linkplain JetPlayer a player} that the task is handled for.
     *
     * @param packet a packet representing the command update
     * @since 1.0
     */
    public void updateCommands(@NonNull ServerDeclareCommandsPlayPacket packet) {
        try (BooleanAcquisition commandsSentAcquisition = this.commandsSent.acquireRead()) {
            // Commands have not been sent yet, so the update will be taken into account when commands are sent
            if (!commandsSentAcquisition.get()) return;
            this.player.sendPacket(packet);
        }
    }
}