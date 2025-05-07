package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which updates
 * {@linkplain net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState a block state}
 * at {@linkplain BlockPosition a block position} specified.
 *
 * @param position the block position
 * @param blockStateIdentifier an identifier of a new block state which should be at the block position
 * @since 1.0
 */
public record ServerUpdateBlockStatePlayPacket(@NonNull BlockPosition position, int blockStateIdentifier)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateBlockStatePlayPacket server update block state play packet}.
     *
     * @param position the block position
     * @param blockStateIdentifier an identifier of a new block state which should be at the block position
     * @since 1.0
     */
    public ServerUpdateBlockStatePlayPacket {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(blockStateIdentifier, "block state identifier");
    }
}