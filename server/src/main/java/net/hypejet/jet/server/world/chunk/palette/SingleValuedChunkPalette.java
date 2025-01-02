package net.hypejet.jet.server.world.chunk.palette;

/**
 * Represents {@linkplain ChunkPalette a chunk palette}, which contains the same element at each position.
 *
 * @since 1.0
 * @see ChunkPalette
 */
public final class SingleValuedChunkPalette extends ChunkPalette {

    private static final long[] EMPTY_LONG_ARRAY = new long[0];

    private final int element;

    /**
     * Constructs the {@linkplain SingleValuedChunkPalette single-valued chunk palette}.
     *
     * @param element the element
     * @since 1.0
     */
    public SingleValuedChunkPalette(int element) {
        super((byte) 0, EMPTY_LONG_ARRAY);
        this.element = element;
    }

    @Override
    public int getElement(byte x, byte y, byte z) {
        return this.element;
    }

    /**
     * Gets the element that this chunk palette returns for each position.
     *
     * @return the element
     * @since 1.0
     */
    public int element() {
        return this.element;
    }
}