package net.hypejet.jet.server.test.world.chunk.heightmap;

import it.unimi.dsi.fastutil.bytes.ByteBytePair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.test.world.chunk.ChunkTestUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.builder.JetChunkBuilder;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMapType;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * Represents a test of {@linkplain HeightMap a height map}.
 *
 * @since 1.0
 * @see HeightMap
 */
public final class HeightMapTest {

    private static final JetRegistryEntry<Biome> BIOME = ChunkTestUtil.createMockupBiome(Key.key("mockup"));
    private static final ElementOrder<JetRegistryEntry<Biome>> BIOME_ORDER = new ElementOrder<>(List.of(BIOME));

    private static final JetBlockState AIR = ChunkTestUtil.createMockupBlockState(
            Key.key("air"), true, false, false
    );

    private static final JetBlockState SURFACE = ChunkTestUtil.createMockupBlockState(
            Key.key("surface"), false, false, false
    );

    private static final JetBlockState MOTION_BLOCKER = ChunkTestUtil.createMockupBlockState(
            Key.key("motion_blocker"), false, false, true
    );

    private static final JetBlockState FLUID = ChunkTestUtil.createMockupBlockState(
            Key.key("fluid"), false, true, false
    );

    @Test
    public void testWorldSurfaceCreation() {
        testCreation(HeightMapType.WORLD_SURFACE, SURFACE);
    }

    @Test
    public void testMotionBlockingCreation() {
        testCreation(HeightMapType.MOTION_BLOCKING, MOTION_BLOCKER);
        testCreation(HeightMapType.MOTION_BLOCKING, FLUID);
    }

    @Test
    public void testWorldSurfaceUpdating() {
        testUpdating(HeightMapType.WORLD_SURFACE, SURFACE);
    }

    @Test
    public void testMotionBlockingUpdating() {
        testUpdating(HeightMapType.MOTION_BLOCKING, MOTION_BLOCKER);
        testUpdating(HeightMapType.MOTION_BLOCKING, FLUID);
    }

    private static void testCreation(@NonNull HeightMapType type, @NonNull JetBlockState opaqueBlockState) {
        ensureAirNotOpaque(type);
        ensureOpaque(type, opaqueBlockState);

        DimensionType dimensionType = ChunkTestUtil.DIMENSION_TYPE;
        ElementOrder<JetBlockState> blockStateOrder = new ElementOrder<>(List.of(AIR, opaqueBlockState));

        JetRegistryEntry<Biome> testBiome = ChunkTestUtil.createMockupBiome(Key.key("test"));
        ElementOrder<JetRegistryEntry<Biome>> biomeOrder = new ElementOrder<>(List.of(testBiome));

        JetChunkBuilder chunkBuilder = new JetChunkBuilder(
                dimensionType, blockStateOrder, biomeOrder,
                HeightMapTest.AIR, testBiome, null
        );

        Object2IntMap<ByteBytePair> expectedHeights = new Object2IntOpenHashMap<>();

        int minY = dimensionType.minY();
        int maxY = minY + dimensionType.height() - 1;

        byte axisLength = ChunkPaletteType.BLOCK_STATE.axisLength();
        for (byte x = 0; x < axisLength; x++) {
            for (byte z = 0; z < axisLength; z++) {
                int height = Math.clamp(x * z - x + z, minY, maxY);
                ChunkRelativeBlockPosition position = new ChunkRelativeBlockPosition(x, height, z);
                chunkBuilder.setBlockState(position, opaqueBlockState);
                expectedHeights.put(ByteBytePair.of(x, z), height);
            }
        }

        HeightMap heightMap = chunkBuilder.build().heightMaps().get(type);
        for (byte x = 0; x < axisLength; x++) {
            for (byte z = 0; z < axisLength; z++) {
                int expectedY = expectedHeights.getInt(ByteBytePair.of(x, z));
                int actualY = heightMap.getBlockY(x, z);
                Assertions.assertEquals(expectedY, actualY);
            }
        }
    }

    private static void testUpdating(@NonNull HeightMapType type, @NonNull JetBlockState opaqueBlockState) {
        ensureAirNotOpaque(type);
        ensureOpaque(type, opaqueBlockState);

        DimensionType dimensionType = ChunkTestUtil.DIMENSION_TYPE;
        ElementOrder<JetBlockState> blockStateOrder = new ElementOrder<>(List.of(AIR, opaqueBlockState));

        JetChunkBuilder chunkBuilder = new JetChunkBuilder(
                dimensionType, blockStateOrder, BIOME_ORDER,
                AIR, BIOME, null
        );

        ChunkRelativeBlockPosition position = new ChunkRelativeBlockPosition((byte) 5, 32, (byte) 4);
        chunkBuilder.setBlockState(position, opaqueBlockState);

        ChunkSectionList sectionList = chunkBuilder.build().chunkSectionList();
        HeightMap heightMap = HeightMap.create(type, sectionList);

        int initialHeight = heightMap.getBlockY(position.relativeX(), position.relativeZ());
        Assertions.assertEquals(position.absoluteY(), initialHeight);

        ChunkRelativeBlockPosition updatePosition = new ChunkRelativeBlockPosition(
                position.relativeX(), position.absoluteY() + 1, position.relativeZ()
        );

        Map<ChunkRelativeBlockPosition, JetBlockState> updates = Map.of(updatePosition, opaqueBlockState);

        ChunkSectionList updatedSectionList = sectionList.withUpdates(updates, Map.of());
        HeightMap updatedHeightMap = heightMap.withUpdates(updatedSectionList, updates);

        int updatedHeight = updatedHeightMap.getBlockY(position.relativeX(), position.relativeZ());
        Assertions.assertEquals(updatePosition.absoluteY(), updatedHeight);
    }

    private static void ensureAirNotOpaque(@NonNull HeightMapType type) {
        if (type.isOpaque(AIR))
            throw new IllegalArgumentException("The air block state is considered as opaque");
    }

    private static void ensureOpaque(@NonNull HeightMapType type, @NonNull JetBlockState opaqueBlockState) {
        if (!type.isOpaque(opaqueBlockState)) {
            throw new IllegalArgumentException(
                    "The opaque block state must be considered as opaque by the height map type specified"
            );
        }
    }
}