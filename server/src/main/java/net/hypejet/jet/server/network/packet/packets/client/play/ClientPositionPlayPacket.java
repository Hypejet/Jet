package net.hypejet.jet.server.network.packet.packets.client.play;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.coordinate.flag.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client when
 * {@linkplain net.hypejet.jet.server.entity.player.JetPlayer a player} associated with it move and the rotation
 * does not change.
 *
 * @param vector a new position that a player associated with the client should have, represented as a vector
 * @param flags flags of the new position
 * @since 1.0
 * @see ClientPacket
 */
public record ClientPositionPlayPacket(@NonNull Vector vector, @NonNull Collection<PositionFlag> flags)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientPositionPlayPacket client position play packet}.
     *
     * @param vector a new position of that a player associated with the client should have, represented as a vector
     * @param flags flags of the new position
     * @since 1.0
     */
    public ClientPositionPlayPacket {
        Objects.requireNonNull(vector, "vector");
        flags = Set.copyOf(Objects.requireNonNull(flags, "flags"));
    }
}