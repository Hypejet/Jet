package net.hypejet.jet.data.json.adapters.model.enchantment;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonEquipmentSlotGroup;
import net.hypejet.jet.data.json.model.JsonHolderSet;
import net.hypejet.jet.data.json.model.enchantment.JsonEnchantment;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.util.JsonUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonEnchantment enchantments}.
 *
 * @since 1.0
 * @see JsonEnchantment
 * @see TypeAdapter
 */
final class EnchantmentTypeAdapter extends TypeAdapter<JsonEnchantment> {

    private static final String DESCRIPTION_FIELD = "description";
    private static final String EXCLUSIVE_SET_FIELD = "exclusive-set";
    private static final String SUPPORTED_ITEMS_FIELD = "supported-items";
    private static final String PRIMARY_ITEMS_FIELD = "primary-items";
    private static final String WEIGHT_FIELD = "weight";
    private static final String MAX_LEVEL_FIELD = "max-level";
    private static final String MIN_COST_FIELD = "min-cost";
    private static final String MAX_COST_FIELD = "max-cost";
    private static final String ANVIL_COST_FIELD = "anvil-cost";
    private static final String SLOTS_FIELD = "slots";
    private static final String EFFECTS_FIELD = "effects";

    private static final Type ENCHANTMENT_SET_TYPE = new TypeToken<JsonHolderSet<JsonEnchantment>>() {}.getType();
    private static final Type ITEM_HOLDER_SET_TYPE = new TypeToken<JsonHolderSet<JsonItem>>() {}.getType();
    private static final Type EFFECTS_TYPE = new TypeToken<Map<Key, BinaryTagHolder>>() {}.getType();

    private final Gson gson;

    /**
     * Constructs the {@linkplain EnchantmentTypeAdapter enchantment type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    EnchantmentTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonEnchantment value) throws IOException {
        out.beginObject();

        out.name(DESCRIPTION_FIELD);
        this.gson.toJson(value.description(), Component.class, out);

        out.name(EXCLUSIVE_SET_FIELD);
        this.gson.toJson(value.exclusiveSet(), ENCHANTMENT_SET_TYPE, out);

        out.name(SUPPORTED_ITEMS_FIELD);
        this.gson.toJson(value.supportedItems(), ITEM_HOLDER_SET_TYPE, out);

        JsonHolderSet<JsonItem> primaryItems = value.primaryItems();
        if (primaryItems != null) {
            out.name(PRIMARY_ITEMS_FIELD);
            this.gson.toJson(value.primaryItems(), ITEM_HOLDER_SET_TYPE, out);
        }

        out.name(WEIGHT_FIELD);
        out.value(value.weight());

        out.name(MAX_LEVEL_FIELD);
        out.value(value.maxLevel());

        out.name(MIN_COST_FIELD);
        this.gson.toJson(value.minCost(), JsonEnchantment.Cost.class, out);

        out.name(MAX_COST_FIELD);
        this.gson.toJson(value.maxCost(), JsonEnchantment.Cost.class, out);

        out.name(ANVIL_COST_FIELD);
        out.value(value.anvilCost());

        List<JsonEquipmentSlotGroup> slots = value.slots();
        if (!slots.isEmpty()) {
            out.name(SLOTS_FIELD);
            JsonUtil.writeCollection(out, this.gson, JsonEquipmentSlotGroup.class, slots);
        }

        Map<Key, BinaryTagHolder> effects = value.effects();
        if (!effects.isEmpty()) {
            out.name(EFFECTS_FIELD);
            this.gson.toJson(effects, EFFECTS_TYPE, out);
        }

        out.endObject();
    }

    @Override
    public JsonEnchantment read(JsonReader in) throws IOException {
        in.beginObject();

        Component description = null;
        JsonHolderSet<JsonEnchantment> exclusiveSet = null;
        JsonHolderSet<JsonItem> supportedItems = null;
        JsonHolderSet<JsonItem> primaryItems = null;
        int weight = 0;
        int maxLevel = 0;
        JsonEnchantment.Cost minCost = null;
        JsonEnchantment.Cost maxCost = null;
        int anvilCost = 0;
        List<JsonEquipmentSlotGroup> slots = new ArrayList<>();
        Map<Key, BinaryTagHolder> effects = Map.of();

        boolean weightInitialized = false;
        boolean maxLevelInitialized = false;
        boolean anvilCostInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case DESCRIPTION_FIELD -> description = this.gson.fromJson(in, Component.class);
                case EXCLUSIVE_SET_FIELD -> exclusiveSet = this.gson.fromJson(in, ENCHANTMENT_SET_TYPE);
                case SUPPORTED_ITEMS_FIELD -> supportedItems = this.gson.fromJson(in, ITEM_HOLDER_SET_TYPE);
                case PRIMARY_ITEMS_FIELD -> primaryItems = this.gson.fromJson(in, ITEM_HOLDER_SET_TYPE);
                case WEIGHT_FIELD -> {
                    weight = in.nextInt();
                    weightInitialized = true;
                }
                case MAX_LEVEL_FIELD -> {
                    maxLevel = in.nextInt();
                    maxLevelInitialized = true;
                }
                case MIN_COST_FIELD -> minCost = this.gson.fromJson(in, JsonEnchantment.Cost.class);
                case MAX_COST_FIELD -> maxCost = this.gson.fromJson(in, JsonEnchantment.Cost.class);
                case ANVIL_COST_FIELD -> {
                    anvilCost = in.nextInt();
                    anvilCostInitialized = true;
                }
                case SLOTS_FIELD -> JsonUtil.readCollection(in, this.gson, JsonEquipmentSlotGroup.class, slots);
                case EFFECTS_FIELD -> effects = this.gson.fromJson(in, EFFECTS_TYPE);
            }
        }

        in.endObject();

        if (description == null) {
            throw new JsonParseException("The description has not been specified");
        } else if (exclusiveSet == null) {
            throw new JsonParseException("The exclusive set has not been specified");
        } else if (supportedItems == null) {
            throw new JsonParseException("The supported items have not been specified");
        } else if (!weightInitialized) {
            throw new JsonParseException("The weight has not been specified");
        } else if (!maxLevelInitialized) {
            throw new JsonParseException("The max level has not been specified");
        } else if (minCost == null) {
            throw new JsonParseException("The min cost has not been specified");
        } else if (maxCost == null) {
            throw new JsonParseException("The max cost has not been specified");
        } else if (!anvilCostInitialized) {
            throw new JsonParseException("The anvil cost has not been specified");
        }

        return new JsonEnchantment(
                description, exclusiveSet, supportedItems, primaryItems, weight,
                maxLevel, minCost, maxCost, anvilCost, slots, effects
        );
    }
}