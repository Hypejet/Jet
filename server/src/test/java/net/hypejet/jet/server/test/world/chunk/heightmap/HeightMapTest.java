package net.hypejet.jet.server.test.world.chunk.heightmap;

import it.unimi.dsi.fastutil.bytes.ByteBytePair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.test.world.chunk.ChunkTestUtil;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.builder.JetChunkBuilder;
import net.hypejet.jet.server.world.chunk.factory.palette.JetChunkPaletteFactory;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMapType;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.hypejet.jet.world.dimension.DimensionType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * A test of {@linkplain HeightMap height maps}.
 *
 * @since 1.0
 * @see HeightMap
 */
public final class HeightMapTest {

    private static final Holder.Reference<Biome> BIOME = new Holder.Reference<>(Key.key("biome", "mockup"));

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

    private static final JetChunkPaletteFactory<BlockState> BLOCK_STATE_PALETTE_FACTORY
            = new JetChunkPaletteFactory<>(ChunkPaletteType.BLOCK_STATE, new BlockStateIndexSpecification());
    private static final JetChunkPaletteFactory<Holder.Reference<Biome>> BIOME_PALETTE_FACTORY
            = new JetChunkPaletteFactory<>(ChunkPaletteType.BIOME, new BiomeIndexSpecification());

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
        JetChunkBuilder chunkBuilder = new JetChunkBuilder(
                dimensionType,
                BLOCK_STATE_PALETTE_FACTORY,
                BIOME_PALETTE_FACTORY,
                AIR, BIOME, null
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

        JetChunkBuilder chunkBuilder = new JetChunkBuilder(
                ChunkTestUtil.DIMENSION_TYPE,
                BLOCK_STATE_PALETTE_FACTORY,
                BIOME_PALETTE_FACTORY,
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

    /**
     * A {@linkplain AbstractChunkPalette.IndexSpecification chunk-palette index specification}
     * providing registry indices of mockup {@linkplain BlockState block states} created for
     * a {@linkplain HeightMapTest height map test}.
     *
     * @since 1.0
     * @see BlockState
     * @see AbstractChunkPalette.IndexSpecification
     */
    private static final class BlockStateIndexSpecification
            implements AbstractChunkPalette.IndexSpecification<BlockState> {

        private static final int AIR_INDEX = 0;
        private static final int SURFACE_INDEX = 1;
        private static final int MOTION_BLOCKER_INDEX = 2;
        private static final int FLUID_INDEX = 3;

        @Override
        public int indexFor(@NonNull BlockState value) {
            if (value.equals(AIR)) {
                return AIR_INDEX;
            } else if (value.equals(SURFACE)) {
                return SURFACE_INDEX;
            } else if (value.equals(MOTION_BLOCKER)) {
                return MOTION_BLOCKER_INDEX;
            } else if (value.equals(FLUID)) {
                return FLUID_INDEX;
            } else {
                throw new IllegalArgumentException(
                        "This index specification does not provide an index for the specified block state: " + value
                );
            }
        }

        @Override
        public @NonNull BlockState valueByIndex(int index) {
            return switch (index) {
                case AIR_INDEX -> AIR;
                case SURFACE_INDEX -> SURFACE;
                case MOTION_BLOCKER_INDEX -> MOTION_BLOCKER;
                case FLUID_INDEX -> FLUID;
                default -> throw new IllegalArgumentException(String.format(
                        "This index specification does not provide a value for index of %d",
                        index
                ));
            };
        }
    }

    /**
     * A {@linkplain AbstractChunkPalette.IndexSpecification chunk-palette index specification}
     * providing registry indices of mockup {@linkplain Biome biomes} created for
     * a {@linkplain HeightMapTest height map test}.
     *
     * @since 1.0
     * @see Biome
     * @see AbstractChunkPalette.IndexSpecification
     */
    private static final class BiomeIndexSpecification
            implements AbstractChunkPalette.IndexSpecification<Holder.Reference<Biome>> {

        private static final int BIOME_INDEX = 0;

        @Override
        public int indexFor(Holder.@NonNull Reference<Biome> value) {
            if (!value.equals(BIOME)) {
                throw new IllegalArgumentException(String.format(
                        "This index specification does not provide an index for \"%s\" biome",
                        value.key()
                ));
            }
            return BIOME_INDEX;
        }

        @Override
        public Holder.@NonNull Reference<Biome> valueByIndex(int index) {
            if (index != BIOME_INDEX) {
                throw new IllegalArgumentException(String.format(
                        "This index specification does not provide a value for index of %d",
                        index
                ));
            }
            return BIOME;
        }
    }
}