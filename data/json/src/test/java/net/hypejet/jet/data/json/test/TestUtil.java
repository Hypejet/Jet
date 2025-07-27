package net.hypejet.jet.data.json.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.DataJson;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;

/**
 * GSON conversion testing utilities.
 *
 * @since 1.0
 */
final class TestUtil {

    private TestUtil() {}

    /**
     * Tests GSON conversion of the specified object.
     *
     * @param object the object to test
     * @param <T> a type of the object to test
     * @since 1.0
     */
    static <T> void test(@NonNull T object) {
        test(object, TypeToken.get(object.getClass()));
    }

    /**
     * Tests GSON conversion of the specified object.
     *
     * @param object the object to test
     * @param typeToken a type token of the object to test
     * @param <T> a type of the object to test
     * @since 1.0
     */
    static <T> void test(@NonNull T object, @NonNull TypeToken<? extends T> typeToken) {
        Gson gson = DataJson.GSON;
        Object deserializedObject = gson.fromJson(gson.toJson(object, typeToken.getType()), typeToken);
        Assertions.assertEquals(object, deserializedObject);
    }
}