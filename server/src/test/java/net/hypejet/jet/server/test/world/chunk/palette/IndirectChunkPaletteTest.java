package net.hypejet.jet.server.test.world.chunk.palette;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.IndirectChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * A test of {@linkplain IndirectChunkPalette indirect chunk palettea}.
 *
 * @since 1.0
 * @see IndirectChunkPalette
 */
public final class IndirectChunkPaletteTest {
    @Test
    public void testContents() {
        for (ChunkPaletteType paletteType : ChunkPaletteType.values()) {
            int elementListSize = MathUtil.power(2, paletteType.minimumIndirectBits()) - 1;
            if (elementListSize <= 1) elementListSize++;

            List<String> elementList = new ArrayList<>();
            for (int index = 0; index < elementListSize; index++)
                elementList.add(index, String.valueOf(index));

            int elementCount = paletteType.elementCount();
            List<String> elements = new ArrayList<>(elementCount);
            Object2ShortMap<String> elementCountMap = new Object2ShortOpenHashMap<>();

            for (int index = 0; index < elementCount; index++) {
                String element = elementList.get(index % elementListSize);
                elements.add(index, element);
                short count = elementCountMap.containsKey(element) ? (short) (elementCountMap.getShort(element) + 1) : 1;
                elementCountMap.put(element, count);
            }

            AbstractChunkPalette<String> palette = AbstractChunkPalette.create(
                    paletteType,
                    new ListIndexSpecification<>(elementList),
                    elements
            );

            Assertions.assertInstanceOf(IndirectChunkPalette.class, palette);
            Assertions.assertEquals(elements, palette.elements());
            Assertions.assertEquals(elementCountMap, palette.elementCountMap());

            IndirectChunkPalette<String> castPalette = (IndirectChunkPalette<String>) palette;
            IntList registryIndices = new IntArrayList(castPalette.registryIndices());

            for (String element : elementCountMap.keySet()) {
                int elementIdentifier = elementList.indexOf(element);
                Assertions.assertTrue(registryIndices.contains(elementIdentifier));
                registryIndices.rem(elementIdentifier);
            }

            Assertions.assertTrue(registryIndices.isEmpty());
        }
    }
}