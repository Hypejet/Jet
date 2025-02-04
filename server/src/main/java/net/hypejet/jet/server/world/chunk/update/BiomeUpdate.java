package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.relative.ChunkRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of {@linkplain Biome a biome} that should be made
 * in {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunk} on a coordinate with values specified.
 *
 * @param position a position where the change should be made
 * @param biome a registry entry of a new biome that should be put on the coordinate with values specified
 * @since 1.0
 */
public record BiomeUpdate(@NonNull ChunkRelativePosition position, @NonNull JetRegistryEntry<Biome> biome) {
    /**
     * Constructs the {@linkplain BiomeUpdate biome update}.
     *
     * @param position a position where the change should be made
     * @param biome a registry entry of a new biome that should be put on the coordinate with values specified
     * @since 1.0
     */
    public BiomeUpdate {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(biome, "biome");

        if (position.paletteType() != ChunkPaletteType.BIOME)
            throw new IllegalArgumentException("The position has not been made for biome chunk palettes");
    }
}