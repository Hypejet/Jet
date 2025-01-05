package net.hypejet.jet.server.world.chunk.palette.usage;

/**
 * Represents a usage type of {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.palette.ChunkPalette
 */
public enum ChunkPaletteUsageType {
    /**
     * {@linkplain ChunkPaletteUsageType A chunk palette usage type} used when
     * {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette} stores
     * {@linkplain ??? blocks}.
     *
     * @since 1.0
     */
    BLOCK((byte) 4, (byte) 8, (byte) 15, (byte) 16),

    /**
     * {@linkplain ChunkPaletteUsageType A chunk palette usage type} used when
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
     * Constructs the {@linkplain ChunkPaletteUsageType chunk palette usage type}.
     *
     * @param minimumIndirectBits a minimum bits-per-entry number that indirect palettes with the usage should use
     * @param maximumIndirectBits a maximum bits-per-entry number that indirect palettes with the usage should use
     * @param minimumDirectBits a minimum bits-per-entry number that direct palettes with the usage should use
     * @param axisLength an axis length that palettes with the usage should use
     * @since 1.0
     */
    ChunkPaletteUsageType(byte minimumIndirectBits, byte maximumIndirectBits, byte minimumDirectBits, byte axisLength) {
        this.minimumIndirectBits = minimumIndirectBits;
        this.maximumIndirectBits = maximumIndirectBits;
        this.minimumDirectBits = minimumDirectBits;
        this.axisLength = axisLength;
    }

    /**
     * Gets a minimum bits-per-entry number that indirect palettes with this usage should use.
     *
     * @return the minimum bits-per-entry number
     * @since 1.0
     */
    public byte minimumIndirectBits() {
        return this.minimumIndirectBits;
    }

    /**
     * Gets a maximum bits-per-entry number that indirect palettes with this usage should use.
     *
     * @return the maximum bits-per-entry number
     * @since 1.0
     */
    public byte maximumIndirectBits() {
        return this.maximumIndirectBits;
    }

    /**
     * Gets a minimum bits-per-entry number that direct palettes with this usage should use.
     *
     * @return the minimum bits-per-entry number
     * @since 1.0
     */
    public byte minimumDirectBits() {
        return this.minimumDirectBits;
    }

    /**
     * Gets an axis length that palettes with this usage should use.
     *
     * @return the axis length
     * @since 1.0
     */
    public byte axisLength() {
        return this.axisLength;
    }
}