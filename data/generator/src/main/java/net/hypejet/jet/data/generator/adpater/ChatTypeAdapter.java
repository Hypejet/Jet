package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.type.chat.JsonChatType;
import net.hypejet.jet.data.json.model.type.chat.JsonChatTypeDecoration;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.ChatTypeDecoration;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents something converting {@linkplain ChatType chat types} to a Jet data equivalent.
 *
 * @since 1.0
 * @see ChatType
 */
public final class ChatTypeAdapter {

    private ChatTypeAdapter() {}

    /**
     * Converts the specified {@linkplain ChatType chat type} to a Jet data equivalent.
     *
     * @param chatType the chat type to convert
     * @return the converted chat type
     * @since 1.0
     */
    public static @NonNull JsonChatType convert(@NonNull ChatType chatType) {
        return new JsonChatType(convertDecoration(chatType.chat()), convertDecoration(chatType.narration()));
    }

    private static @NonNull JsonChatTypeDecoration convertDecoration(@NonNull ChatTypeDecoration decoration) {
        return new JsonChatTypeDecoration(
                decoration.translationKey(),
                convertParameters(decoration.parameters()),
                StyleAdapter.convert(decoration.style())
        );
    }

    private static @NonNull List<JsonChatTypeDecoration.Parameter> convertParameters(
            @NonNull List<ChatTypeDecoration.Parameter> parameters
    ) {
        List<JsonChatTypeDecoration.Parameter> convertedParameters = new ArrayList<>();
        for (ChatTypeDecoration.Parameter parameter : parameters) {
            convertedParameters.add(switch (parameter) {
                case SENDER -> JsonChatTypeDecoration.Parameter.SENDER;
                case TARGET -> JsonChatTypeDecoration.Parameter.TARGET;
                case CONTENT -> JsonChatTypeDecoration.Parameter.CONTENT;
            });
        }
        return List.copyOf(convertedParameters);
    }
}