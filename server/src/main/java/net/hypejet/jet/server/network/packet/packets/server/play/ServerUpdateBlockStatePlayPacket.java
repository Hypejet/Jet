package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} updating a {@linkplain JetBlockState block state}
 * at the specified {@linkplain BlockPosition block position}.
 *
 * @param position the block position where the block state should be updated
 * @param blockStateIdentifier an identifier of a new block state that should be
 *                             present at the specified block position
 * @since 1.0
 */
public record ServerUpdateBlockStatePlayPacket(@NonNull BlockPosition position, int blockStateIdentifier)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateBlockStatePlayPacket server update block state play packet}.
     *
     * @param position the block position where the block state should be updated
     * @param blockStateIdentifier an identifier of a new block state that should be
     *                             present at the specified block position
     * @since 1.0
     */
    public ServerUpdateBlockStatePlayPacket {
        Objects.requireNonNull(position, "position");
    }
}