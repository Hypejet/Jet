package net.hypejet.jet.event.events.lifecycle;

import net.hypejet.jet.MinecraftServer;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An event called when the {@linkplain MinecraftServer server} has been fully initialized, but is not listening
 * for connections yet. When all listeners listening to this event stop executing their work, the server starts
 * listening to the connections.
 *
 * @param server the server that has been fully initialized
 * @since 1.0
 */
public record ServerInitializedEvent(@NonNull MinecraftServer server) {
    /**
     * Constructs the {@linkplain ServerInitializedEvent server initialized event}.
     *
     * @param server the server that has been fully initialized
     * @since 1.0
     */
    public ServerInitializedEvent {
        Objects.requireNonNull(server, "server");
    }
}