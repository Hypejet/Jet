package net.hypejet.jet.event.events.lifecycle;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.registry.RegistryManager;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An event called when the {@linkplain RegistryManager registry manager}
 * of the {@linkplain MinecraftServer server} has been initialized.
 *
 * @param registryManager the initialized registry manager
 * @since 1.0
 * @see RegistryManager
 * @see MinecraftServer
 */
public record RegistryManagerLoadEvent(@NonNull RegistryManager registryManager) {
    /**
     * Constructs the {@linkplain RegistryManagerLoadEvent registry manager load event}.
     *
     * @param registryManager the initialized registry manager
     * @since 1.0
     */
    public RegistryManagerLoadEvent {
        Objects.requireNonNull(registryManager, "registry manager");
    }
}