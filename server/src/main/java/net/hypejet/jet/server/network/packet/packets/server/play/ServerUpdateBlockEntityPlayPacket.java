package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet}, which updates data of a block entity of a block
 * at {@linkplain BlockPosition a block position} specified.
 *
 * @param position the block position
 * @param blockEntityTypeIdentifier an identifier of type of the block entity
 * @param data a new data of the block entity, represented as a compound binary tag
 * @since 1.0
 * @see ServerPacket
 */
public record ServerUpdateBlockEntityPlayPacket(@NonNull BlockPosition position, int blockEntityTypeIdentifier,
                                                @NonNull CompoundBinaryTag data) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateBlockEntityPlayPacket server update block entity play packet}.
     *
     * @param position the block position
     * @param blockEntityTypeIdentifier an identifier of type of the block entity
     * @param data a new data of the block entity, represented as a compound binary tag
     * @since 1.0
     */
    public ServerUpdateBlockEntityPlayPacket {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(data, "data");
    }
}