package net.hypejet.jet.network.packet.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.client.ClientPacket;
import net.hypejet.jet.world.coordinate.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.EnumSet;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client when it changes position flags
 * of their position.
 *
 * @param flags the new position flags
 * @since 1.0
 * @author Codestech
 * @see PositionFlag
 * @see ClientPacket
 */
public record ClientPositionFlagsPlayPacket(@NonNull Collection<PositionFlag> flags) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientPositionFlagsPlayPacket client position flags play packet}.
     *
     * @param flags the new position flags
     * @since 1.0
     */
    public ClientPositionFlagsPlayPacket {
        flags = EnumSet.copyOf(NullabilityUtil.requireNonNull(flags, "flags"));
    }
}