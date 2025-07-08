package net.hypejet.jet.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hypejet.jet.data.json.adapters.model.biome.BiomeTypeAdapterFactory;

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
            .registerTypeAdapterFactory(BiomeTypeAdapterFactory.INSTANCE)
            .setPrettyPrinting()
            .create();

    private DataJson() {}
}