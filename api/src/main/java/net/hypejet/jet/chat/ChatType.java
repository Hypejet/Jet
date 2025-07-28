package net.hypejet.jet.chat;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A type of Minecraft chat message.
 *
 * @param chat configuration on how the chat message should be displayed in chat
 * @param narration configuration on how the chat message should be read by the narrator
 * @since 1.0
 */
public record ChatType(@NonNull ChatTypeDecoration chat, @NonNull ChatTypeDecoration narration) {
    /**
     * Constructs the {@linkplain ChatType chat type}.
     *
     * @param chat configuration on how the chat message should be displayed in chat
     * @param narration configuration on how the chat message should be read by the narrator
     * @since 1.0
     */
    public ChatType {
        Objects.requireNonNull(chat, "chat");
        Objects.requireNonNull(narration, "narration");
    }
}