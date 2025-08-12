package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.flag.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client when
 * {@linkplain net.hypejet.jet.server.entity.player.JetPlayer a player} associated with it move
 * and change their rotation.
 *
 * @param position a new position that a player associated with the client should have
 * @param flags flags of the new position
 * @since 1.0
 * @see ClientPacket
 */
public record ClientRotationAndPositionPlayPacket(@NonNull Position position, @NonNull Collection<PositionFlag> flags)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientRotationAndPositionPlayPacket rotation and position play packet}.
     *
     * @param position a new position that a player associated with the client should have
     * @param flags flags of the new position
     * @since 1.0
     */
    public ClientRotationAndPositionPlayPacket {
        Objects.requireNonNull(position, "position");
        flags = Set.copyOf(Objects.requireNonNull(flags, "flags"));
    }
}