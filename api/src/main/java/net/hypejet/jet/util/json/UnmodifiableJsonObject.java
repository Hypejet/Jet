package net.hypejet.jet.util.json;

import com.google.gson.JsonObject;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a holder of {@linkplain JsonObject a json object}, which is intended be unmodifiable. In order to ensure
 * that, the json object is cloned during construction and every time it is being got a clone is returned.
 *
 * @param object the json object
 * @since 1.0
 */
public record UnmodifiableJsonObject(@NonNull JsonObject object) {
    /**
     * Constructs the {@linkplain UnmodifiableJsonObject unmodifiable json object}.
     *
     * @param object the json object
     * @since 1.0
     */
    public UnmodifiableJsonObject {
        object = NullabilityUtil.requireNonNull(object, "json object").deepCopy();
    }

    @Override
    public @NonNull JsonObject object() {
        return this.object.deepCopy();
    }
}