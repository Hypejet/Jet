package net.hypejet.jet.data.json.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonIntProvider;
import net.hypejet.jet.data.json.model.JsonWeighted;
import net.hypejet.jet.data.json.util.JsonUtil;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonIntProvider int providers}.
 *
 * @since 1.0
 * @see JsonIntProvider
 * @see TypeAdapter
 */
public final class IntProviderTypeAdapter extends TypeAdapter<JsonIntProvider> {

    private static final String TYPE_CLAMPED = "clamped";
    private static final String TYPE_CLAMPED_NORMAL = "clamped-normal";
    private static final String TYPE_UNIFORM = "uniform";
    private static final String TYPE_BIASED_TO_BOTTOM = "biased-to-bottom";
    private static final String TYPE_WEIGHTED_RANDOM = "weighted-random";

    private static final String TYPE_FIELD = "type";
    private static final String SOURCE_FIELD = "source";
    private static final String MINIMUM_FIELD = "min";
    private static final String MAXIMUM_FIELD = "max";
    private static final String MEAN_FIELD = "mean";
    private static final String DEVIATION_FIELD = "deviation";
    private static final String DISTRIBUTION_FIELD = "distribution";

    private static final Type WEIGHTED_INT_PROVIDER_TYPE = new TypeToken<JsonWeighted<JsonIntProvider>>() {}.getType();

    private final Gson gson;

    /**
     * Constructs the {@linkplain IntProviderTypeAdapter int provider type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    public IntProviderTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonIntProvider value) throws IOException {
        switch (value) {
            case JsonIntProvider.Constant(int intValue) -> out.value(intValue);
            case JsonIntProvider.Clamped(JsonIntProvider source, int minimum, int maximum) -> {
                out.beginObject();

                out.name(TYPE_FIELD);
                out.value(TYPE_CLAMPED);

                out.name(SOURCE_FIELD);
                this.write(out, source);

                out.name(MINIMUM_FIELD);
                out.value(minimum);

                out.name(MAXIMUM_FIELD);
                out.value(maximum);

                out.endObject();
            }
            case JsonIntProvider.ClampedNormal(float mean, float deviation, int minimum, int maximum) -> {
                out.beginObject();

                out.name(TYPE_FIELD);
                out.value(TYPE_CLAMPED_NORMAL);

                out.name(MEAN_FIELD);
                out.value(mean);

                out.name(DEVIATION_FIELD);
                out.value(deviation);

                out.name(MINIMUM_FIELD);
                out.value(minimum);

                out.name(MAXIMUM_FIELD);
                out.value(maximum);

                out.endObject();
            }
            case JsonIntProvider.Uniform(int minimum, int maximum) -> {
                out.beginObject();

                out.name(TYPE_FIELD);
                out.value(TYPE_UNIFORM);

                out.name(MINIMUM_FIELD);
                out.value(minimum);

                out.name(MAXIMUM_FIELD);
                out.value(maximum);

                out.endObject();
            }
            case JsonIntProvider.BiasedToBottom(int minimum, int maximum) -> {
                out.beginObject();

                out.name(TYPE_FIELD);
                out.value(TYPE_BIASED_TO_BOTTOM);

                out.name(MINIMUM_FIELD);
                out.value(minimum);

                out.name(MAXIMUM_FIELD);
                out.value(maximum);

                out.endObject();
            }
            case JsonIntProvider.WeightedRandom(List<JsonWeighted<JsonIntProvider>> distribution) -> {
                out.beginObject();

                out.name(TYPE_FIELD);
                out.value(TYPE_WEIGHTED_RANDOM);

                out.name(DISTRIBUTION_FIELD);
                JsonUtil.writeCollection(out, this.gson, WEIGHTED_INT_PROVIDER_TYPE, distribution);

                out.endObject();
            }
        }
    }

    @Override
    public JsonIntProvider read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NUMBER)
            return new JsonIntProvider.Constant(in.nextInt());

        in.beginObject();

        String type = null;
        JsonIntProvider source = null;
        int minimum = 0;
        int maximum = 0;
        float mean = 0;
        float deviation = 0;
        List<JsonWeighted<JsonIntProvider>> distribution = new ArrayList<>();

        boolean minimumInitialized = false;
        boolean maximumInitialized = false;
        boolean meanInitialized = false;
        boolean deviationInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case TYPE_FIELD -> type = in.nextString();
                case SOURCE_FIELD -> source = this.read(in);
                case MINIMUM_FIELD -> {
                    minimum = in.nextInt();
                    minimumInitialized = true;
                }
                case MAXIMUM_FIELD -> {
                    maximum = in.nextInt();
                    maximumInitialized = true;
                }
                case MEAN_FIELD -> {
                    mean = (float) in.nextDouble();
                    meanInitialized = true;
                }
                case DEVIATION_FIELD -> {
                    deviation = (float) in.nextDouble();
                    deviationInitialized = true;
                }
                case DISTRIBUTION_FIELD ->
                        JsonUtil.readCollection(in, this.gson, WEIGHTED_INT_PROVIDER_TYPE, distribution);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (!Objects.equals(type, TYPE_WEIGHTED_RANDOM)) {
            if (!minimumInitialized) {
                throw new JsonParseException("The minimum field has not been specified");
            } else if (!maximumInitialized) {
                throw new JsonParseException("The maximum field has not been specified");
            }
        }

        return switch (type) {
            case TYPE_CLAMPED -> {
                if (source == null)
                    throw new JsonParseException("The source has not been specified");
                yield new JsonIntProvider.Clamped(source, minimum, maximum);
            }
            case TYPE_CLAMPED_NORMAL -> {
                if (!meanInitialized) {
                    throw new JsonParseException("The mean field has not been specified");
                } else if (!deviationInitialized) {
                    throw new JsonParseException("The deviation has not been specified");
                }

                yield new JsonIntProvider.ClampedNormal(mean, deviation, minimum, maximum);
            }
            case TYPE_UNIFORM -> new JsonIntProvider.Uniform(minimum, maximum);
            case TYPE_BIASED_TO_BOTTOM -> new JsonIntProvider.BiasedToBottom(minimum, maximum);
            case TYPE_WEIGHTED_RANDOM -> new JsonIntProvider.WeightedRandom(distribution);
            case null -> throw new JsonParseException("The type has not been specified");
            default -> throw new JsonParseException("Unknown type: " + type);
        };
    }
}