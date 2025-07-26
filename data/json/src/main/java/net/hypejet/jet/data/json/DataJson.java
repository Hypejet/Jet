package net.hypejet.jet.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hypejet.jet.data.json.adapters.adventure.AdventureTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.guava.GuavaTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.block.BlockTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.entity.EntityTypeTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.event.GameEventTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.item.ItemTypeAdapterFactory;
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
            .registerTypeAdapterFactory(GuavaTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(AdventureTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(ItemTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(BlockTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(EntityTypeTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(GameEventTypeAdapterFactory.INSTANCE)
            .setPrettyPrinting()
            .create();

    private DataJson() {}
}