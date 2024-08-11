package net.hypejet.jet.event.command;

import net.hypejet.jet.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event, which is called when something related to command management happens.
 *
 * @since 1.0
 * @author Codestech
 */
public interface CommandEvent {
    /**
     * Gets a command source, which was involved into the event.
     *
     * @return the command source
     * @since 1.0
     */
    @NonNull CommandSource source();
}