package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.coordinate.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.EnumSet;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is received when a position and rotation is changed
 * on a client.
 *
 * @param x an absolute {@code X} value of the new position
 * @param feetY an absolute {@code feet Y} value of the new position
 * @param z an absolute {@code Z} value of the new position
 * @param yaw an absolute rotation on the {@code X} axis, in degrees
 * @param pitch an absolute rotation on the {@code Y} axis, in degrees
 * @param flags flags of the new position
 * @since 1.0
 * @see ClientPacket
 */
public record ClientRotationAndPositionPlayPacket(double x, double feetY, double z, float yaw, float pitch,
                                                  @NonNull Collection<PositionFlag> flags) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientRotationAndPositionPlayPacket rotation and position play packet}.
     *
     * @param x an absolute {@code X} value of the position
     * @param feetY an absolute {@code feet Y} value of the new position
     * @param z an absolute {@code Z} value of the position
     * @param yaw an absolute rotation on the {@code X} axis, in degrees
     * @param pitch an absolute rotation on the {@code Y} axis, in degrees
     * @param flags flags of the new position
     * @since 1.0
     */
    public ClientRotationAndPositionPlayPacket {
        flags = EnumSet.copyOf(NullabilityUtil.requireNonNull(flags, "flags"));
    }
}