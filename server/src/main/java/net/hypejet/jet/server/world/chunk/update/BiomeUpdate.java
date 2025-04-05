package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of {@linkplain Biome a biome} that should be made
 * at {@linkplain ChunkRelativeBiomePosition a chunk-relative biome position} in some {@linkplain JetChunk chunk}.
 *
 * @param position a position where the change should be made
 * @param biome a registry entry of a new biome that should be put on the coordinate with values specified
 * @since 1.0
 */
public record BiomeUpdate(@NonNull ChunkRelativeBiomePosition position, @NonNull JetRegistryEntry<Biome> biome) {
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
    }
}