package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import net.hypejet.jet.protocol.position.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when player changes their rotation
 * on client.
 *
 * @param yaw an absolute rotation on the {@code X} axis, in degrees
 * @param pitch an absolute rotation on the {@code Y} axis, in degrees
 * @param flags flags of a new position of the player
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientRotationPlayPacket(float yaw, float pitch, @NonNull Collection<PositionFlag> flags)
        implements ClientPlayPacket {
    /**
     * Constructs the {@link ClientRotationPlayPacket rotation play packet}.
     *
     * @param yaw an absolute rotation on the {@code X} axis, in degrees
     * @param pitch an absolute rotation on the {@code Y} axis, in degrees
     * @param flags flags of a new position of the player
     * @since 1.0
     */
    public ClientRotationPlayPacket {
        flags = Set.copyOf(NullabilityUtil.requireNonNull(flags, "flags"));
    }
}