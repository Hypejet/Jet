package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.protocol.position.PositionFlag;
import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when player changes their position
 * and rotation on client.
 *
 * @param x an absolute {@code X} value of the position
 * @param feetY an absolute feet position of the player, normally head {@code Y} - {@code 1.62}
 * @param z an absolute {@code Z} value of the position
 * @param yaw an absolute rotation on the {@code X} axis, in degrees
 * @param pitch an absolute rotation on the {@code Y} axis, in degrees
 * @param flags flags of a new position of the player
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientRotationAndPositionPlayPacket(double x, double feetY, double z, float yaw, float pitch,
                                                  @NonNull Collection<PositionFlag> flags)
        implements ClientPlayPacket {
    /**
     * Constructs the {@linkplain ClientRotationAndPositionPlayPacket rotation and position play packet}.
     *
     * @param x an absolute {@code X} value of the position
     * @param feetY an absolute feet position of the player, normally head {@code Y} - {@code 1.62}
     * @param z an absolute {@code Z} value of the position
     * @param yaw an absolute rotation on the {@code X} axis, in degrees
     * @param pitch an absolute rotation on the {@code Y} axis, in degrees
     * @param flags flags of a new position of the player
     * @since 1.0
     */
    public ClientRotationAndPositionPlayPacket {
        flags = Set.copyOf(NullabilityUtil.requireNonNull(flags, "flags"));
    }
}