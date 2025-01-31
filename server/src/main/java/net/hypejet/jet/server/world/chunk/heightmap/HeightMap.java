package net.hypejet.jet.server.world.chunk.heightmap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.storage.BitStorage;
import net.hypejet.jet.server.util.storage.BitStorageUpdate;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSection;
import net.hypejet.jet.server.world.chunk.update.BlockStateUpdate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Objects;

/**
 * Represents a map, which stores highest height for each column of
 * {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}, which is considered
 * {@linkplain HeightMapType#isOpaque(JetBlockState) opaque} by {@linkplain HeightMapType a type of the height map}.
 *
 * <p>It is used mainly by Minecraft client for client-side optimizations.</p>
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 * @see HeightMapType#isOpaque(JetBlockState)
 * @see HeightMapType
 */
public final class HeightMap {

    private final HeightMapType type;
    private final BitStorage data;

    /**
     * Constructs the {@linkplain HeightMap height map}.
     *
     * @param type a type of which the height map should be
     * @param data a bit storage containing data that the height map should have
     * @since 1.0
     */
    private HeightMap(@NonNull HeightMapType type, @NonNull BitStorage data) {
        this.type = NullabilityUtil.requireNonNull(type, "type");
        this.data = NullabilityUtil.requireNonNull(data, "data");
    }

    /**
     * Gets {@linkplain HeightMapType a height map type} of this height map.
     *
     * @return the height map type
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull HeightMapType type() {
        return this.type;
    }

    /**
     * Gets {@linkplain BitStorage a bit storage} that stores data of this height map.
     *
     * @return the bit storage
     * @since 1.0
     */
    @Contract(pure = true)
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
     * @param dimensionType a dimension type of world associated with a chunk that the height maps are created for
     * @param sections a chunk section list with the updates taken into account, used when a new height needs to be
     *                 found
     * @param updates the block state updates
     * @return the new height map
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull HeightMap withUpdates(@NonNull DimensionType dimensionType, @NonNull List<ChunkSection> sections,
                                          @NonNull BlockStateUpdate @NonNull ... updates) {
        if (updates.length == 0)
            return this;

        Int2ObjectMap<BitStorageUpdate> dataUpdates = new Int2ObjectOpenHashMap<>();
        for (BlockStateUpdate update : updates) {
            byte blockX = update.blockX();
            byte blockZ = update.blockZ();

            int previousBlockY = this.getBlockY(blockX, blockZ, dimensionType);
            int newBlockY = update.blockY();

            if (previousBlockY > newBlockY)
                continue;

            int elementIndex = createElementIndex(blockX, blockZ);
            if (this.type.isOpaque(update.blockState())) {
                int element = toElement(newBlockY, dimensionType);
                dataUpdates.put(elementIndex, new BitStorageUpdate(elementIndex, element));
                continue;
            }

            if (newBlockY != previousBlockY)
                continue;

            dataUpdates.put(elementIndex, new BitStorageUpdate(
                    elementIndex,
                    findHeightAndCreateElement(newBlockY - 1, sections, dimensionType, blockX, blockZ, this.type)
            ));
        }

        BitStorageUpdate[] updateArray = dataUpdates.values().toArray(BitStorageUpdate[]::new);
        return new HeightMap(this.type, this.data.withUpdates(updateArray));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HeightMap otherHeightMap)) return false;
        return this.type == otherHeightMap.type && Objects.equals(this.data, otherHeightMap.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.data);
    }

    @Override
    public String toString() {
        return "HeightMap{" +
                "type=" + this.type +
                ", data=" + this.data +
                '}';
    }

    /**
     * Creates {@linkplain HeightMap a height map} for {@linkplain List a list}
     * of {@linkplain ChunkSection chunk sections} specified of
     * {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk} with {@linkplain DimensionType a dimension type}
     * specified. The list should have the same order as sections in the chunk have.
     *
     * @param type a type that the height map should have
     * @param sections the chunk section list
     * @param dimensionType the dimension type
     * @return the height map
     * @since 1.0
     */
    @Contract(pure = true, value = "_, _, _ -> new")
    public static @NonNull HeightMap create(@NonNull HeightMapType type, @NonNull List<ChunkSection> sections,
                                            @NonNull DimensionType dimensionType) {
        byte axisLength = ChunkPaletteType.BLOCK_STATE.axisLength();
        int[] elements = new int[axisLength * axisLength];

        int minY = dimensionType.minY();
        int height = dimensionType.height();

        for (byte blockX = 0; blockX < axisLength; blockX++) {
            for (byte blockZ = 0; blockZ < axisLength; blockZ++) {
                elements[createElementIndex(blockX, blockZ)] = findHeightAndCreateElement(
                        height + minY - 1, sections,
                        dimensionType, blockX, blockZ, type
                );
            }
        }

        byte bitsPerElement = (byte) MathUtil.bitCount(height); // Heightmaps increment each Y coordinate by 1
        return new HeightMap(type, new BitStorage(bitsPerElement, elements));
    }

    private static int findHeightAndCreateElement(int startingBlockY, @NonNull List<ChunkSection> sections,
                                                  @NonNull DimensionType dimensionType, byte blockX, byte blockZ,
                                                  @NonNull HeightMapType heightMapType) {
        for (int blockY = startingBlockY; blockY >= dimensionType.minY(); blockY--) {
            int sectionIndex = JetWorld.createChunkSectionIndex(blockY, dimensionType);
            ChunkSection section = sections.get(sectionIndex);

            if (section == null) {
                throw new IllegalArgumentException(String.format(
                        "Could not find a chunk section with index of %s",
                        sectionIndex
                ));
            }

            ChunkPalette<JetBlockState> blockStatePalette = section.blockStatePalette();
            byte sectionRelativeY = JetWorld.createSectionRelativeCoordinate(blockY);

            JetBlockState blockState = blockStatePalette.getElement(blockX, sectionRelativeY, blockZ);
            if (heightMapType.isOpaque(blockState))
                return toElement(blockY, dimensionType);
        }

        return 0;
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