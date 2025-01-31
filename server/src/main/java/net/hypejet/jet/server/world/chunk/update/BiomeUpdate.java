package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of {@linkplain Biome a biome} that should be made
 * in {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunk} on a coordinate with values specified.
 *
 * @param biomeX a section-relative {@code X} coordinate value where the change should be made
 * @param biomeY an absolute {@code Y} coordinate value where the change should be made
 * @param biomeZ a section-relative {@code Z} coordinate value where the change should be made
 * @param biome a registry entry of a new biome that should be put on the coordinate with values specified
 * @since 1.0
 */
public record BiomeUpdate(byte biomeX, short biomeY, byte biomeZ, @NonNull JetRegistryEntry<Biome> biome) {
    /**
     * Constructs the {@linkplain BiomeUpdate biome update}.
     *
     * @param biomeX a section-relative {@code X} coordinate value where the change should be made
     * @param biomeY an absolute {@code Y} coordinate value where the change should be made
     * @param biomeZ a section-relative {@code Z} coordinate value where the change should be made
     * @param biome a registry entry of a new biome that should be put on the coordinate with values specified
     * @since 1.0
     */
    public BiomeUpdate {
        NullabilityUtil.requireNonNull(biome, "biome");
    }
}