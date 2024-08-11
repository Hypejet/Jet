package net.hypejet.jet.event.command;

import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.event.events.CancellableEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain CommandEvent command event}, which is called before a command input is being parsed.
 *
 * @since 1.0
 * @author Codestech
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
     * Gets the command input.
     *
     * @return the command input
     * @since 1.0
     */
    public @NonNull String getInput() {
        return this.input;
    }

    /**
     * Sets the command input.
     *
     * @param input the command input
     * @since 1.0
     */
    public void setInput(@NonNull String input) {
        this.input = input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommandPreParseEvent that)) return false;
        return Objects.equals(this.source, that.source) && Objects.equals(this.input, that.input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.source, this.input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CommandPreParseEvent{" +
                "source=" + this.source +
                ", input='" + this.input + '\'' +
                '}';
    }
}