package net.hypejet.jet.server.util.game.signing;

import java.util.Objects;
import net.hypejet.jet.util.bitset.UnmodifiableBitSet;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An information about last seen Minecraft chat messages.
 *
 * @param messageCount the chat message count
 * @param acknowledged a bitset, where each bit represents whether a corresponding message was acknowledged
 * @param checksum a hash of signatures of the last client-seen chat messages, used
 *                 for de-synchronized state detection, {@code 0} if the check is disabled
 * @since 1.0
 */
public record SeenMessages(int messageCount, @NonNull UnmodifiableBitSet acknowledged, byte checksum) {
    /**
     * Constructs the {@linkplain SeenMessages seen messages}.
     *
     * @param messageCount a count of the messages
     * @param acknowledged a bitset, where each bit represents whether a corresponding message was acknowledged
     * @param checksum a hash of signatures of the last client-seen chat messages, used
     *                 for de-synchronized state detection, {@code 0} if the check should be disabled
     * @since 1.0
     */
    public SeenMessages {
        Objects.requireNonNull(acknowledged, "acknowledged messages");
    }

    /**
     * Gets whether the specified message has been acknowledged.
     *
     * @param message the index of the message
     * @return {@code true} if the message has been acknowledged, {@code false} otherwise
     * @since 1.0
     */
    public boolean wasAcknowledged(int message) {
        return this.acknowledged.bitSet().get(message);
    }
}