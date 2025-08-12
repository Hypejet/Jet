package net.hypejet.jet.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hypejet.jet.data.json.adapters.adventure.AdventureTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.entry.RegistryEntryTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.guava.GuavaTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.block.BlockTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.entity.EntityTypeTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.event.GameEventTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.feature.FeatureTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.item.ItemTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.model.sound.SoundEventTypeAdapterFactory;
import net.hypejet.jet.data.json.adapters.util.UtilTypeAdapterFactory;

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
            .registerTypeAdapterFactory(UtilTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(RegistryEntryTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(GuavaTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(AdventureTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(ItemTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(BlockTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(EntityTypeTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(GameEventTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(SoundEventTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(FeatureTypeAdapterFactory.INSTANCE)
            .setPrettyPrinting()
            .create();

    private DataJson() {}
}