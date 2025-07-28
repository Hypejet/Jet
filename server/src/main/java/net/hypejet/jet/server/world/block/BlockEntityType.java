package net.hypejet.jet.server.world.block;

import net.hypejet.jet.server.registry.JetRegistryEntry;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * A type of Minecraft block entity.
 *
 * @param validBlocks a set blocks registry entries that support block entities of this type
 * @since 1.0
 */
public record BlockEntityType(@NonNull Set<JetRegistryEntry<JetBlockType>> validBlocks) {
    /**
     * Constructs the {@linkplain BlockEntityType block entity type}.
     *
     * @param validBlocks a set blocks registry entries that support block entities of this type
     * @since 1.0
     */
    public BlockEntityType {
        validBlocks = Set.copyOf(Objects.requireNonNull(validBlocks, "valid blocks"));
    }
}