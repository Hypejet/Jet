package net.hypejet.jet.data.json.adapters.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonEquipmentSlotGroup;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonEquipmentSlotGroup equipment slot group}.
 *
 * @since 1.0
 * @see JsonEquipmentSlotGroup
 * @see TypeAdapter
 */
public final class EquipmentSlotGroupTypeAdapter extends TypeAdapter<JsonEquipmentSlotGroup> {
    /**
     * An instance of the {@linkplain EquipmentSlotGroupTypeAdapter equipment slot group type adapter}.
     *
     * @since 1.0
     */
    public static final EquipmentSlotGroupTypeAdapter INSTANCE = new EquipmentSlotGroupTypeAdapter();

    private EquipmentSlotGroupTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonEquipmentSlotGroup value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonEquipmentSlotGroup read(JsonReader in) throws IOException {
        return JsonEquipmentSlotGroup.values()[in.nextInt()];
    }
}