package net.hypejet.jet.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Represents a holder of a {@linkplain Gson gson} instance converting Jet data objects.
 *
 * @since 1.0
 */
public final class DataJson {
    /**
     * The gson instance.
     *
     * @since 1.0
     */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(DataTypeAdapterFactory.INSTANCE)
            .create();

    private DataJson() {}
}