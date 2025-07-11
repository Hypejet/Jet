package net.hypejet.jet.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hypejet.jet.data.json.adapters.model.biome.BiomeTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.chat.type.ChatTypeTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.trim.material.TrimMaterialTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.trim.pattern.TrimPatternTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.variant.cat.CatVariantTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.variant.frog.FrogVariantTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.variant.pig.PigVariantTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.variant.wolf.WolfVariantTypeAdapterFactory;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

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
    public static final Gson GSON = GsonComponentSerializer.gson().populator().apply(new GsonBuilder())
            .registerTypeAdapterFactory(DataTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(BiomeTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(ChatTypeTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(TrimMaterialTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(TrimPatternTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(WolfVariantTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(PigVariantTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(FrogVariantTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(CatVariantTypeAdapterFactory.INSTANCE)
            .setPrettyPrinting()
            .create();

    private DataJson() {}
}