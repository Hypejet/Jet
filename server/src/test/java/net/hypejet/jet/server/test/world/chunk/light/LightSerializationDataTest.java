package net.hypejet.jet.server.test.world.chunk.light;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.hypejet.jet.server.test.world.chunk.ChunkTestUtil;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.light.LightSerializationData;
import net.hypejet.jet.server.world.chunk.light.LightType;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.DirectLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.util.array.NibbleArray;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.hypejet.jet.world.dimension.DimensionType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * A test of a {@linkplain LightSerializationData light serialization data}.
 *
 * @since 1.0
 * @see LightSerializationData
 */
public final class LightSerializationDataTest {
    @Test
    public void testDataCreation() {
        DimensionType dimensionType = ChunkTestUtil.DIMENSION_TYPE;

        AbstractLightStorage skyLightStorage = AbstractLightStorage.create(
                new NibbleArray.Builder(ChunkPaletteType.BLOCK_STATE.elementCount())
                        .set(0, (byte) 12)
                        .set(1, (byte) 3)
                        .set(23, (byte) 2)
                        .set(1045, (byte) 12)
                        .set(1046, (byte) 15)
                        .build()
        );

        AbstractLightStorage blockLightStorage = AbstractLightStorage.create(
                new NibbleArray.Builder(ChunkPaletteType.BLOCK_STATE.elementCount())
                        .set(3, (byte) 1)
                        .set(4, (byte) 15)
                        .set(323, (byte) 10)
                        .set(321, (byte) 11)
                        .set(1123, (byte) 5)
                        .build()
        );

        int sectionCount = LightSectionList.createSectionCount(dimensionType);
        List<JetLightSection> lightSections = new ArrayList<>(sectionCount);

        for (int index = 0; index < sectionCount; index++) {
            lightSections.add(index, new JetLightSection(
                    isSkyLightFilled(index) ? skyLightStorage : EmptyLightStorage.INSTANCE,
                    isBlockLightFilled(index) ? blockLightStorage : EmptyLightStorage.INSTANCE
            ));
        }

        LightSectionList sectionList = new LightSectionList(dimensionType, lightSections);
        LightSerializationData serializationData = LightSerializationData.create(sectionList);

        BitSet skyLightMask = serializationData.skyLightMask().bitSet();
        BitSet blockLightMask = serializationData.blockLightMask().bitSet();

        BitSet emptySkyLightMask = serializationData.emptySkyLightMask().bitSet();
        BitSet emptyBlockLightMask = serializationData.emptyBlockLightMask().bitSet();

        for (int index = 0; index < sectionCount; index++) {
            boolean skyLightMaskSet = skyLightMask.get(index);
            boolean blockLightMaskSet = blockLightMask.get(index);

            boolean emptySkyLightMaskSet = emptySkyLightMask.get(index);
            boolean emptyBlockLightMaskSet = emptyBlockLightMask.get(index);

            if (isSkyLightFilled(index)) {
                Assertions.assertTrue(skyLightMaskSet);
                Assertions.assertFalse(emptySkyLightMaskSet);
            } else {
                Assertions.assertFalse(skyLightMaskSet);
                Assertions.assertTrue(emptySkyLightMaskSet);
            }

            if (isBlockLightFilled(index)) {
                Assertions.assertTrue(blockLightMaskSet);
                Assertions.assertFalse(emptyBlockLightMaskSet);
            } else {
                Assertions.assertFalse(blockLightMaskSet);
                Assertions.assertTrue(emptyBlockLightMaskSet);
            }
        }

        List<NibbleArray> skyLightData = serializationData.skyLightData();
        List<NibbleArray> blockLightData = serializationData.blockLightData();

        int skyLightDataIndex = 0;
        int blockLightDataIndex = 0;

        for (JetLightSection section : lightSections) {
            AbstractLightStorage sectionSkyLightStorage = section.skyLightStorage();
            if (sectionSkyLightStorage instanceof DirectLightStorage) {
                Assertions.assertSame(sectionSkyLightStorage.data(), skyLightData.get(skyLightDataIndex));
                skyLightDataIndex++;
            }
            
            AbstractLightStorage sectionBlockLightStorage = section.blockLightStorage();
            if (sectionBlockLightStorage instanceof DirectLightStorage) {
                Assertions.assertSame(sectionBlockLightStorage.data(), blockLightData.get(blockLightDataIndex));
                blockLightDataIndex++;
            }
        }

        Assertions.assertEquals(skyLightData.size(), skyLightDataIndex);
        Assertions.assertEquals(blockLightData.size(), blockLightDataIndex);
    }

