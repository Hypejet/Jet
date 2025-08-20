package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Represents {@linkplain ServerPacket a server packet}, which synchronizes position of a player on a client.
 *
 * @param identifier an identifier of the position synchronization
 * @param position a value that a clientside position should be set to
 * @param deltaMovement a value that clientside delta movement should be set to
 * @param relativeFlags relative flags, which indicate whether a specific value of the position or the delta movement
 *                      is relative to a previous position or delta movement
 * @since 1.0
 */
public record ServerSynchronizePositionPlayPacket(
        int identifier, @NonNull Position position, @NonNull Vector deltaMovement,
        @NonNull Collection<RelativeFlag> relativeFlags
) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerSynchronizePositionPlayPacket server synchronize position play packet}.
     *
     * @param identifier an identifier of the position synchronization
     * @param position a value that a clientside position should be set to
     * @param deltaMovement a value that clientside delta movement should be set to
     * @param relativeFlags relative flags, which indicate whether a specific value of the position
     *                      or the delta movement is relative to a previous position or delta movement
     * @since 1.0
     */
    public ServerSynchronizePositionPlayPacket {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(deltaMovement, "delta movement");
        relativeFlags = Set.copyOf(Objects.requireNonNull(relativeFlags, "relative flags"));
    }
}