package net.hypejet.jet.server.network.packet.packets.client.play;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.coordinate.flag.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client when
 * {@linkplain net.hypejet.jet.server.entity.player.JetPlayer a player} associated with it change their rotation.
 *
 * @param yaw an absolute rotation on the {@code X} axis, in degrees
 * @param pitch an absolute rotation on the {@code Y} axis, in degrees
 * @param flags flags of the new position
 * @since 1.0
 * @see ClientPacket
 */
public record ClientRotationPlayPacket(float yaw, float pitch, @NonNull Collection<PositionFlag> flags)
        implements ClientPacket {
    /**
     * Constructs the {@link ClientRotationPlayPacket rotation play packet}.
     *
     * @param yaw an absolute rotation on the {@code X} axis, in degrees
     * @param pitch an absolute rotation on the {@code Y} axis, in degrees
     * @param flags flags of the new position
     * @since 1.0
     */
    public ClientRotationPlayPacket {
        flags = Set.copyOf(Objects.requireNonNull(flags, "flags"));
    }
}