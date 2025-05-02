package net.hypejet.jet.server.world.chunk.heightmap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.storage.BitStorage;
import net.hypejet.jet.server.util.storage.BitStorageUpdate;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a map, which stores highest height for each column of {@linkplain JetChunk a chunk}, which is considered
 * {@linkplain HeightMapType#isOpaque(BlockState) opaque} by {@linkplain HeightMapType a type of the height map}.
 *
 * <p>It is used mainly by Minecraft client for client-side optimizations.</p>
 *
 * @since 1.0
 * @see JetChunk
 * @see HeightMapType#isOpaque(BlockState)
 * @see HeightMapType
 */
public final class HeightMap {

    private final DimensionType dimensionType;
    private final HeightMapType type;

    private final BitStorage data;

    /**
     * Constructs the {@linkplain HeightMap height map}.
     *
     * @param dimensionType a dimension type of world associated with a chunk that the height map is created for
     * @param type a type of which the height map should be
     * @param data a bit storage containing data that the height map should have
     * @since 1.0
     */
    private HeightMap(@NonNull DimensionType dimensionType, @NonNull HeightMapType type, @NonNull BitStorage data) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.type = NullabilityUtil.requireNonNull(type, "type");
        this.data = NullabilityUtil.requireNonNull(data, "data");
    }

    /**
     * Gets {@linkplain HeightMapType a height map type} of this height map.
     *
     * @return the height map type
     * @since 1.0
     */
    public @NonNull HeightMapType type() {
        return this.type;
    }

    /**
     * Gets {@linkplain BitStorage a bit storage} that stores data of this height map.
     *
     * @return the bit storage
     * @since 1.0
     */
    public @NonNull BitStorage data() {
        return this.data;
    }

    /**
     * Gets an absolute {@code block Y} coordinate value of a height that is stored for chunk-relative {@code block X}
     * and chunk-relative {@code block Z} coordinate values by this {@linkplain HeightMap height map}.
     *
     * @param blockX the chunk-relative {@code block X} coordinate value
     * @param blockZ the chunk-relative {@code block Z} coordinate value
     * @param dimensionType a dimension type of chunk that this height map belongs to
     * @return the absolute {@code block Y} coordinate value of the height
     * @since 1.0
     */
    @Contract(pure = true)
    public int getBlockY(byte blockX, byte blockZ, @NonNull DimensionType dimensionType) {
        int elementIndex = createElementIndex(blockX, blockZ);
        int element = this.data.getElement(elementIndex);
        return toBlockY(element, dimensionType);
    }

    /**
     * Creates a new copy of this {@linkplain HeightMap height map}, which takes into account block state updates
     * specified.
     *
     * @param chunkSectionList a chunk section list with the updates taken into account, used when a new height
     *                         needs to be found
     * @param updates a map which maps chunk-relative block positions to new block states that should be present there
     * @return the new height map
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull HeightMap withUpdates(@NonNull ChunkSectionList chunkSectionList,
                                          @NonNull Map<ChunkRelativeBlockPosition, BlockState> updates) {
        if (updates.isEmpty())
            return this;

        Int2ObjectMap<BitStorageUpdate> dataUpdates = new Int2ObjectOpenHashMap<>();
        for (Map.Entry<ChunkRelativeBlockPosition, BlockState> update : updates.entrySet()) {
            ChunkRelativeBlockPosition position = update.getKey();

            byte blockX = position.relativeX();
            byte blockZ = position.relativeZ();

            int previousBlockY = this.getBlockY(blockX, blockZ, this.dimensionType);
            int newBlockY = position.absoluteY();

            if (previousBlockY > newBlockY)
                continue;

            int elementIndex = createElementIndex(blockX, blockZ);
            if (this.type.isOpaque(update.getValue())) {
                int element = toElement(newBlockY, this.dimensionType);
                dataUpdates.put(elementIndex, new BitStorageUpdate(elementIndex, element));
                continue;
            }

            if (newBlockY != previousBlockY)
                continue;

            dataUpdates.put(elementIndex, new BitStorageUpdate(
                    elementIndex,
                    findHeightAndCreateElement(
                            newBlockY - 1, chunkSectionList,
                            this.dimensionType, blockX, blockZ, this.type
                    )
            ));
        }

        if (dataUpdates.isEmpty())
            return this;

        BitStorageUpdate[] updateArray = dataUpdates.values().toArray(BitStorageUpdate[]::new);
        return new HeightMap(this.dimensionType, this.type, this.data.withUpdates(updateArray));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeightMap otherHeightMap)) return false;
        return Objects.equals(this.dimensionType, otherHeightMap.dimensionType)
                && this.type == otherHeightMap.type
                && Objects.equals(this.data, otherHeightMap.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dimensionType, this.type, this.data);
    }

    @Override
    public String toString() {
        return "HeightMap{" +
                "dimensionType=" + this.dimensionType +
                ", type=" + this.type +
                ", data=" + this.data +
                '}';
    }

    /**
     * Creates {@linkplain HeightMap a height map} for {@linkplain ChunkSectionList a chunk section list}
     * specified of {@linkplain JetChunk a chunk}
     * with {@linkplain DimensionType a dimension type} specified.
     *
     * @param type a type that the height map should have
     * @param chunkSectionList the chunk section list
     * @param dimensionType the dimension type
     * @return the height map
     * @since 1.0
     */
    @Contract(pure = true, value = "_, _, _ -> new")
    public static @NonNull HeightMap create(@NonNull HeightMapType type, @NonNull ChunkSectionList chunkSectionList,
                                            @NonNull DimensionType dimensionType) {
        byte axisLength = ChunkPaletteType.BLOCK_STATE.axisLength();
        int[] elements = new int[axisLength * axisLength];

        int height = dimensionType.height();
        for (byte blockX = 0; blockX < axisLength; blockX++) {
            for (byte blockZ = 0; blockZ < axisLength; blockZ++) {
                elements[createElementIndex(blockX, blockZ)] = findHeightAndCreateElement(
                        dimensionType.minY() + height - 1, chunkSectionList,
                        dimensionType, blockX, blockZ, type
                );
            }
        }

        // Heightmaps increment each Y coordinate by 1, so the highest count of bits used is height of the world
        byte bitsPerElement = (byte) MathUtil.bitCount(height);
        return new HeightMap(dimensionType, type, new BitStorage(bitsPerElement, elements));
    }

    private static int findHeightAndCreateElement(int startingBlockY, @NonNull ChunkSectionList chunkSectionList,
                                                  @NonNull DimensionType dimensionType, byte blockX, byte blockZ,
                                                  @NonNull HeightMapType heightMapType) {
        for (int blockY = startingBlockY; blockY >= dimensionType.minY(); blockY--) {
            ChunkRelativeBlockPosition position = new ChunkRelativeBlockPosition(blockX, blockY, blockZ);

            JetChunkSection section = chunkSectionList.sectionFor(position);
            AbstractChunkPalette<BlockState> blockStatePalette = section.blockStatePalette();

            BlockState blockState = blockStatePalette.getElement(ChunkPaletteRelativePosition.from(position));
            if (heightMapType.isOpaque(blockState))
                return toElement(blockY, dimensionType);
        }

        return 0; // 0 indicates that there is no block state in a column that satisfies the opaque check
    }

    private static int createElementIndex(byte blockX, byte blockZ) {
        return blockX + blockZ * ChunkPaletteType.BLOCK_STATE.axisLength();
    }

    private static int toElement(int blockY, @NonNull DimensionType dimensionType) {
        return blockY + 1 - dimensionType.minY();
    }

    private static int toBlockY(int element, @NonNull DimensionType dimensionType) {
        return element - 1 + dimensionType.minY();
    }
}