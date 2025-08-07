package net.hypejet.jet.event.events.command;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContext;
import net.hypejet.jet.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain CommandEvent a command event}, which is called when a command is executed.
 *
 * @param source a command source that executed the command
 * @param input a raw command input, which was sent by the command source
 * @param parseResults a parse results of the command input
 * @param executionResult a numeric result of the command execution, which was returned
 *                        by {@linkplain com.mojang.brigadier.Command#run(CommandContext)}
 * @since 1.0
 * @see CommandEvent
 */
public record CommandExecuteEvent(@NonNull CommandSource source, @NonNull String input,
                                  @NonNull ParseResults<CommandSource> parseResults, int executionResult)
        implements CommandEvent {
    /**
     * Constructs the {@linkplain CommandExecuteEvent command execute event}.
     *
     * @param source a command source that executed the command
     * @param input a raw command input, which was sent by the command source
     * @param parseResults a parse results of the command input
     * @param executionResult a numeric result of the command execution, which was returned
     *                        by {@linkplain com.mojang.brigadier.Command#run(CommandContext)}
     * @since 1.0
     */
    public CommandExecuteEvent {
        Objects.requireNonNull(source, "command source");
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(parseResults, "parse results");
    }
}