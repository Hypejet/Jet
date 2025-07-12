package net.hypejet.jet.data.json.adapters.model.type.chat;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.type.chat.JsonChatType;
import net.hypejet.jet.data.json.model.type.chat.JsonChatTypeDecoration;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} converting {@linkplain JsonChatType chat types}.
 *
 * @since 1.0
 * @see JsonChatType
 * @see TypeAdapter
 */
final class ChatTypeTypeAdapter extends TypeAdapter<JsonChatType> {

    private static final String CHAT_FIELD = "chat";
    private static final String NARRATION_FIELD = "narration";

    private final Gson gson;

    /**
     * Constructs the {@linkplain ChatTypeTypeAdapter chat type type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    ChatTypeTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonChatType value) throws IOException {
        out.beginObject();

        out.name(CHAT_FIELD);
        this.gson.toJson(value.chat(), JsonChatTypeDecoration.class, out);

        out.name(NARRATION_FIELD);
        this.gson.toJson(value.narration(), JsonChatTypeDecoration.class, out);

        out.endObject();
    }

    @Override
    public JsonChatType read(JsonReader in) throws IOException {
        in.beginObject();

        JsonChatTypeDecoration chat = null;
        JsonChatTypeDecoration narration = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case CHAT_FIELD -> chat = this.gson.fromJson(in, JsonChatTypeDecoration.class);
                case NARRATION_FIELD -> narration = this.gson.fromJson(in, JsonChatTypeDecoration.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (chat == null) {
            throw new JsonParseException("The chat field has not been specified");
        } else if (narration == null) {
            throw new JsonParseException("The narration field has not been specified");
        }

        return new JsonChatType(chat, narration);
    }
}