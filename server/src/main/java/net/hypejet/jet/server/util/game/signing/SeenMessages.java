package net.hypejet.jet.server.util.game.signing;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.util.bitset.UnmodifiableBitSet;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents messages, which were seen by a client.
 *
 * @param messageCount a count of the messages
 * @param acknowledged a bitset, where each bit represents whether a corresponding message was acknowledged
 * @since 1.0
 */
public record SeenMessages(int messageCount, @NonNull UnmodifiableBitSet acknowledged) {
    /**
     * Constructs the {@linkplain SeenMessages seen messages}.
     *
     * @param messageCount a count of the messages
     * @param acknowledged a bitset, where each bit represents whether a corresponding message was acknowledged
     * @since 1.0
     */
    public SeenMessages(int messageCount, @NonNull BitSet acknowledged) {
        this(messageCount, new UnmodifiableBitSet(acknowledged));
    }

    /**
     * Constructs the {@linkplain SeenMessages seen messages}.
     *
     * @param messageCount a count of the messages
     * @param acknowledged a bitset, where each bit represents whether a corresponding message was acknowledged
     * @since 1.0
     */
    public SeenMessages {
        NullabilityUtil.requireNonNull(acknowledged, "acknowledged messages");
    }

    /**
     * Gets whether a message was acknowledged or not.
     *
     * @param message an index of the message
     * @return {@code true} if the message was acknowledged, {@code false} otherwise
     * @since 1.0
     */
    public boolean wasAcknowledged(int message) {
        return this.acknowledged.bitSet().get(message);
    }
}