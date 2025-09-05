package net.hypejet.jet.data.json.model.entity.metadata;

import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * A metadata of a Minecraft entity.
 *
 * @param entries a set of the metadata entries
 * @since 1.0
 */
public record JsonEntityMetadata(@NonNull Set<JsonEntityMetadataEntry> entries) {
    /**
     * Constructs the {@linkplain JsonEntityMetadata entity metadata}.
     *
     * @param entries a set of the entries that the entity metadata should have
     * @since 1.0
     */
    public JsonEntityMetadata {
        entries = Set.copyOf(Objects.requireNonNull(entries, "entries"));
    }
}