package net.hypejet.jet.server.test.world.chunk.palette;

import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.DirectChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.IndirectChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.SingleValuedChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Represents a test of {@linkplain AbstractChunkPalette an abstract chunk palette}.
 *
 * @since 1.0
 * @see AbstractChunkPalette
 */
public final class AbstractChunkPaletteTest {
    @Test
    public void testSingleValuedCreation() {
        testCreation(SingleValuedChunkPalette.class, type -> 0);
    }

    @Test
    public void testMaximalIndirectCreation() {
        testCreation(IndirectChunkPalette.class, ChunkPaletteType::maximumIndirectBits);
    }

    @Test
    public void testMinimalIndirectCreation() {
        testCreation(IndirectChunkPalette.class, ChunkPaletteType::minimumIndirectBits);
    }

    @Test
    public void testDirectCreation() {
        testCreation(DirectChunkPalette.class, ChunkPaletteType::minimumDirectBits);
    }

    @Test
    public void testSingleValuedToIndirectUpdate() {
        for (ChunkPaletteType type : ChunkPaletteType.values()) {
            List<String> elementList = createElementList(2);
            ElementOrder<String> elementOrder = new ElementOrder<>(elementList);

            SingleValuedChunkPalette<String> singleValuedPalette = new SingleValuedChunkPalette<>(
                    type, elementList.getFirst(),
                    elementOrder
            );

            Assertions.assertInstanceOf(
                    IndirectChunkPalette.class,
                    singleValuedPalette.withUpdates(List.of(new ChunkPaletteUpdate<>(
                            new ChunkPaletteRelativePosition((byte) 0, (byte) 0, (byte) 0, type),
                            elementList.getLast()
                    )))
            );
        }
    }

    @Test
    public void testIndirectToDirectUpdate() {
        for (ChunkPaletteType type : ChunkPaletteType.values()) {
            int elementCount = type.elementCount();
            int axisLength = type.axisLength();

            int elementListSize = MathUtil.power(2, type.minimumDirectBits()) - 1;
            if (elementListSize <= 1) elementListSize++;

            List<String> elementList = createElementList(elementListSize);
            ElementOrder<String> elementOrder = new ElementOrder<>(elementList);

            List<String> indirectElements = new ArrayList<>(elementCount);
            for (int index = 0; index < elementCount; index++) {
                String element = elementList.get(index % 2);
                indirectElements.add(index, element);
            }

            AbstractChunkPalette<String> palette = AbstractChunkPalette.create(type, elementOrder, indirectElements);
            Assertions.assertInstanceOf(IndirectChunkPalette.class, palette);

            List<ChunkPaletteUpdate<String>> updates = new ArrayList<>();
            int index = 0;

            for (byte x = 0; x < axisLength; x++) {
                for (byte y = 0; y < axisLength; y++) {
                    for (byte z = 0; z < axisLength; z++) {
                        updates.add(new ChunkPaletteUpdate<>(
                                new ChunkPaletteRelativePosition(x, y, z, type),
                                elementList.get(index)
                        ));

                        index++;
                        if (index >= elementListSize) break;
                    }
                }
            }

            Assertions.assertInstanceOf(DirectChunkPalette.class, palette.withUpdates(updates));
        }
    }

    @Test
    public void testDirectToIndirectUpdate() {
        for (ChunkPaletteType type : ChunkPaletteType.values()) {
            int elementListSize = MathUtil.power(2, type.minimumDirectBits()) - 1;
            if (elementListSize <= 1) elementListSize++;

            int elementCount = type.elementCount();

            List<String> elementList = createElementList(elementListSize);
            ElementOrder<String> elementOrder = new ElementOrder<>(elementList);

            List<String> directElements = new ArrayList<>(elementCount);
            for (int index = 0; index < elementCount; index++) {
                String element = elementList.get(index % elementListSize);
                directElements.add(index, element);
            }

            AbstractChunkPalette<String> palette = AbstractChunkPalette.create(type, elementOrder, directElements);
            Assertions.assertInstanceOf(DirectChunkPalette.class, palette);

            List<ChunkPaletteUpdate<String>> updates = new ArrayList<>();

            int maximumIndirectElements = MathUtil.power(2, type.maximumIndirectBits()) - 1;
            if (maximumIndirectElements <= 1) maximumIndirectElements++;

            byte axisLength = type.axisLength();
            for (byte x = 0; x < axisLength; x++) {
                for (byte y = 0; y < axisLength; y++) {
                    for (byte z = 0; z < axisLength; z++) {
                        ChunkPaletteRelativePosition position = new ChunkPaletteRelativePosition(x, y, z, type);
                        int index = AbstractChunkPalette.calculateElementIndex(position);

                        int indexModulo = index % elementListSize;
                        if (indexModulo < maximumIndirectElements) continue;

                        updates.add(new ChunkPaletteUpdate<>(
                                position,
                                elementList.get(indexModulo % maximumIndirectElements)
                        ));
                    }
                }
            }

            Assertions.assertInstanceOf(IndirectChunkPalette.class, palette.withUpdates(updates));
        }
    }

    @Test
    public void testIndirectToSingleValuedUpdate() {
        for (ChunkPaletteType type : ChunkPaletteType.values()) {
            List<String> elementList = createElementList(2);
            ElementOrder<String> elementOrder = new ElementOrder<>(elementList);

            int elementCount = type.elementCount();
            List<String> elements = new ArrayList<>(elementCount);

            for (int index = 0; index < elementCount; index++)
                elements.add(index, elementList.get(index % 2));

            AbstractChunkPalette<String> palette = AbstractChunkPalette.create(type, elementOrder, elements);
            Assertions.assertInstanceOf(IndirectChunkPalette.class, palette);

            List<ChunkPaletteUpdate<String>> updates = new ArrayList<>();
            byte axisLength = type.axisLength();

            for (byte x = 0; x < axisLength; x++) {
                for (byte y = 0; y < axisLength; y++) {
                    for (byte z = 0; z < axisLength; z++) {
                        ChunkPaletteRelativePosition position = new ChunkPaletteRelativePosition(x, y, z, type);
                        int index = AbstractChunkPalette.calculateElementIndex(position);
                        if (index % 2 == 0) continue;
                        updates.add(new ChunkPaletteUpdate<>(position, elementList.getFirst()));
                    }
                }
            }

            Assertions.assertInstanceOf(SingleValuedChunkPalette.class, palette.withUpdates(updates));
        }
    }

    private static void testCreation(
            @NonNull Class<?> expectedType,
            @NonNull ToIntFunction<ChunkPaletteType> bitCountFunction
    ) {
        for (ChunkPaletteType type : ChunkPaletteType.values()) {
            int bitCount = bitCountFunction.applyAsInt(type);

            int elementListSize = bitCount == 0 ? 0 : MathUtil.power(2, bitCount) - 1;
            if (elementListSize <= 1) elementListSize++;

            List<String> elementList = createElementList(elementListSize);
            int elementCount = type.elementCount();
            List<String> elements = new ArrayList<>();

            for (int index = 0; index < elementCount; index++) {
                String element = elementList.get(index % elementListSize);
                elements.add(index, element);
            }

            Assertions.assertInstanceOf(
                    expectedType,
                    AbstractChunkPalette.create(type, new ElementOrder<>(elementList), elements)
            );
        }
    }

    private static @NonNull List<String> createElementList(int elementCount) {
        List<String> elementList = new ArrayList<>(elementCount);
        for (int index = 0; index < elementCount; index++) {
            String element = String.valueOf(index * 2 + 1);
            elementList.add(index, element);
        }
        return elementList;
    }
}