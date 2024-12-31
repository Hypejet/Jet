package net.hypejet.jet.event.events.command;

import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.event.events.CancellableEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain CommandEvent a command event}, which is called before a command input is being parsed.
 *
 * @since 1.0
 * @see CommandEvent
 * @see CancellableEvent
 */
public final class CommandPreParseEvent extends CancellableEvent implements CommandEvent {

    private final CommandSource source;

    private String input;

    /**
     * Constructs the {@linkplain CommandPreParseEvent command pre-parse event}.
     *
     * @param source a command source, which sent the command input
     * @param input the command input
     * @since 1.0
     */
    public CommandPreParseEvent(@NonNull CommandSource source, @NonNull String input) {
        this.source = source;
        this.input = input;
    }

    /**
     * Gets a command source, which sent the command input.
     *
     * @since 1.0
     */
    @Override
    public @NonNull CommandSource source() {
        return this.source;
    }

    /**
     * Gets the command input that should be parsed.
     *
     * @return the command input
     * @since 1.0
     */
    public @NonNull String getInput() {
        return this.input;
    }

    /**
     * Sets the command input that should be parsed.
     *
     * @param input the command input
     * @since 1.0
     */
    public void setInput(@NonNull String input) {
        this.input = input;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommandPreParseEvent event)) return false;
        return Objects.equals(this.source, event.source) && Objects.equals(this.input, event.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source, this.input);
    }

    @Override
    public String toString() {
        return "CommandPreParseEvent{" +
                "source=" + this.source +
                ", input='" + this.input + '\'' +
                '}';
    }
}