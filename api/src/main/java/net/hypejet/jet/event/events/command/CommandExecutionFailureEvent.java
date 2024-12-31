package net.hypejet.jet.event.events.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain CommandEvent a command event}, which is called when a failure during command execution
 * occurs.
 *
 * @param source a command source, which tried to execute the command
 * @param exception an exception, which was thrown due to the failure
 * @since 1.0
 * @see CommandEvent
 */
public record CommandExecutionFailureEvent(@NonNull CommandSource source, @NonNull CommandSyntaxException exception)
        implements CommandEvent {
    /**
     * Constructs the {@linkplain CommandExecutionFailureEvent comand execution failure event}.
     *
     * @param source a command source, which tried to execute the command
     * @param exception an exception, which was thrown due to the failure
     * @since 1.0
     */
    public CommandExecutionFailureEvent {
        NullabilityUtil.requireNonNull(source, "command source");
        NullabilityUtil.requireNonNull(exception, "exception");
    }
}