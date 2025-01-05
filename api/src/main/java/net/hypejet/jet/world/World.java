package net.hypejet.jet.world;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a Minecraft world.
 *
 * @since 1.0
 */
public interface World {
    /**
     * Gets a unique identifier of this world.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NotNull UUID uniqueId();

    /**
     * Gets {@linkplain RegistryEntry a registry entry} of {@link DimensionType a dimension type} that this world uses.
     *
     * @return the registry entry
     * @since 1.0
     */
    @NotNull RegistryEntry<DimensionType> dimensionType();

    /**
     * Creates {@linkplain NotNullObjectAcquisition a not-null object acquisition} of {@linkplain ??? a block}
     * at {@linkplain BlockPosition a block position} specified.
     *
     * @param position the block position
     * @return the not-null object acquisition created
     * @since 1.0
     */
    @NotNull NotNullObjectAcquisition<Integer> getBlock(@NotNull BlockPosition position);

    /**
     * Sets {@linkplain ??? a block} at {@linkplain BlockPosition a block position} specified.
     *
     * @param position the block position
     * @param block a block to replace current block at the position with
     * @since 1.0
     */
    void setBlock(@NotNull BlockPosition position, int block);
}