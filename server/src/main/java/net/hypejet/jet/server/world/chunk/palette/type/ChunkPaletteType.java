package net.hypejet.jet.server.world.chunk.palette.type;

import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;

/**
 * Represents a type of {@linkplain AbstractChunkPalette a chunk palette}.
 *
 * @since 1.0
 * @see AbstractChunkPalette
 */
public enum ChunkPaletteType {
    /**
     * {@linkplain ChunkPaletteType A chunk palette type} used when
     * {@linkplain AbstractChunkPalette a chunk palette} stores
     * {@linkplain net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState block states}.
     *
     * @since 1.0
     */
    BLOCK_STATE((byte) 4, (byte) 8, (byte) 15, (byte) 16),

    /**
     * {@linkplain ChunkPaletteType A chunk palette type} used when
     * {@linkplain AbstractChunkPalette a chunk palette} stores
     * {@linkplain net.hypejet.jet.data.model.api.registries.biome.Biome biomes}.
     *
     * @since 1.0
     */
    BIOME((byte) 1, (byte) 3, (byte) 6, (byte) 4);

    private final byte minimumIndirectBits;
    private final byte maximumIndirectBits;
    private final byte minimumDirectBits;

    private final byte axisLength;

    /**
     * Constructs the {@linkplain ChunkPaletteType chunk palette type}.
     *
     * @param minimumIndirectBits a minimum bits-per-entry number that indirect palettes of the type should use
     * @param maximumIndirectBits a maximum bits-per-entry number that indirect palettes of the type should use
     * @param minimumDirectBits a minimum bits-per-entry number that direct palettes of the type should use
     * @param axisLength an axis length that palettes of the type should use
     * @since 1.0
     */
    ChunkPaletteType(byte minimumIndirectBits, byte maximumIndirectBits, byte minimumDirectBits, byte axisLength) {
        this.minimumIndirectBits = minimumIndirectBits;
        this.maximumIndirectBits = maximumIndirectBits;
        this.minimumDirectBits = minimumDirectBits;

        /* Element count maps in chunk palettes use shorts to store number of block states, due to this, we need to
           make sure it is always safe. If Minecraft ever adds a new palette type or changes axis length, we must
           change map value types of the element count maps. */
        if (axisLength > 31)
            throw new IllegalArgumentException("The axis length cannot be higher than 31");
        this.axisLength = axisLength;
    }

    /**
     * Gets a minimum bits-per-entry number that indirect palettes of this type should use.
     *
     * @return the minimum bits-per-entry number
     * @since 1.0
     */
    public byte minimumIndirectBits() {
        return this.minimumIndirectBits;
    }

    /**
     * Gets a maximum bits-per-entry number that indirect palettes of this type should use.
     *
     * @return the maximum bits-per-entry number
     * @since 1.0
     */
    public byte maximumIndirectBits() {
        return this.maximumIndirectBits;
    }

    /**
     * Gets a minimum bits-per-entry number that direct palettes of this type should use.
     *
     * @return the minimum bits-per-entry number
     * @since 1.0
     */
    public byte minimumDirectBits() {
        return this.minimumDirectBits;
    }

    /**
     * Gets an axis length that palettes of this type should use.
     *
     * @return the axis length
     * @since 1.0
     */
    public byte axisLength() {
        return this.axisLength;
    }

    /**
     * Gets a maximum value that coordinate values of positions relative to chunk palettes of this type can use.
     *
     * @return the maximum value
     * @since 1.0
     */
    public byte maximumCoordinateValue() {
        return (byte) (this.axisLength - 1);
    }

    /**
     * Gets a number of elements that palettes of this type store.
     *
     * @return the number
     * @since 1.0
     */
    public short elementCount() {
        return (short) (this.axisLength * this.axisLength * this.axisLength);
    }

    /**
     * Validates whether a value specified is valid for a coordinate relative
     * to {@linkplain AbstractChunkPalette a chunk palette} of this type.
     *
     * @param value the value
     * @throws IllegalArgumentException if the value is not valid for the chunk-palette-relative coordinate
     * @since 1.0
     */
    public void validateCoordinateValue(byte value) {
        if (value >= this.axisLength || value < 0) {
            throw new IllegalArgumentException(String.format(
                    "Value %d is not a valid coordinate value for a chunk palette of type %s",
                    value, this
            ));
        }
    }
}