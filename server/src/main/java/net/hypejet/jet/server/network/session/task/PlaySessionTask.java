package net.hypejet.jet.server.network.session.task;

import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.registry.function.RegistryTagUpdateFunction;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain SessionTask a session task}, which handles
 * {@linkplain net.hypejet.jet.server.network.ProtocolState#PLAY a play protocol state}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#PLAY
 * @see SessionTask
 */
public final class PlaySessionTask implements SessionTask, RegistryTagUpdateFunction {

    private final JetPlayer player;

    private final World spawningWorld;
    private final Position spawningPosition;

    private final Player.GameMode previousGameMode;
    private final Player.GameMode gameMode;

    private final boolean enableRespawnScreen;

    private final BooleanAcquirable commandsSent = new BooleanAcquirable();

    /**
     * Constructs the {@linkplain PlaySessionTask play session task}.
     *
     * @param player a player that the session task should be handled for
     * @param spawningWorld a world that the player should spawn in
     * @param spawningPosition a position where the player should spawn at
     * @param previousGameMode a game mode that the player had before spawning, {@code null} if none
     * @param gameMode an initial game mode that the player should have
     * @param enableRespawnScreen whether a respawn screen should be enabled for the player
     * @since 1.0
     */
    public PlaySessionTask(@NonNull JetPlayer player, @NonNull JetWorld spawningWorld,
                           @NonNull Position spawningPosition, Player.@Nullable GameMode previousGameMode,
                           Player.@NonNull GameMode gameMode, boolean enableRespawnScreen) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
        this.spawningWorld = NullabilityUtil.requireNonNull(spawningWorld, "spawning world");
        this.spawningPosition = NullabilityUtil.requireNonNull(spawningPosition, "spawning position");
        this.previousGameMode = previousGameMode;
        this.gameMode = NullabilityUtil.requireNonNull(gameMode, "game mode");
        this.enableRespawnScreen = enableRespawnScreen;
    }

    @Override
    public void start() {
        try (WriteBooleanAcquisition commandsSentAcquisition = this.commandsSent.acquireWrite()) {
            this.player.server().commandManager().sendDeclarationPacket(this.player);
            commandsSentAcquisition.set(true);
        }

        this.player.initializeGameMode(this.previousGameMode, this.gameMode);
        this.player.initializeRespawnScreenEnabled(this.enableRespawnScreen);

        this.player.setWorld(this.spawningWorld, this.spawningPosition);
    }

    @Override
    public void handleDisconnection() {
        // NOOP
    }

    @Override
    public void updateTags(@NonNull ServerUpdateTagsPacket packet) {
        this.player.sendPacket(packet);
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