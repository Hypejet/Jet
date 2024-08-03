package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when
 * a {@linkplain net.hypejet.jet.entity.player.Player player} performs an action.
 *
 * @param entityId an identifier of an entity, which taken part in the action, or identifier of the player if none
 * @param action the action
 * @param jumpBoost a horse jump boost of the player, used only if the action
 *                  is {@link Action#START_JUMPING_WITH_HORSE}
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientActionPlayPacket(int entityId, @NonNull Action action, int jumpBoost) implements ClientPlayPacket {
    /**
     * Represents an action performed by a {@linkplain net.hypejet.jet.entity.player.Player player}.
     *
     * @since 1.0
     * @author Codestech
     */
    public enum Action {
        /**
         * An action used when a player starts sneaking.
         *
         * @since 1.0
         */
        START_SNEAKING,
        /**
         * An action used when a player stops sneaking.
         *
         * @since 1.0
         */
        STOP_SNEAKING,
        /**
         * An action used when a player leaves a bed.
         * <p>Only used when a {@code Leave Bed} button is clicked in the GUI.</p>
         *
         * @since 1.0
         */
        LEAVE_BED,
        /**
         * An action used when a player starts sprinting.
         *
         * @since 1.0
         */
        START_SPRINTING,
        /**
         * An action used when a player stops sprinting.
         *
         * @since 1.0
         */
        STOP_SPRINTING,
        /**
         * An action used when a player starts jumping with a horse.
         *
         * @since 1.0
         */
        START_JUMPING_WITH_HORSE,
        /**
         * An action used when a player stops jumping with a horse.
         *
         * @since 1.0
         */
        STOP_JUMPING_WITH_HORSE,
        /**
         * An action used when a player opens an inventory of vehicle that they are riding.
         *
         * @since 1.0
         */
        OPEN_VEHICLE_INVENTORY,
        /**
         * An action used when a player starts flying with an elytra.
         *
         * @since 1.0
         */
        START_FLYING_WITH_ELYTRA
    }
}