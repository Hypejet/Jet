package net.hypejet.jet.signing;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents messages, which were seen by a client.
 *
 * @param messageCount a count of the messages
 * @param acknowledged a bitset, where each bit represents whether a corresponding message was acknowledged
 * @since 1.0
 * @author Codestech
 */
public record SeenMessages(int messageCount, @NonNull BitSet acknowledged) {
    /**
     * Constructs the {@linkplain SeenMessages seen messages}.
     *
     * <p>The {@code acknowledged} bitset is cloned to prevent mutations on the record.</p>
     *
     * @param messageCount a count of the messages
     * @param acknowledged a bitset, where each bit represents whether a corresponding message was acknowledged
     * @since 1.0
     */
    public SeenMessages {
        acknowledged = (BitSet) acknowledged.clone();
    }

    /**
     * Gets a {@linkplain BitSet bitset}, where each bit represents whether a corresponding message was acknowledged.
     *
     * <p>The {@code acknowledged} bitset is cloned to prevent mutations on the original bitset.</p>
     *
     * @return the bitset
     * @since 1.0
     */
    @Override
    public BitSet acknowledged() {
        return (BitSet) this.acknowledged.clone();
    }

    /**
     * Gets whether a message was acknowledged or not.
     *
     * @param message an index of the message
     * @return {@code true} if the message was acknowledged, {@code false} otherwise
     * @since 1.0
     */
    public boolean wasAcknowledged(int message) {
        return this.acknowledged.get(message);
    }
}