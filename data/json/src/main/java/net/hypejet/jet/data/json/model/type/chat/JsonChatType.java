package net.hypejet.jet.data.json.model.type.chat;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A type of Minecraft chat message.
 *
 * @param chat configuration on how the chat message should be displayed in chat
 * @param narration configuration on how the chat message should be read by the narrator
 * @since 1.0
 */
public record JsonChatType(@NonNull JsonChatTypeDecoration chat, @NonNull JsonChatTypeDecoration narration) {
    /**
     * Constructs the {@linkplain JsonChatType chat type}.
     *
     * @param chat configuration on how the chat message should be displayed in chat
     * @param narration configuration on how the chat message should be read by the narrator
     * @since 1.0
     */
    public JsonChatType {
        Objects.requireNonNull(chat, "chat");
        Objects.requireNonNull(narration, "narration");
    }
}