package net.hypejet.jet.data.json.adapters.model.instrument;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.instrument.JsonInstrument;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonInstrument instruments}.
 *
 * @since 1.0
 * @see JsonInstrument
 * @see TypeAdapterFactory
 */
public final class InstrumentTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain InstrumentTypeAdapterFactory instrument type adapter factory}.
     *
     * @since 1.0
     */
    public static final InstrumentTypeAdapterFactory INSTANCE = new InstrumentTypeAdapterFactory();

    private InstrumentTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonInstrument.class.isAssignableFrom(type.getRawType())) {
            return new InstrumentTypeAdapter(gson);
        } else {
            return null;
        }
    }
}