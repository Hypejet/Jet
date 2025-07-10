package net.hypejet.jet.data.json.adapters.model.chat.type;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.chat.type.JsonChatType;
import net.hypejet.jet.data.json.model.chat.type.JsonChatTypeDecoration;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting types related to {@linkplain JsonChatType chat types}.
 *
 * @since 1.0
 * @see JsonChatType
 * @see TypeAdapterFactory
 */
public final class ChatTypeTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain ChatTypeTypeAdapterFactory chat type type adapter factory}.
     *
     * @since 1.0
     */
    public static final ChatTypeTypeAdapterFactory INSTANCE = new ChatTypeTypeAdapterFactory();

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (rawType.isAssignableFrom(JsonChatType.class)) {
            return new ChatTypeTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonChatTypeDecoration.class)) {
            return new ChatTypeDecorationTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonChatTypeDecoration.Parameter.class)) {
            return ChatTypeDecorationParameterTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}