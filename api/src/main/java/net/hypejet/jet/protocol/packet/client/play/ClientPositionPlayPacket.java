package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.protocol.position.PositionFlag;
import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when player changes their position
 * on client.
 *
 * @param x an absolute {@code X} value of the position
 * @param feetY an absolute feet position of the player, normally head {@code Y} - {@code 1.62}
 * @param z an absolute {@code Z} value of the position
 * @param flags flags of a new position of the player
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientPositionPlayPacket(double x, double feetY, double z, @NonNull Collection<PositionFlag> flags)
        implements ClientPlayPacket {
    /**
     * Constructs the {@linkplain ClientPositionPlayPacket position play packet}.
     *
     * @param x an absolute {@code X} value of the position
     * @param feetY an absolute feet position of the player, normally head {@code Y} - {@code 1.62}
     * @param z an absolute {@code Z} value of the position
     * @param flags flags of a new position of the player
     * @since 1.0
     */
    public ClientPositionPlayPacket {
        flags = Set.copyOf(NullabilityUtil.requireNonNull(flags, "flags"));
    }
}