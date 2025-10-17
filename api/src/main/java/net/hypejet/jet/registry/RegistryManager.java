package net.hypejet.jet.registry;

import com.google.common.collect.Multimap;
import net.hypejet.jet.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.world.block.state.BlockState;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Map;

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
     * {@linkplain BlockState block states} that can be used
     * on the {@linkplain net.hypejet.jet.MinecraftServer server} associated with this registry manager.
     *
     * @return the block state registry
     * @since 1.0
     */
    @NonNull BlockStateRegistry blockStateRegistry();

    /**
     * Replaces tags of {@linkplain MinecraftRegistry registries} using the specified {@linkplain Map map}
     * associating {@linkplain RegistryReference registry references} with {@linkplain Multimap multimaps}
     * of tags that registries associated with these registry references should have.
     *
     * <p>The {@linkplain Multimap multimaps} inside the specified map should associate {@linkplain Key keys}
     * of tags with {@linkplain Key keys} of registry values that these tags should be associated with.</p>
     *
     * <p>If the specified {@linkplain Map map} contains value for certain
     * {@linkplain RegistryReference registry reference}, tags of {@linkplain MinecraftRegistry registry}
     * associated with the same registry reference are <strong>replaced</strong> with the value of the same
     * {@linkplain Map.Entry map entry}. However, if the {@linkplain Map map} does not contain certain
     * {@linkplain RegistryReference registry reference}, the tags stay the same
     * for that {@linkplain MinecraftRegistry registry}</p>
     *
     * @param tags the map associating registry references with tag multimaps
     * @since 1.0
     */
    void updateTags(@NonNull Map<RegistryReference<?>, Multimap<Key, Key>> tags);
}