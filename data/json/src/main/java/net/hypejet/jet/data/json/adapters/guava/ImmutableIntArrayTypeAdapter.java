package net.hypejet.jet.data.json.adapters.guava;

import com.google.common.primitives.ImmutableIntArray;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain ImmutableIntArray immutable int arrays}.
 *
 * @since 1.0
 * @see ImmutableIntArray
 * @see TypeAdapter
 */
final class ImmutableIntArrayTypeAdapter extends TypeAdapter<ImmutableIntArray> {
    /**
     * An instance of the {@linkplain ImmutableIntArrayTypeAdapter immutable int array type adapter}.
     *
     * @since 1.0
     */
    static final ImmutableIntArrayTypeAdapter INSTANCE = new ImmutableIntArrayTypeAdapter();

    private ImmutableIntArrayTypeAdapter() {}

    @Override
    public void write(JsonWriter out, ImmutableIntArray value) throws IOException {
        out.beginArray();
        for (int element : value.toArray())
            out.value(element);
        out.endArray();
    }

    @Override
    public ImmutableIntArray read(JsonReader in) throws IOException {
        in.beginArray();

        ImmutableIntArray.Builder builder = ImmutableIntArray.builder();
        while (in.peek() != JsonToken.END_ARRAY)
            builder.add(in.nextInt());

        in.endArray();
        return builder.build();
    }
}