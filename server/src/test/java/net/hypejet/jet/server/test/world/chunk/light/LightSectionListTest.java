package net.hypejet.jet.server.test.world.chunk.light;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMaps;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.server.test.world.chunk.ChunkTestDimensionHolder;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.light.LightType;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a test of {@linkplain LightSectionList a light-section list}.
 *
 * @since 1.0
 * @see LightSectionList
 */
public final class LightSectionListTest {
    @Test
    public void testUpdatingAndSectionIndices() {
        DimensionType dimensionType = ChunkTestDimensionHolder.DIMENSION_TYPE;

        JetLightSection emptySection = new JetLightSection(EmptyLightStorage.INSTANCE, EmptyLightStorage.INSTANCE);
        int sectionCount = LightSectionList.createSectionCount(dimensionType);

        List<JetLightSection> lightSections = new ArrayList<>(sectionCount);
        for (int index = 0; index < sectionCount; index++)
            lightSections.add(index, emptySection);

        LightSectionList sectionList = new LightSectionList(dimensionType, lightSections);

        Assertions.assertSame(sectionList, sectionList.withUpdates(
                Object2ByteMaps.singleton(new ChunkRelativeBlockPosition((byte) 12, 32, (byte) 3), (byte) 0),
                Object2ByteMaps.singleton(new ChunkRelativeBlockPosition((byte) 12, 32, (byte) 3), (byte) 0)
        ));

        Object2ByteMap<ChunkRelativeBlockPosition> skyLightUpdates = new Object2ByteOpenHashMap<>();
        Object2ByteMap<ChunkRelativeBlockPosition> blockLightUpdates = new Object2ByteOpenHashMap<>();

        skyLightUpdates.put(new ChunkRelativeBlockPosition((byte) 13, 23, (byte) 14), (byte) 1);
        skyLightUpdates.put(new ChunkRelativeBlockPosition((byte) 15, 43, (byte) 2), (byte) 15);

        blockLightUpdates.put(new ChunkRelativeBlockPosition((byte) 3, 0, (byte) 12), (byte) 1);
        blockLightUpdates.put(new ChunkRelativeBlockPosition((byte) 7, 103, (byte) 6), (byte) 15);

        LightSectionList updatedList = sectionList.withUpdates(skyLightUpdates, blockLightUpdates);
        Assertions.assertNotEquals(sectionList, updatedList);

        assertUpdated(sectionList, updatedList, skyLightUpdates, LightType.SKY);
        assertUpdated(sectionList, updatedList, blockLightUpdates, LightType.BLOCK);

        IntList unaffectedSections = new IntArrayList(sectionCount);
        for (int index = 0; index < sectionCount; index++)
            unaffectedSections.add(index);

        removeAffected(unaffectedSections, dimensionType, skyLightUpdates);
        removeAffected(unaffectedSections, dimensionType, blockLightUpdates);

        List<JetLightSection> sections = sectionList.sections();
        List<JetLightSection> updatedSections = updatedList.sections();

        for (int index = 0; index < unaffectedSections.size(); index++)
            Assertions.assertEquals(sections.get(index), updatedSections.get(index));
    }

    private static void assertUpdated(@NonNull LightSectionList previousList, @NonNull LightSectionList newList,
                                      @NonNull Object2ByteMap<ChunkRelativeBlockPosition> updates,
                                      @NonNull LightType lightType) {
        for (Object2ByteMap.Entry<ChunkRelativeBlockPosition> entry : updates.object2ByteEntrySet()) {
            ChunkRelativeBlockPosition position = entry.getKey();
            byte value = entry.getByteValue();

            JetLightSection previousSection = previousList.sectionFor(position);
            JetLightSection newSection = newList.sectionFor(position);

            Assertions.assertNotEquals(previousSection, newSection);

            ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);
            AbstractLightStorage storage = switch (lightType) {
                case SKY -> newSection.skyLightStorage();
                case BLOCK -> newSection.blockLightStorage();
            };

            Assertions.assertEquals(value, storage.getValue(palettePosition));
        }
    }

    private static void removeAffected(@NonNull IntList unaffectedSections, @NonNull DimensionType dimensionType,
                                       @NonNull Object2ByteMap<ChunkRelativeBlockPosition> updates) {
        for (Object2ByteMap.Entry<ChunkRelativeBlockPosition> entry : updates.object2ByteEntrySet()) {
            ChunkRelativeBlockPosition position = entry.getKey();
            int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);
            unaffectedSections.removeInt(LightSectionList.createSectionIndex(sectionY, dimensionType));
        }
    }
}