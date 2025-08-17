package net.hypejet.jet.event.events.lifecycle;

import net.hypejet.jet.MinecraftServer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * An event called when the {@linkplain MinecraftServer server}
 * has been fully initialized and is listening for connections.
 *
 * @param server the server that is ready
 * @since 1.0
 */
public record ServerReadyEvent(@NonNull MinecraftServer server) {
    /**
     * Constructs the {@linkplain ServerReadyEvent server ready event}.
     *
     * @param server the server that is ready
     * @since 1.0
     */
    public ServerReadyEvent {
        Objects.requireNonNull(server, "server");
    }
}