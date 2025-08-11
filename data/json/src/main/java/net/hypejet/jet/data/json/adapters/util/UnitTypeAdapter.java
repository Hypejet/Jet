package net.hypejet.jet.data.json.adapters.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.util.JsonUnit;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonUnit units}.
 *
 * @since 1.0
 * @see JsonUnit
 * @see TypeAdapter
 */
final class UnitTypeAdapter extends TypeAdapter<JsonUnit> {
    /**
     * An instance of the {@linkplain UnitTypeAdapter unit type adapter}.
     *
     * @since 1.0
     */
    static final UnitTypeAdapter INSTANCE = new UnitTypeAdapter();

    private UnitTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonUnit value) throws IOException {
        out.beginObject();
        out.endObject();
    }

    @Override
    public JsonUnit read(JsonReader in) throws IOException {
        in.beginObject();
        in.endObject();
        return JsonUnit.INSTANCE;
    }
}