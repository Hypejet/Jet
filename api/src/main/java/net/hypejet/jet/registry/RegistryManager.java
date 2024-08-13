package net.hypejet.jet.registry;

import net.hypejet.jet.data.biome.Biome;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a manager of {@linkplain Registry Minecraft registries}.
 *
 * @since 1.0
 * @author Codestech
 * @see Registry
 */
public interface RegistryManager {
    /**
     * Gets a {@linkplain Registry registry} of {@linkplain Biome biomes}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull Registry<Biome> biomeRegistry();
}