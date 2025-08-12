package net.hypejet.jet.server.network.packet.packets.server.play;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.server.world.coordinate.chunk.section.ChunkSectionPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} updating {@linkplain JetBlockState block states}
 * in a {@linkplain JetChunkSection chunk section}.
 *
 * @param position an absolute position of the chunk section that the block states should be updated in
 * @param updates a map associating chunk-palette-relative positions with registry indices of block states
 *                that should be present at these positions
 * @since 1.0
 * @see JetBlockState
 * @see JetChunkSection
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
     * @param updates a map associating chunk-palette-relative positions with registry indices of block states
     *                that should be present at these positions
     * @since 1.0
     */
    public ServerUpdateChunkSectionBlockStatesPlayPacket {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(updates, "updates");
        updates = Object2IntMaps.unmodifiable(new Object2IntOpenHashMap<>(updates));
    }
}