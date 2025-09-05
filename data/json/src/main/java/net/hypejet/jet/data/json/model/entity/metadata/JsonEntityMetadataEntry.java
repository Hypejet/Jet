package net.hypejet.jet.data.json.model.entity.metadata;

import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An entry of {@linkplain JsonEntityMetadata entity metadata}.
 *
 * @param index an index that the metadata entry is bound to
 * @param value the value of the metadata entry
 * @since 1.0
 * @see JsonEntityMetadata
 */
public record JsonEntityMetadataEntry(@Range(from = 0, to = 254) short index, @NonNull JsonEntityMetadataValue value) {
    /**
     * Constructs the {@linkplain JsonEntityMetadataEntry entity metadata entry}.
     *
     * @param index an index that the metadata entry should be bound to
     * @param value the value that the metadata entry should have
     * @since 1.0
     */
    public JsonEntityMetadataEntry {
        Objects.requireNonNull(value, "value");
    }
}