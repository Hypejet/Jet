package net.hypejet.jet.server.util.data;

import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.DataJson;
import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Utilities for reading Jet data JSON resource files.
 *
 * @since 1.0
 */
public final class JetDataUtil {

    private JetDataUtil() {}

    /**
     * Deserializes a {@linkplain JsonRegistryEntry registry-entry} {@linkplain List list} from contents
     * of the specified resource file.
     *
     * @param classpath the classpath of the resource file that the list should be deserialized from
     * @param valueType the value type of the registry entries
     * @return the registry entry
     * @param <V> the value type of the registry entries
     * @since 1.0
     */
    public static <V> @NonNull List<JsonRegistryEntry<V>> deserializeEntries(@NonNull String classpath,
                                                                             @NonNull Type valueType) {
        try (InputStream stream = ClassLoader.getSystemResourceAsStream(classpath)) {
            if (stream == null)
                throw new IllegalArgumentException("Resource file with classpath " + classpath + " does not exist");
            return DataJson.GSON.fromJson(
                    new InputStreamReader(stream),
                    TypeToken.getParameterized(
                            List.class,
                            TypeToken.getParameterized(JsonRegistryEntry.class, valueType).getType()
                    ).getType()
            );
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}