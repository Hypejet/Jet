package net.hypejet.jet.data.json.adapters.adventure;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.nbt.api.BinaryTagHolder;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain BinaryTagHolder binary tag holders}.
 *
 * @since 1.0
 * @see BinaryTagHolder
 * @see TypeAdapter
 */
final class BinaryTagHolderTypeAdapter extends TypeAdapter<BinaryTagHolder> {
    /**
     * An instance of the {@linkplain BinaryTagHolderTypeAdapter binary tag holder type adapter}.
     *
     * @since 1.0
     */
    static final BinaryTagHolderTypeAdapter INSTANCE = new BinaryTagHolderTypeAdapter();

    private BinaryTagHolderTypeAdapter() {}

    @Override
    public void write(JsonWriter out, BinaryTagHolder value) throws IOException {
        out.value(value.string());
    }

    @Override
    public BinaryTagHolder read(JsonReader in) throws IOException {
        return BinaryTagHolder.binaryTagHolder(in.nextString());
    }
}