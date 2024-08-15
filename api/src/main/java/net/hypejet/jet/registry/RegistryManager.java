package net.hypejet.jet.registry;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Represents a manager of {@linkplain Registry Minecraft registries}.
 *
 * @since 1.0
 * @author Codestech
 * @see Registry
 */
public interface RegistryManager {
    /**
     * Gets a {@linkplain Registry registry} with an identifier specified.
     *
     * @param identifier the identifier
     * @return the registry
     * @since 1.0
     */
    @Nullable Registry<?> getRegistry(@NonNull Key identifier);

    /**
     * Gets a {@linkplain Map map} of {@linkplain Registry registries} registered in this {@linkplain RegistryManager
     * registry manager}.
     *
     * @return the map
     * @since 1.0
     */
    @NonNull Map<Key, ? extends Registry<?>> getRegistries();
}