package net.hypejet.jet.server.network.packet.packets.server.play;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.server.world.coordinate.chunk.section.ChunkSectionPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which updates
 * {@linkplain net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState block states}
 * of {@linkplain net.hypejet.jet.world.chunk.section.ChunkSection a chunk section}.
 *
 * @param position an absolute position of the chunk section that the block states should be updated for
 * @param updates a map, which maps chunk-palette-relative positions to block state identifiers, which should
 *                be at these positions
 * @since 1.0
 * @see net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState
 * @see net.hypejet.jet.world.chunk.section.ChunkSection
 * @see ServerPacket
 */
public record ServerUpdateChunkSectionBlockStatesPlayPacket(
        @NonNull ChunkSectionPosition position,
        @NonNull Object2IntMap<ChunkPaletteRelativePosition> updates
) implements ServerPacket {
    /**
     * Constructs the
     * {@linkplain ServerUpdateChunkSectionBlockStatesPlayPacket server update chunk-section block states play packet}.
     *
     * @param position an absolute position of the chunk section that the block states should be updated for
     * @param updates a map, which maps chunk-palette-relative positions to block state identifiers, which should
     *                be at these positions
     * @since 1.0
     */
    public ServerUpdateChunkSectionBlockStatesPlayPacket {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(updates, "updates");
        updates = Object2IntMaps.unmodifiable(new Object2IntOpenHashMap<>(updates));
    }
}