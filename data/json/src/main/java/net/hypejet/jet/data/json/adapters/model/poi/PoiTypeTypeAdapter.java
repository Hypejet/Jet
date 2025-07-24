package net.hypejet.jet.data.json.adapters.model.poi;

import com.google.common.primitives.ImmutableIntArray;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.poi.JsonPoiType;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonPoiType point of interest types}.
 *
 * @since 1.0
 * @see JsonPoiType
 * @see TypeAdapterFactory
 */
final class PoiTypeTypeAdapter extends TypeAdapter<JsonPoiType> {

    private static final String MATCHING_STATE_IDS_FIELD = "matching_state_ids";
    private static final String MAX_TICKETS_FIELD = "max_tickets";
    private static final String VALID_RANGE_FIELD = "valid_range";

    private final Gson gson;

    /**
     * Constructs the {@linkplain PoiTypeTypeAdapter point of interest type type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    PoiTypeTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonPoiType value) throws IOException {
        out.beginObject();

        ImmutableIntArray matchingStateIds = value.matchingStateIds();
        if (!matchingStateIds.isEmpty()) {
            out.name(MATCHING_STATE_IDS_FIELD);
            this.gson.toJson(matchingStateIds, ImmutableIntArray.class, out);
        }

        out.name(MAX_TICKETS_FIELD);
        out.value(value.maxTickets());

        out.name(VALID_RANGE_FIELD);
        out.value(value.validRange());

        out.endObject();
    }

    @Override
    public JsonPoiType read(JsonReader in) throws IOException {
        in.beginObject();

        ImmutableIntArray matchingStateIds = ImmutableIntArray.of();
        int maxTickets = 0;
        int validRange = 0;

        boolean maxTicketsInitialized = false;
        boolean validRangeInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case MATCHING_STATE_IDS_FIELD -> matchingStateIds = this.gson.fromJson(in, ImmutableIntArray.class);
                case MAX_TICKETS_FIELD -> {
                    maxTickets = in.nextInt();
                    maxTicketsInitialized = true;
                }
                case VALID_RANGE_FIELD -> {
                    validRange = in.nextInt();
                    validRangeInitialized = true;
                }
            }
        }

        in.endObject();

        if (!maxTicketsInitialized) {
            throw new JsonParseException("The max tickets value has not been specified");
        } else if (!validRangeInitialized) {
            throw new JsonParseException("The valid range has not been specified");
        } else {
            return new JsonPoiType(matchingStateIds, maxTickets, validRange);
        }
    }
}