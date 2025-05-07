package net.hypejet.jet.server.test.world.chunk.palette;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.DirectChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a test of {@linkplain DirectChunkPalette a direct chunk palette}.
 *
 * @since 1.0
 * @see DirectChunkPalette
 */
public final class DirectChunkPaletteTest {
    @Test
    public void testContents() {
        ChunkPaletteType paletteType = ChunkPaletteType.BIOME;

        int elementListSize = MathUtil.power(2, paletteType.minimumDirectBits()) - 1;
        int elementCount = paletteType.elementCount();

        List<String> elementList = new ArrayList<>();
        for (int index = 0; index < elementListSize; index++)
            elementList.add(index, String.valueOf(index));

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
                new ElementOrder<>(elementList),
                elements
        );

        Assertions.assertInstanceOf(DirectChunkPalette.class, palette);
        Assertions.assertEquals(elements, palette.elements());
        Assertions.assertEquals(elementCountMap, palette.elementCountMap());
    }
}