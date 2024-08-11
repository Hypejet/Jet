package net.hypejet.jet.event.command;

import com.mojang.brigadier.ParseResults;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.event.events.CancellableEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain CommandEvent command event}, which is called when a command has been parsed, but not
 * executed yet.
 *
 * <p>This is the last event that can cancel the command execution via
 * {@link CancellableEvent#setCancelled(boolean)}.</p>
 *
 * @since 1.0
 * @author Codestech
 * @see CommandEvent
 * @see CancellableEvent
 */
public final class CommandPreExecuteEvent extends CancellableEvent implements CommandEvent {

    private final CommandSource source;
    private final String input;
    private final ParseResults<CommandSource> parseResults;

    /**
     * Constructs the {@linkplain CommandPreExecuteEvent command pre-execute event}.
     *
     * @param source a command source, which sent a raw input of the command
     * @param input a raw input of the command input
     * @param parseResults a parse results of the command
     * @since 1.0
     */
    public CommandPreExecuteEvent(@NonNull CommandSource source, @NonNull String input,
                                  @NonNull ParseResults<CommandSource> parseResults) {
        this.source = source;
        this.input = input;
        this.parseResults = parseResults;
    }

    /**
     * Gets a command source, which sent a raw input of the command.
     *
     * @return the command source
     * @since 1.0
     */
    @Override
    public @NonNull CommandSource source() {
        return this.source;
    }

    /**
     * Gets a raw input of the command.
     *
     * @return the raw input
     * @since 1.0
     */
    public @NonNull String input() {
        return this.input;
    }

    /**
     * Gets a parse results of the command input.
     *
     * @return the parse results
     * @since 1.0
     */
    public @NonNull ParseResults<CommandSource> parseResults() {
        return this.parseResults;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommandPreExecuteEvent that)) return false;
        return Objects.equals(this.source, that.source) && Objects.equals(this.input, that.input)
                && Objects.equals(this.parseResults, that.parseResults);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.source, this.input, this.parseResults);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CommandPreExecuteEvent{" +
                "source=" + this.source +
                ", input='" + this.input + '\'' +
                ", parseResults=" + this.parseResults +
                '}';
    }
}