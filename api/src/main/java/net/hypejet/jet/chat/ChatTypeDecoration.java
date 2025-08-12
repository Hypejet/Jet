package net.hypejet.jet.chat;

import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Configuration on how {@linkplain ChatType chat types} should be displayed in chat or read by the narrator.
 *
 * @param translationKey the language translation key to use, can also be a plain text with {@code %s} insertions
 * @param parameters the parameters to insert when rendering a text specified in the translation key field
 * @param style a style to apply to the whole chat message
 * @since 1.0
 * @see ChatType
 */
public record ChatTypeDecoration(@NonNull String translationKey, @NonNull List<Parameter> parameters,
                                 @NonNull Style style) {
    /**
     * Constructs the {@linkplain ChatTypeDecoration chat type decoration}.
     *
     * @param translationKey the language translation key to use, can also be a plain text with {@code %s} insertions
     * @param parameters the parameters to insert when rendering a text specified in the translation key field
     * @param style a style to apply to the whole chat message
     * @since 1.0
     */
    public ChatTypeDecoration {
        Objects.requireNonNull(translationKey, "translation key");
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(style, "style");
    }

    /**
     * A parameter of a {@linkplain ChatTypeDecoration chat type decoration}.
     *
     * <p>This is not an enum since it depends on Minecraft.
     * Adding new entries could break switch cases for example.</p>
     *
     * @since 1.0
     */
    public static final class Parameter {
        /**
         * A parameter inserting sender name into a chat message.
         *
         * @since 1.0
         */
        public static final Parameter SENDER = new Parameter("sender");

        /**
         * A parameter inserting receiver name into a chat message.
         *
         * @since 1.0
         */
        public static final Parameter TARGET = new Parameter("target");

        /**
         * A parameter inserting content into a chat message.
         *
         * @since 1.0
         */
        public static final Parameter CONTENT = new Parameter("content");

        private final String name;

        private Parameter(@NonNull String name) {
            this.name = Objects.requireNonNull(name, "name");
        }

        @Override
        public String toString() {
            return "Parameter{" +
                    "name='" + this.name + '\'' +
                    '}';
        }
    }
}