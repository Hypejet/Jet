package net.hypejet.jet.registry;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Represents a manager of {@linkplain MinecraftRegistry Minecraft registries}.
 *
 * @since 1.0
 * @author Codestech
 * @see MinecraftRegistry
 */
public interface RegistryManager {
    /**
     * Gets a {@linkplain MinecraftRegistry Minecraft registry} with a {@linkplain Key key} specified.
     *
     * @param identifier the key
     * @return the registry
     * @since 1.0
     */
    @Nullable MinecraftRegistry<?> getRegistry(@NonNull Key identifier);

    /**
     * Gets a {@linkplain Map map} of {@linkplain MinecraftRegistry registries} registered in
     * this {@linkplain RegistryManager registry manager}.
     *
     * @return the map
     * @since 1.0
     */
    @NonNull Map<Key, ? extends MinecraftRegistry<?>> getRegistries();
}