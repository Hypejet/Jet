package net.hypejet.jet.world.block.slab;

/**
 * The type how slab block occupies the block position where it is placed.
 *
 * <p>Contents of this enum depend on Minecraft, however it is safe to keep
 * it an enum, since it is very unlikely to change.</p>
 *
 * @since 1.0
 */
public enum SlabOccupancyType {
    /**
     * A {@linkplain SlabOccupancyType slab occupancy type} indicating
     * that a slab block occupies the higher half of the block.
     *
     * @since 1.0
     */
    TOP,
    /**
     * A {@linkplain SlabOccupancyType slab occupancy type} indicating
     * that a slab block occupies the lower half of the block.
     *
     * @since 1.0
     */
    BOTTOM,/**
     * A {@linkplain SlabOccupancyType slab occupancy type} indicating that a slab
     * block occupies both halves of the block, meaning that the block consists of two slabs.
     *
     * @since 1.0
     */
    DOUBLE
}