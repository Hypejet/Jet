package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain ServerPacket a server packet}, which updates
 * data of {@linkplain net.hypejet.jet.world.block.entity.BlockEntity a block entity}
 * at {@linkplain BlockPosition a block position} specified.
 *
 * @param position the block position
 * @param blockEntityTypeIdentifier an identifier of type of the block entity
 * @param data data of the block entity, represented as a compound binary tag, {@code null} to remove the block entity
 * @since 1.0
 * @see net.hypejet.jet.world.block.entity.BlockEntity
 * @see ServerPacket
 */
public record ServerUpdateBlockEntityPlayPacket(@NonNull BlockPosition position, int blockEntityTypeIdentifier,
                                                @Nullable CompoundBinaryTag data) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateBlockEntityPlayPacket server update block entity play packet}.
     *
     * @param position the block position
     * @param blockEntityTypeIdentifier an identifier of type of the block entity
     * @param data data of the block entity, represented as a compound binary tag, {@code null} to remove
     *             the block entity
     * @since 1.0
     */
    public ServerUpdateBlockEntityPlayPacket {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(data, "data");
    }
}