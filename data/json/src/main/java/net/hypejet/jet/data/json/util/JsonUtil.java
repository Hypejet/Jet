package net.hypejet.jet.data.json.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Represents a utility for JSON management.
 *
 * @since 1.0
 */
public final class JsonUtil {

    private JsonUtil() {}

    /**
     * Reads a {@linkplain Collection collection} from the specified {@linkplain JsonReader JSON reader}.
     *
     * @param reader the JSON reader
     * @param gson a gson instance to read collection values with
     * @param valueType a type of the collection values
     * @param collection a collection to put the read values to
     * @param <V> a type of the collection values
     * @throws IOException when an I/O error occurs
     * @since 1.0
     */
    public static <V> void readCollection(
            @NotNull JsonReader reader, @NotNull Gson gson,
            @NotNull Type valueType, @NotNull Collection<V> collection
    ) throws IOException {
        reader.beginArray();
        while (reader.peek() != JsonToken.END_ARRAY)
            collection.add(gson.fromJson(reader, valueType));
        reader.endArray();
    }

    /**
     * Writes a {@linkplain Collection collection} to the specified {@linkplain JsonWriter JSON writer}.
     *
     * @param writer the JSON writer
     * @param gson a gson instance to write collection values with
     * @param valueType a type of the collection values
     * @param collection a collection to be written
     * @param <V> a type of the collection values
     * @throws IOException when an I/O error occurs
     * @since 1.0
     */
    public static <V> void writeCollection(
            @NotNull JsonWriter writer, @NotNull Gson gson,
            @NotNull Type valueType, @NotNull Collection<V> collection
    ) throws IOException {
        writer.beginArray();
        collection.forEach(value -> gson.toJson(value, valueType, writer));
        writer.endArray();
    }
}