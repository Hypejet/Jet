package net.hypejet.jet.data.json.adapters.model;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonHolderSet;
import net.hypejet.jet.data.json.util.JsonUtil;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonHolderSet holder sets}.
 *
 * @param <V> a holder value type of holder sets that this type adapter converts
 * @since 1.0
 * @see JsonHolderSet
 * @see TypeAdapter
 */
public final class HolderSetTypeAdapter<V> extends TypeAdapter<JsonHolderSet<V>> {

    private static final String TYPE_FIELD = "type";
    private static final String CONTENTS_FIELD = "contents";
    private static final String TAG_FIELD = "tag";

    private static final String DIRECT_TYPE = "direct";
    private static final String NAMED_TYPE = "named";

    private final Gson gson;
    private final Type holderType;

    /**
     * Constructs the {@linkplain HolderSetTypeAdapter holder set type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @param valueClass a holder value class of holder sets that the type adapter should convert
     * @since 1.0
     */
    public HolderSetTypeAdapter(@NonNull Gson gson, @NonNull Class<V> valueClass) {
        this.gson = Objects.requireNonNull(gson, "gson");
        Objects.requireNonNull(valueClass, "value class");
        this.holderType = TypeToken.getParameterized(JsonHolder.class, valueClass).getType();
    }

    @Override
    public void write(JsonWriter out, JsonHolderSet<V> value) throws IOException {
        out.beginObject();

        out.name(TYPE_FIELD);
        out.value(value instanceof JsonHolderSet.Direct<V> ? DIRECT_TYPE : NAMED_TYPE);

        switch (value) {
            case JsonHolderSet.Direct<V>(List<JsonHolder<V>> contents) -> {
                if (!contents.isEmpty()) {
                    out.name(CONTENTS_FIELD);
                    JsonUtil.writeCollection(out, this.gson, this.holderType, contents);
                }
            }
            case JsonHolderSet.Named<V>(Key tag) -> {
                out.name(TAG_FIELD);
                this.gson.toJson(tag, Key.class, out);
            }
        }

        out.endObject();
    }

    @Override
    public JsonHolderSet<V> read(JsonReader in) throws IOException {
        in.beginObject();

        String type = null;
        List<JsonHolder<V>> contents = new ArrayList<>();
        Key tag = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case TYPE_FIELD -> type = in.nextString();
                case CONTENTS_FIELD -> JsonUtil.readCollection(in, this.gson, this.holderType, contents);
                case TAG_FIELD -> tag = this.gson.fromJson(in, Key.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        return switch (type) {
            case DIRECT_TYPE -> new JsonHolderSet.Direct<>(contents);
            case NAMED_TYPE -> {
                if (tag == null)
                    throw new JsonParseException("The tag has not been specified");
                yield new JsonHolderSet.Named<>(tag);
            }
            case null -> throw new JsonParseException("The holder set type has not been specified");
            default -> throw new JsonParseException("Unknown holder set type: " + type);
        };
    }
}