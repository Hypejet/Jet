package net.hypejet.jet.server.world.chunk.palette.type;

/**
 * Represents a type of {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.palette.ChunkPalette
 */
public enum ChunkPaletteType {
    /**
     * {@linkplain ChunkPaletteType A chunk palette type} used when
     * {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette} stores
     * {@linkplain net.hypejet.jet.server.world.block.JetBlockState block states}.
     *
     * @since 1.0
     */
    BLOCK_STATE((byte) 4, (byte) 8, (byte) 15, (byte) 16),

    /**
     * {@linkplain ChunkPaletteType A chunk palette type} used when
     * {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette} stores
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
     * Gets a count of elements that palettes of this type store.
     *
     * @return the count
     * @since 1.0
     */
    public int elementCount() {
        return this.axisLength * this.axisLength * this.axisLength;
    }
}