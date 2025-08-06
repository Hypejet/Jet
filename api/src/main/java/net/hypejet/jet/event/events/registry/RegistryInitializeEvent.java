package net.hypejet.jet.event.events.registry;

import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * An event called when a {@linkplain MinecraftRegistry registry} where
 * custom {@linkplain RegistryEntry registry entries} can be defined is being initialized.
 *
 * @param <V> the value type of the initialized registry
 * @since 1.0
 * @see RegistryEntry
 * @see MinecraftRegistry
 */
public final class RegistryInitializeEvent<V> {

    private final RegistryReference<V> registryReference;
    private final RegistryAccess<V> registryAccess;

    /**
     * Constructs the {@linkplain RegistryInitializeEvent registry initialize event}.
     *
     * @param registryReference a reference to the registry that is being initialized
     * @param registryAccess access to the registry that is being initialized
     * @since 1.0
     */
    public RegistryInitializeEvent(@NonNull RegistryReference<V> registryReference,
                                   @NonNull RegistryAccess<V> registryAccess) {
        this.registryReference = Objects.requireNonNull(registryReference, "registry reference");
        this.registryAccess = Objects.requireNonNull(registryAccess, "registry access");
    }

    /**
     * Gets a {@linkplain RegistryReference registry reference} to the {@linkplain MinecraftRegistry registry}
     * that is being initialized.
     *
     * @return the registry reference
     * @since 1.0
     */
    public @NonNull RegistryReference<V> registryReference() {
        return this.registryReference;
    }

    /**
     * Registers a {@linkplain RegistryEntry registry entry} in the {@linkplain MinecraftRegistry registry}
     * that is being initialized.
     *
     * @param key the key that the registry entry should have
     * @param value the value that the registry entry should have
     * @param knownPack an information about a feature pack that can enable the registry entry without sending
     *                  the encoded value to the client, {@code null} if there is no such a feature pack
     * @return the registry entry
     * @since 1.0
     */
    public @NonNull RegistryEntry<V> register(@NonNull Key key, @NonNull V value, @Nullable KnownPack knownPack) {
        return this.registryAccess.register(key, value, knownPack);
    }

    /**
     * Gets a copy of a current state of {@linkplain Map map} associating {@linkplain Key keys}
     * with {@linkplain RegistryEntry registry entries} that are currently bound to these keys.
     *
     * @return the map copy
     * @since 1.0
     */
    public @NonNull Map<Key, RegistryEntry<V>> currentEntries() {
        return this.registryAccess.currentEntries();
    }

    /**
     * Access to a {@linkplain MinecraftRegistry registry} that is during initialization process.
     *
     * @param <V> the value type of the registry in initialization process
     * @since 1.0
     * @see MinecraftRegistry
     */
    // TODO: Consider initializing tags here
    public interface RegistryAccess<V> {
        /**
         * Registers a {@linkplain RegistryEntry registry entry} in the {@linkplain MinecraftRegistry registry}
         * that is being initialized.
         *
         * @param key the key that the registry entry should have
         * @param value the value that the registry entry should have
         * @param knownPack an information about a feature pack that can enable the registry entry without sending
         *                  the encoded value to the client, {@code null} if there is no such a feature pack
         * @return the registry entry
         * @since 1.0
         */
        @NonNull RegistryEntry<V> register(@NonNull Key key, @NonNull V value, @Nullable KnownPack knownPack);

        /**
         * Gets a copy of a current state of {@linkplain Map map} associating {@linkplain Key keys}
         * with {@linkplain RegistryEntry registry entries} that are currently bound to these keys.
         *
         * @return the map copy
         * @since 1.0
         */
        @NonNull Map<Key, RegistryEntry<V>> currentEntries();
    }
}