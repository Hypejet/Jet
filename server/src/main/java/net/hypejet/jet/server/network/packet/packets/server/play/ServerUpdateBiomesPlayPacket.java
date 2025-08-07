package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Represents {@linkplain ServerPacket a server packet}, which updates {@linkplain Biome biomes}
 * of {@linkplain net.hypejet.jet.world.chunk.Chunk chunks}.
 * 
 * @param data a collection of new biome data of chunks, whose biomes are updated
 * @since 1.0
 * @see ServerPacket
 */
public record ServerUpdateBiomesPlayPacket(@NonNull Collection<BiomeData> data) implements ServerPacket {
    /**
     * Represents data of {@linkplain Biome biomes} of {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk}
     * with {@linkplain ChunkPosition a chunk position} specified.
     *
     * @param position the chunk position
     * @param palettes a chunk-section-like ordered list of chunk palettes containing biome data of chunk sections
     *                 associated with them
     * @since 1.0
     * @see Biome
     */
    public record BiomeData(@NonNull ChunkPosition position,
                            @NonNull List<AbstractChunkPalette<RegistryEntry<Biome>>> palettes) {
        /**
         * Constructs the {@linkplain BiomeData biome data}.
         *
         * @param position the chunk position
         * @param palettes a chunk-section-like ordered list of chunk palettes containing biome data of chunk sections
         *                 associated with them
         * @since 1.0
         */
        public BiomeData {
            Objects.requireNonNull(position, "position");
            Objects.requireNonNull(palettes, "palettes");
        }
    }
}