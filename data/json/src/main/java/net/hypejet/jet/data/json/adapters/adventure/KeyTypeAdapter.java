package net.hypejet.jet.data.json.adapters.adventure;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain Key keys}.
 *
 * @since 1.0
 * @see Key
 * @see TypeAdapter
 */
final class KeyTypeAdapter extends TypeAdapter<Key> {
    /**
     * An instance of the {@linkplain KeyTypeAdapter key adapter}.
     *
     * @since 1.0
     */
    static final KeyTypeAdapter INSTANCE = new KeyTypeAdapter();

    private KeyTypeAdapter() {}

    @Override
    public void write(JsonWriter out, Key value) throws IOException {
        out.value(value.asString());
    }

    @Override
    public Key read(JsonReader in) throws IOException {
        return Key.key(in.nextString());
    }
}