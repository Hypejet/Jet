package net.hypejet.jet.registry;

import net.hypejet.jet.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.registry.reference.RegistryReference;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a manager of {@linkplain MinecraftRegistry Minecraft registries}.
 *
 * @since 1.0
 * @see MinecraftRegistry
 */
public interface RegistryManager {
    /**
     * Gets a {@linkplain MinecraftRegistry registry} by its reference.
     *
     * @param reference the reference to the wanted registry
     * @return the registry
     * @param <V> the value type of the wanted registry
     * @throws IllegalArgumentException if the specified registry reference is not recognised by the registry managed
     * @since 1.0
     */
    <V> @NonNull MinecraftRegistry<V> registry(@NonNull RegistryReference<V> reference);

    /**
     * Gets a {@linkplain BlockStateRegistry block state registry} containing all possible
     * {@linkplain net.hypejet.jet.world.block.BlockState block states} that can be used
     * on the {@linkplain net.hypejet.jet.MinecraftServer server} associated with this registry manager.
     *
     * @return the block state registry
     * @since 1.0
     */
    @NonNull BlockStateRegistry blockStateRegistry();
}