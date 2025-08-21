package net.hypejet.jet.server.entity.player.movement;

import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientConfirmMovementSynchronizationPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Something synchronizing {@linkplain Position position} and velocity {@linkplain Vector vector}
 * of a {@linkplain JetPlayer player} with the client associated with that player.
 *
 * @since 1.0
 * @see Position
 * @see Vector
 * @see JetPlayer
 */
// TODO: Make this something async?
public final class PlayerMovementSynchronizer {

    private final JetPlayer player;

    private int synchronizationId;
    // Currently unused, but we will need that when we implement vanilla-like client movement handling
    private int synchronizationTickEpoch;
    private Vector positionInSynchronization;

    private int tickCount; // Same as above, currently unused, but we will need that

    /**
     * Constructs the {@linkplain PlayerMovementSynchronizer player movement synchronizer}.
     *
     * @param player the player that the synchronization should be handled for
     * @since 1.0
     */
    public PlayerMovementSynchronizer(@NonNull JetPlayer player) {
        this.player = Objects.requireNonNull(player, "player");
    }

    /**
     * Sends a request to the client to synchronize clientside {@linkplain Position position}
     * and velocity {@linkplain Vector vector} with the specified values.
     *
     * @param position the position value that the clientside position should be synchronized with
     * @param velocity the velocity vector that the clientside velocity should be synchronized with
     * @param flags relative flags of the specified position and velocity,
     *              indicate that a value is relative to the previous one
     * @since 1.0
     */
    public void synchronize(@NonNull Position position, @NonNull Vector velocity,
                            @NonNull Collection<RelativeFlag> flags) {
        this.synchronizationTickEpoch = this.tickCount;

        if (this.synchronizationId == Integer.MAX_VALUE) {
            this.synchronizationId = 0;
        } else {
            this.synchronizationId++;
        }

        this.positionInSynchronization = new Vector(position.x(), position.y(), position.z());
        this.player.updateRawPositionAndVelocity(position, velocity, flags);

        this.player.sendPacket(new ServerSynchronizePositionPlayPacket(
                this.synchronizationId,
                position,
                velocity,
                flags
        ));
    }

    /**
     * Handles a client confirmation to a synchronization request
     * made by this {@linkplain PlayerMovementSynchronizer player movement synchronizer}.
     *
     * @param packet the packet that the client sent to confirm the synchronization
     * @since 1.0
     */
    public void handleConfirmation(@NonNull ClientConfirmMovementSynchronizationPlayPacket packet) {
        this.player.server().ticker().scheduleTask(() -> {
            if (packet.identifier() != this.synchronizationId) return;
            if (this.positionInSynchronization == null) return; // We are more lenient than vanilla
            this.player.updateRawPosition(this.player.position().withValues(this.positionInSynchronization));
            this.positionInSynchronization = null;
        });
    }

    /**
     * Handles a clientside change of a {@linkplain Position position} of the {@linkplain JetPlayer player}.
     *
     * @param positionUnaryOperator a unary operator that provides a new position
     *                              of the player by consuming their current position
     * @since 1.0
     */
    public void handleClientMovement(@NonNull UnaryOperator<Position> positionUnaryOperator) {
        this.player.server().ticker().scheduleTask(() -> {
            /* TODO: Make a vanilla-like implementation, currently we blindly trust the client,
               but entity system is not completed yet, therefore it is impossible to make
               a vanilla-like implementation for now. */
            this.player.updateRawPosition(positionUnaryOperator.apply(this.player.position()));
        });
    }
}