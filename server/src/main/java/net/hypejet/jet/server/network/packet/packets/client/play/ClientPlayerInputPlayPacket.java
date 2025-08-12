package net.hypejet.jet.server.network.packet.packets.client.play;

import com.google.common.collect.Sets;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * Represents {@linkplain ClientPacket a client packet} sent by a client to indicate current state of moving of
 * {@linkplain net.hypejet.jet.entity.player.Player a player} associated with the client.
 *
 * @param inputFlags flags that define the current state of moving
 * @since 1.0
 */
public record ClientPlayerInputPlayPacket(@NonNull Set<InputFlag> inputFlags) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientPlayerInputPlayPacket client player input play packet}.
     *
     * @param inputFlags flags that define the current state of moving
     * @since 1.0
     */
    public ClientPlayerInputPlayPacket {
        inputFlags = Sets.immutableEnumSet(Objects.requireNonNull(inputFlags, "input flags"));
    }

    /**
     * Represents a flag that define state of moving of {@linkplain net.hypejet.jet.entity.player.Player a player}.
     *
     * @since 1.0
     * @see net.hypejet.jet.entity.player.Player
     */
    public enum InputFlag {
        /**
         * {@linkplain InputFlag An input flag} that indicates that the player is moving forwards.
         *
         * @since 1.0
         */
        MOVING_FORWARD,
        /**
         * {@linkplain InputFlag An input flag} that indicates that the player is moving backwards.
         *
         * @since 1.0
         */
        MOVING_BACKWARD,
        /**
         * {@linkplain InputFlag An input flag} that indicates that the player is moving to the left.
         *
         * @since 1.0
         */
        MOVING_LEFT,
        /**
         * {@linkplain InputFlag An input flag} that indicates that the player is moving to the right.
         *
         * @since 1.0
         */
        MOVING_RIGHT,
        /**
         * {@linkplain InputFlag An input flag} that indicates that the player is jumping.
         *
         * @since 1.0
         */
        JUMPING,
        /**
         * {@linkplain InputFlag An input flag} that indicates that the player is sneaking.
         *
         * @since 1.0
         */
        SNEAKING,
        /**
         * {@linkplain InputFlag An input flag} that indicates that the player is sprinting.
         *
         * @since 1.0
         */
        SPRINTING
    }
}