    @Test
    public void testUpdateDataCreation() {
        DimensionType dimensionType = ChunkTestUtil.DIMENSION_TYPE;

        AbstractLightStorage storage = AbstractLightStorage.create(
                new NibbleArray.Builder(ChunkPaletteType.BLOCK_STATE.elementCount())
                        .set(0, (byte) 12)
                        .set(1, (byte) 3)
                        .set(23, (byte) 2)
                        .set(1045, (byte) 12)
                        .set(1046, (byte) 15)
                        .build()
        );

        int sectionCount = LightSectionList.createSectionCount(dimensionType);
        List<JetLightSection> lightSections = new ArrayList<>(sectionCount);

        for (int index = 0; index < sectionCount; index++) {
            lightSections.add(index, new JetLightSection(
                    index % 3 != 0 ? storage : EmptyLightStorage.INSTANCE,
                    index % 2 == 0 ? storage : EmptyLightStorage.INSTANCE
            ));
        }

        Object2ByteMap<ChunkRelativeBlockPosition> skyLightUpdates = new Object2ByteOpenHashMap<>();
        Object2ByteMap<ChunkRelativeBlockPosition> blockLightUpdates = new Object2ByteOpenHashMap<>();

        skyLightUpdates.put(new ChunkRelativeBlockPosition((byte) 0, 0, (byte) 0), (byte) 8);
        skyLightUpdates.put(new ChunkRelativeBlockPosition((byte) 3, 18, (byte) 5), (byte) 13);

        skyLightUpdates.put(new ChunkRelativeBlockPosition((byte) 4, 0, (byte) 14), (byte) 11);
        skyLightUpdates.put(new ChunkRelativeBlockPosition((byte) 13, 35, (byte) 12), (byte) 5);

        LightSectionList sectionList = new LightSectionList(dimensionType, lightSections);
        LightSectionList updatedList = sectionList.withUpdates(skyLightUpdates, blockLightUpdates);

        LightSerializationData serializationData = LightSerializationData.create(
                skyLightUpdates.keySet(), blockLightUpdates.keySet(),
                updatedList, dimensionType
        );

        assertValidUpdateData(
                updatedList, LightType.SKY, skyLightUpdates, sectionCount, dimensionType,
                serializationData.skyLightMask().bitSet(), serializationData.emptySkyLightMask().bitSet(),
                serializationData.skyLightData()
        );

        assertValidUpdateData(
                updatedList, LightType.BLOCK, blockLightUpdates, sectionCount, dimensionType,
                serializationData.blockLightMask().bitSet(), serializationData.emptyBlockLightMask().bitSet(),
                serializationData.blockLightData()
        );
    }

    private static boolean isSkyLightFilled(int index) {
        return index % 3 == 0 && index != 0 && index != 9;
    }

    private static boolean isBlockLightFilled(int index) {
        return index % 3 == 0 && index != 12 && index != 6 && index != 9;
    }

    private static void assertValidUpdateData(@NonNull LightSectionList updatedList, @NonNull LightType lightType,
                                              @NonNull Object2ByteMap<ChunkRelativeBlockPosition> updates,
                                              int sectionCount, @NonNull DimensionType dimensionType,
                                              @NonNull BitSet lightMask, @NonNull BitSet emptyLightMask,
                                              @NonNull List<NibbleArray> data) {
        IntList affectedSections = new IntArrayList();
        for (ChunkRelativeBlockPosition position : updates.keySet()) {
            int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);
            affectedSections.add(LightSectionList.createSectionIndex(sectionY, dimensionType));
        }

        int dataIndex = 0;
        for (int index = 0; index < sectionCount; index++) {
            boolean lightMaskSet = lightMask.get(index);
            boolean emptyLightMaskSet = emptyLightMask.get(index);

            if (!affectedSections.contains(index)) {
                Assertions.assertFalse(lightMaskSet);
                Assertions.assertFalse(emptyLightMaskSet);
                continue;
            }

            JetLightSection section = updatedList.sections().get(index);
            AbstractLightStorage lightStorage = switch (lightType) {
                case SKY -> section.skyLightStorage();
                case BLOCK -> section.blockLightStorage();
            };

            if (lightStorage instanceof EmptyLightStorage) {
                Assertions.assertFalse(lightMaskSet);
                Assertions.assertTrue(emptyLightMaskSet);
                return;
            }

            Assertions.assertTrue(lightMaskSet);
            Assertions.assertFalse(emptyLightMaskSet);
            Assertions.assertEquals(lightStorage.data(), data.get(dataIndex));

            dataIndex++;
        }
    }
}