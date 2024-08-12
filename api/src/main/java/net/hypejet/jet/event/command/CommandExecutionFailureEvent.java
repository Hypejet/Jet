package net.hypejet.jet.event.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.hypejet.jet.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain CommandEvent command event}, which is called when a failure during command execution
 * occurred.
 *
 * @param source a command source, which tried to execute the command
 * @param exception an exception, which was thrown due to the failure
 * @since 1.0
 * @author Codsetech
 * @see CommandEvent
 */
public record CommandExecutionFailureEvent(@NonNull CommandSource source, @NonNull CommandSyntaxException exception)
        implements CommandEvent {}