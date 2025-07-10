package net.hypejet.jet.data.json.model.chat.type;

import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Configuration on how {@linkplain JsonChatType chat types} should be displayed in chat or read by the narrator.
 *
 * @param translationKey the language translation key to use, can also be a plain text with {@code %s} insertions
 * @param parameters the parameters to insert when rendering a text specified in the translation key field
 * @param style a style to apply to the whole chat message
 * @since 1.0
 */
public record JsonChatTypeDecoration(@NonNull String translationKey, @NonNull List<Parameter> parameters,
                                     @NonNull Style style) {
    /**
     * Constructs the {@linkplain JsonChatTypeDecoration chat type decoration}.
     *
     * @param translationKey the language translation key to use, can also be a plain text with {@code %s} insertions
     * @param parameters the parameters to insert when rendering a text specified in the translation key field
     * @param style a style to apply to the whole chat message
     * @since 1.0
     */
    public JsonChatTypeDecoration {
        Objects.requireNonNull(translationKey, "translation key");
        parameters = List.copyOf(Objects.requireNonNull(parameters, "parameters"));
        Objects.requireNonNull(style, "style");
    }

    /**
     * A parameter of a {@linkplain JsonChatTypeDecoration chat type decoration}.
     *
     * @since 1.0
     */
    public enum Parameter {
        /**
         * A parameter inserting sender name into a chat message.
         *
         * @since 1.0
         */
        SENDER,
        /**
         * A parameter inserting receiver name into a chat message.
         *
         * @since 1.0
         */
        TARGET,
        /**
         * A parameter inserting content into a chat message.
         *
         * @since 1.0
         */
        CONTENT
    }
}