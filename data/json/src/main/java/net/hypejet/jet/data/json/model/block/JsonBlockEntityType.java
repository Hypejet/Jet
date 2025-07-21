package net.hypejet.jet.data.json.model.block;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * A type of Minecraft block entity.
 *
 * @param validBlocks a set of keys of blocks that support block entities of this type
 * @since 1.0
 */
public record JsonBlockEntityType(@NonNull Set<Key> validBlocks) {
    /**
     * Constructs the {@linkplain JsonBlockEntityType block entity type}.
     *
     * @param validBlocks a set of keys of blocks that support block entities of this type
     * @since 1.0
     */
    public JsonBlockEntityType {
        validBlocks = Set.copyOf(Objects.requireNonNull(validBlocks, "valid blocks"));
    }
}