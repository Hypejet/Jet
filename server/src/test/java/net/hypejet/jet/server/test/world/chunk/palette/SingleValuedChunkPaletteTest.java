package net.hypejet.jet.server.test.world.chunk.palette;

import it.unimi.dsi.fastutil.objects.Object2ShortMaps;
import net.hypejet.jet.server.world.chunk.palette.SingleValuedChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * A test of a {@linkplain SingleValuedChunkPalette single-valued chunk palette}.
 *
 * @since 1.0
 * @see SingleValuedChunkPalette
 */
public final class SingleValuedChunkPaletteTest {
    @Test
    public void testContents() {
        ChunkPaletteType paletteType = ChunkPaletteType.BIOME;
        int elementCount = paletteType.elementCount();

        String element = "test-element";
        List<String> elementList = List.of(element);

        SingleValuedChunkPalette<String> palette = new SingleValuedChunkPalette<>(
                paletteType,
                element,
                new ListIndexSpecification<>(elementList)
        );

        Assertions.assertEquals(element, palette.element());
        Assertions.assertEquals(elementList.indexOf(element), palette.elementRegistryIndex());

        List<String> elements = new ArrayList<>(elementCount);
        for (int index = 0; index < elementCount; index++)
            elements.add(index, element);

        Assertions.assertEquals(elements, palette.elements());
        Assertions.assertEquals(Object2ShortMaps.singleton(element, (short) elementCount), palette.elementCountMap());
    }
}