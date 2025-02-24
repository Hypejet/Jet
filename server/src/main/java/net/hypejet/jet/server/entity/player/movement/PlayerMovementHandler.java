package net.hypejet.jet.server.entity.player.movement;

import net.hypejet.concurrency.object.nullable.NullableObjectAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.object.nullable.WriteNullableObjectAcquisition;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.coordinate.Vector;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.movement.flag.RelativeFlag;
import net.hypejet.jet.server.entity.movement.acquisition.InternalWriteMovementAcquisition;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientConfirmMovementSynchronizationPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizePositionPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * Represents something that synchronizes {@linkplain Position a position} and {@linkplain Vector vector}
 * of a delta movement of {@linkplain JetPlayer a player} with a client associated with it.
 *
 * @since 1.0
 * @see Position
 * @see Vector
 * @see JetPlayer
 */
public final class PlayerMovementHandler {

    private final JetPlayer player;
    private final NullableObjectAcquirable<Synchronization> synchronization = new NullableObjectAcquirable<>();

    /**
     * Constructs the {@linkplain PlayerMovementHandler player movement handler}.
     *
     * @param player a player that the position and delta movement synchronization should be handled for
     * @since 1.0
     */
    public PlayerMovementHandler(@NonNull JetPlayer player) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
    }

    /**
     * Sends a request to client to synchronize clientside {@linkplain Position position}
     * and {@linkplain Vector vector} of a delta movement with a value specified.
     *
     * @param position the value
     * @since 1.0
     */
    public void synchronize(@NonNull Position position, @NonNull Vector deltaMovement,
                            @NonNull Collection<RelativeFlag> flags) {
        NullabilityUtil.requireNonNull(position, "position");
        try (WriteNullableObjectAcquisition<Synchronization> acquisition = this.synchronization.acquireWrite()) {
            Synchronization previousSynchronization = acquisition.get();
            int identifier = previousSynchronization == null ? 0 : previousSynchronization.identifier() + 1;

            acquisition.set(new Synchronization(identifier, position));

            this.player.sendPacket(new ServerSynchronizePositionPlayPacket(
                    identifier, position, deltaMovement, flags
            ));
        }
    }

    /**
     * Handles a client confirmation to synchronization of {@linkplain Position a position}
     * and {@linkplain Vector vector} of a delta movement.
     *
     * @param packet a packet that the client sent to confirm the synchronization
     * @since 1.0
     */
    public void handleConfirmation(@NonNull ClientConfirmMovementSynchronizationPlayPacket packet) {
        NullabilityUtil.requireNonNull(packet, "packet");
        try (
                WriteNullableObjectAcquisition<Synchronization> acquisition = this.synchronization.acquireWrite();
                InternalWriteMovementAcquisition positionAcquisition = this.player.acquireMovementWrite()
        ) {
            Synchronization synchronization = acquisition.get();
            if (synchronization == null || synchronization.identifier() != packet.identifier()) return;
            positionAcquisition.setPosition(synchronization.position());
            acquisition.set(null);
        }
    }

    /**
     * Handles a clientside change of {@linkplain Position a position} of the {@linkplain JetPlayer player}.
     *
     * @param positionUnaryOperator a unary operator that provides a new position of the player by accepting their
     *                              current position
     * @since 1.0
     */
    public void handleClientMovement(@NonNull UnaryOperator<Position> positionUnaryOperator) {
        NullabilityUtil.requireNonNull(positionUnaryOperator, "position unary operator");
        try (
                NullableObjectAcquisition<Synchronization> acquisition = this.synchronization.acquireRead();
                InternalWriteMovementAcquisition positionAcquisition = this.player.acquireMovementWrite()
        ) {
            if (acquisition.get() != null) return;
            Position newPosition = positionUnaryOperator.apply(positionAcquisition.position());
            positionAcquisition.setPosition(newPosition);
        }
    }


    /**
     * Represents a pending synchronization request of {@linkplain Position a position}
     * of {@linkplain JetPlayer a player} with a client associated with it.
     *
     * @param identifier an identifier of the synchronization
     * @param position a value that clientside position should be set to
     * @since 1.0
     * @see Position
     * @see JetPlayer
     */
    private record Synchronization(int identifier, @NonNull Position position) {
        /**
         * Constructs the {@linkplain Synchronization synchronization}.
         *
         * @param identifier an identifier of the synchronization
         * @param position a value that clientside position should be set to
         * @since 1.0
         */
        private Synchronization {
            NullabilityUtil.requireNonNull(position, "position");
        }
    }
}