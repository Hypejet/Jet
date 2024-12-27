package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.coordinate.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client when it changes their position.
 *
 * @param x an absolute {@code X} value of the new position
 * @param feetY an absolute {@code feet Y} value of the new position
 * @param z an absolute {@code Z} value of the new position
 * @param flags flags of the new position
 * @since 1.0
 * @see ClientPacket
 */
public record ClientPositionPlayPacket(double x, double feetY, double z, @NonNull Collection<PositionFlag> flags)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientPositionPlayPacket client position play packet}.
     *
     * @param x an absolute {@code X} value of the new position
     * @param feetY an absolute {@code feet Y} value of the new position
     * @param z an absolute {@code Z} value of the new position
     * @param flags flags of the new position
     * @since 1.0
     */
    public ClientPositionPlayPacket {
        flags = Set.copyOf(NullabilityUtil.requireNonNull(flags, "flags"));
    }
}