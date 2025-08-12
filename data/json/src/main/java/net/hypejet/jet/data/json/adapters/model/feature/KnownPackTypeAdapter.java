package net.hypejet.jet.data.json.adapters.model.feature;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.feature.JsonKnownPack;

import java.io.IOException;

/**
 * A {@linkplain TypeAdapter type adapter} of {@linkplain JsonKnownPack known packs}.
 *
 * @since 1.0
 * @see JsonKnownPack
 * @see TypeAdapter
 */
final class KnownPackTypeAdapter extends TypeAdapter<JsonKnownPack> {

    private static final String NAMESPACE_FIELD = "namespace";
    private static final String PATH_FIELD = "path";
    private static final String VERSION_FIELD = "version";

    /**
     * An instance of the {@linkplain KnownPackTypeAdapter known pack type adapter}.
     *
     * @since 1.0
     */
    static final KnownPackTypeAdapter INSTANCE = new KnownPackTypeAdapter();

    private KnownPackTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonKnownPack value) throws IOException {
        out.beginObject();

        out.name(NAMESPACE_FIELD);
        out.value(value.namespace());

        out.name(PATH_FIELD);
        out.value(value.path());

        out.name(VERSION_FIELD);
        out.value(value.version());

        out.endObject();
    }

    @Override
    public JsonKnownPack read(JsonReader in) throws IOException {
        in.beginObject();

        String namespace = null;
        String path = null;
        String version = null;

        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case NAMESPACE_FIELD -> namespace = in.nextString();
                case PATH_FIELD -> path = in.nextString();
                case VERSION_FIELD -> version = in.nextString();
            }
        }

        in.endObject();

        if (namespace == null) {
            throw new JsonParseException("The namespace has not been specified");
        } else if (path == null) {
            throw new JsonParseException("The path has not been specified");
        } else if (version == null) {
            throw new JsonParseException("The version has not been specified");
        } else {
            return new JsonKnownPack(namespace, path, version);
        }
    }
}