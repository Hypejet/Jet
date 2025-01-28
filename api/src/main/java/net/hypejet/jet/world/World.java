package net.hypejet.jet.world;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

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
    @NonNull UUID uniqueId();

    /**
     * Gets {@linkplain RegistryEntry a registry entry} of {@link DimensionType a dimension type} that this world uses.
     *
     * @return the registry entry
     * @since 1.0
     */
    @NonNull RegistryEntry<DimensionType> dimensionType();

    /**
     * Creates {@linkplain NotNullObjectAcquisition a not-null object acquisition} of
     * {@linkplain BlockState a block state} at {@linkplain BlockPosition a block position} specified.
     *
     * @param position the block position
     * @return the not-null object acquisition created
     * @since 1.0
     */
    @NonNull NotNullObjectAcquisition<? extends BlockState> getBlockState(@NonNull BlockPosition position);

    /**
     * Sets {@linkplain BlockState a block state} at {@linkplain BlockPosition a block position} specified.
     *
     * @param position the block position
     * @param blockState a bloc state to replace current block state at the position with
     * @since 1.0
     */
    void setBlockState(@NonNull BlockPosition position, @NonNull BlockState blockState);
}