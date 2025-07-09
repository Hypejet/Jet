package net.hypejet.jet.data.json.adapters;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.entry.DataEntry;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain DataEntry.FeaturePack feature packs}.
 *
 * @since 1.0
 * @see DataEntry.FeaturePack
 * @see TypeAdapter
 */
public final class FeaturePackTypeAdapter extends TypeAdapter<DataEntry.FeaturePack> {

    private static final String NAMESPACE_FIELD = "namespace";
    private static final String VALUE_FIELD = "value";
    private static final String VERSION_FIELD = "version";

    /**
     * An instance of the {@linkplain FeaturePackTypeAdapter feature pack type adapter}.
     *
     * @since 1.0
     * @see FeaturePackTypeAdapter
     */
    public static final FeaturePackTypeAdapter INSTANCE = new FeaturePackTypeAdapter();

    private FeaturePackTypeAdapter() {}

    @Override
    public void write(JsonWriter out, DataEntry.FeaturePack value) throws IOException {
        out.beginObject();

        out.name(NAMESPACE_FIELD);
        out.value(value.namespace());

        out.name(VALUE_FIELD);
        out.value(value.value());

        out.name(VERSION_FIELD);
        out.value(value.version());

        out.endObject();
    }

    @Override
    public DataEntry.FeaturePack read(JsonReader in) throws IOException {
        in.beginObject();

        String namespace = null;
        String value = null;
        String version = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case NAMESPACE_FIELD -> namespace = in.nextString();
                case VALUE_FIELD -> value = in.nextString();
                case VERSION_FIELD -> version = in.nextString();
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (namespace == null) {
            throw new JsonParseException("The namespace has not been specified");
        } else if (value == null) {
            throw new JsonParseException("The value has not been specified");
        } else if (version == null) {
            throw new JsonParseException("The version has not been specified");
        }

        return new DataEntry.FeaturePack(namespace, value, version);
    }
}