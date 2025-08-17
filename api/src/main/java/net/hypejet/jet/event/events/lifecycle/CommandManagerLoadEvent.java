package net.hypejet.jet.event.events.lifecycle;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.command.CommandManager;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An event called when the {@linkplain CommandManager command manager}
 * of the {@linkplain MinecraftServer server} has been initialized.
 *
 * @param commandManager the initialized command manager
 * @since 1.0
 * @see CommandManager
 * @see MinecraftServer
 */
public record CommandManagerLoadEvent(@NonNull CommandManager commandManager) {
    /**
     * Constructs the {@linkplain CommandManagerLoadEvent command manager load event}.
     *
     * @param commandManager the initialized command manager
     * @since 1.0
     */
    public CommandManagerLoadEvent {
        Objects.requireNonNull(commandManager, "command manager");
    }
}