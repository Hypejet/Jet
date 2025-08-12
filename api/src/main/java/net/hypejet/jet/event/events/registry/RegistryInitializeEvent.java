package net.hypejet.jet.event.events.registry;

import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * An event called when a {@linkplain MinecraftRegistry registry} where
 * custom registrations can be defined is being initialized.
 *
 * @param <V> the value type of the initialized registry
 * @since 1.0
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
     * Registers the specified value in the {@linkplain MinecraftRegistry registry} that is being initialized.
     *
     * @param key the key that the value should be associated with
     * @param value the value to register
     * @param knownPack an information about a feature pack that can enable the registration without sending
     *                  the entire registration info to the client, {@code null} if there is no such a feature pack
     * @since 1.0
     */
    public void register(@NonNull Key key, @NonNull V value, @Nullable KnownPack knownPack) {
        this.registryAccess.register(key, value, knownPack);
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
         * Registers the specified value in the {@linkplain MinecraftRegistry registry} that is being initialized.
         *
         * @param key the key that the value should be associated with
         * @param value the value to register
         * @param knownPack an information about a feature pack that can enable the registration without sending
         *                  the entire registration info to the client, {@code null} if there is no such a feature pack
         * @since 1.0
         */
        void register(@NonNull Key key, @NonNull V value, @Nullable KnownPack knownPack);
    }
}