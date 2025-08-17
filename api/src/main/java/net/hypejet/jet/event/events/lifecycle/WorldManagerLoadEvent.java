package net.hypejet.jet.event.events.lifecycle;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.world.WorldManager;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An event called when the {@linkplain WorldManager world manager}
 * of the {@linkplain MinecraftServer server} has been initialized.
 *
 * @param worldManager the initialized command manager
 * @since 1.0
 * @see WorldManager
 * @see MinecraftServer
 */
public record WorldManagerLoadEvent(@NonNull WorldManager worldManager) {
    /**
     * Constructs the {@linkplain WorldManagerLoadEvent world manager load event}.
     *
     * @param worldManager the initialized world manager
     * @since 1.0
     */
    public WorldManagerLoadEvent {
        Objects.requireNonNull(worldManager, "world manager");
    }
}