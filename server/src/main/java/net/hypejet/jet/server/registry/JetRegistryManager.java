package net.hypejet.jet.server.registry;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.registry.RegistryManager;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Represents an implementation of the {@linkplain RegistryManager registry manager}.
 *
 * @since 1.0
 * @author Codestech
 * @see RegistryManager
 */
public final class JetRegistryManager implements RegistryManager {

    private final Map<Key, JetRegistry<?>> registries;

    /**
     * Constructs the {@linkplain JetRegistryManager registry manager}.
     *
     * @since 1.0
     */
    public JetRegistryManager(@NonNull MinecraftServer server) {
        this.registries = Map.of();
    }

    @Override
    public @Nullable JetRegistry<?> getRegistry(@NonNull Key identifier) {
        return this.registries.get(identifier);
    }

    @Override
    public @NonNull Map<Key, JetRegistry<?>> getRegistries() {
        return Map.copyOf(this.registries);
    }
}