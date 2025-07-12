package net.hypejet.jet.data.json.adapters.model.type.chat;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.type.chat.JsonChatTypeDecoration;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter}
 * of {@linkplain JsonChatTypeDecoration.Parameter chat type decoration parameters}.
 *
 * @since 1.0
 * @see JsonChatTypeDecoration.Parameter
 * @see TypeAdapter
 */
final class ChatTypeDecorationParameterTypeAdapter extends TypeAdapter<JsonChatTypeDecoration.Parameter> {
    /**
     * An instance
     * of the {@linkplain ChatTypeDecorationParameterTypeAdapter chat type decoration parameter type adapter}.
     *
     * @since 1.0
     */
    static final ChatTypeDecorationParameterTypeAdapter INSTANCE = new ChatTypeDecorationParameterTypeAdapter();

    private ChatTypeDecorationParameterTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonChatTypeDecoration.Parameter value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonChatTypeDecoration.Parameter read(JsonReader in) throws IOException {
        return JsonChatTypeDecoration.Parameter.values()[in.nextInt()];
    }
}