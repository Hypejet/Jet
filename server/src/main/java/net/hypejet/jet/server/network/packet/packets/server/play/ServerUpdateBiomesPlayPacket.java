package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} updating {@linkplain Biome biomes}
 * of {@linkplain net.hypejet.jet.world.chunk.Chunk chunks}.
 * 
 * @param data a collection of new biome data of chunks, whose biomes are updated
 * @since 1.0
 * @see ServerPacket
 */
public record ServerUpdateBiomesPlayPacket(@NonNull Collection<BiomeData> data) implements ServerPacket {
    /**
     * A {@linkplain Biome biome} data that a {@linkplain net.hypejet.jet.world.chunk.Chunk chunk}
     * with the specified {@linkplain ChunkPosition chunk position} should have.
     *
     * @param position the chunk position that the biome data belongs to
     * @param palettes a chunk-section-like ordered list of chunk palettes containing biome data
     *                 that corresponding chunk sections should have in the chunk associated with this biome data
     * @since 1.0
     * @see Biome
     */
    public record BiomeData(@NonNull ChunkPosition position,
                            @NonNull List<AbstractChunkPalette<Holder.Reference<Biome>>> palettes) {
        /**
         * Constructs the {@linkplain BiomeData biome data}.
         *
         * @param position the chunk position that the biome data is being created for
         * @param palettes a chunk-section-like ordered list of chunk palettes containing biome data
         *                 that corresponding chunk sections should have in the specified chunk
         * @since 1.0
         */
        public BiomeData {
            Objects.requireNonNull(position, "position");
            Objects.requireNonNull(palettes, "palettes");
        }
    }
}