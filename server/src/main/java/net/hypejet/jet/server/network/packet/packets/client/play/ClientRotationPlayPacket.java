package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.coordinate.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is received when a rotation is changed on a client
 * on client.
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
        flags = Set.copyOf(NullabilityUtil.requireNonNull(flags, "flags"));
    }
}