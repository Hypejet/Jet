package net.hypejet.jet.world.block.entity;

import net.hypejet.jet.registry.holder.HolderSet;
import net.hypejet.jet.world.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

/**
 * A type of Minecraft block entity.
 *
 * @since 1.0
 */
@ApiStatus.NonExtendable
public interface BlockEntityType {
    /**
     * Gets a {@linkplain HolderSet holder set} referencing to block
     * registry entries that support block entities of this type.
     *
     * @return the holder set
     * @since 1.0
     */
    @NonNull HolderSet<BlockType> validBlocks();
}