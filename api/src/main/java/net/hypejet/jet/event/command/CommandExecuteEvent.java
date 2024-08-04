package net.hypejet.jet.event.command;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContext;
import net.hypejet.jet.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain CommandEvent command event}, which is called when a command was executed.
 *
 * @param source a command source that executed the command
 * @param input a raw command input, which was sent by the command source
 * @param parseResults a parse results of the command input
 * @param executionResult a numeric result of the command execution, which was returned
 *                        by {@linkplain com.mojang.brigadier.Command#run(CommandContext)}
 * @since 1.0
 * @author Codestech
 * @see CommandEvent
 */
public record CommandExecuteEvent(@NonNull CommandSource source, @NonNull String input,
                                  @NonNull ParseResults<CommandSource> parseResults, int executionResult)
        implements CommandEvent {}