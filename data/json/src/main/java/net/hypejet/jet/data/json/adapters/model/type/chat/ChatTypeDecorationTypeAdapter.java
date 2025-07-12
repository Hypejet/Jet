package net.hypejet.jet.data.json.adapters.model.type.chat;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.type.chat.JsonChatTypeDecoration;
import net.hypejet.jet.data.json.util.JsonUtil;
import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonChatTypeDecoration chat type decorations}.
 *
 * @since 1.0
 * @see JsonChatTypeDecoration
 * @see TypeAdapter
 */
final class ChatTypeDecorationTypeAdapter extends TypeAdapter<JsonChatTypeDecoration> {

    private static final String TRANSLATION_KEY_FIELD = "translation-key";
    private static final String PARAMETERS_FIELD = "parameters";
    private static final String STYLE_FIELD = "style";

    private final Gson gson;

    /**
     * Constructs the {@linkplain ChatTypeDecorationTypeAdapter chat type decoration type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    ChatTypeDecorationTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonChatTypeDecoration value) throws IOException {
        out.beginObject();

        out.name(TRANSLATION_KEY_FIELD);
        out.value(value.translationKey());

        List<JsonChatTypeDecoration.Parameter> parameters = value.parameters();
        if (!parameters.isEmpty()) {
            out.name(PARAMETERS_FIELD);
            JsonUtil.writeCollection(out, this.gson, JsonChatTypeDecoration.Parameter.class, parameters);
        }

        Style style = value.style();
        if (!style.isEmpty()) {
            out.name(STYLE_FIELD);
            this.gson.toJson(style, Style.class, out);
        }

        out.endObject();
    }

    @Override
    public JsonChatTypeDecoration read(JsonReader in) throws IOException {
        in.beginObject();

        String translationKey = null;
        List<JsonChatTypeDecoration.Parameter> parameters = new ArrayList<>();
        Style style = Style.empty();

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case TRANSLATION_KEY_FIELD -> translationKey = in.nextString();
                case PARAMETERS_FIELD ->
                        JsonUtil.readCollection(in, this.gson, JsonChatTypeDecoration.Parameter.class, parameters);
                case STYLE_FIELD -> style = this.gson.fromJson(in, Style.class);
                default -> throw new JsonParseException("Unknown field:" + name);
            }
        }

        in.endObject();

        if (translationKey == null)
            throw new JsonParseException("The translation key has not been specified");
        return new JsonChatTypeDecoration(translationKey, parameters, style);
    }
}