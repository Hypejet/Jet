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
        testCreation(SingleValuedChunkPalette.class, ChunkPaletteType::minimumDirectBits);
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
            for (int index = 0; index < elementListSize; index++) {
                updates.add(new ChunkPaletteUpdate<>(
                        new ChunkPaletteRelativePosition(
                                (byte) (index % axisLength),
                                (byte) (index / axisLength),
                                (byte) (index / (axisLength * axisLength)),
                                type
                        ),
                        elementList.get(index)
                ));
            }

            Assertions.assertInstanceOf(DirectChunkPalette.class, palette.withUpdates(updates));
        }
    }

    @Test
    public void testDirectToIndirectUpdate() {
        for (ChunkPaletteType type : ChunkPaletteType.values()) {
            int elementListSize = MathUtil.power(2, type.minimumDirectBits()) - 1;
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
            int maximumIndirectBits = type.maximumIndirectBits();
            int maximumIndirectElements = MathUtil.power(2, maximumIndirectBits) - 1;

            for (int index = 0; index < directElements.size(); index++) {
                int indexModulo = index % elementListSize;
                if (indexModulo < maximumIndirectBits) continue;
                updates.add(new ChunkPaletteUpdate<>(
                        AbstractChunkPalette.createPosition(index, type),
                        elementList.get(indexModulo % maximumIndirectElements)
                ));
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
            for (int index = 0; index < elementCount && index % elementCount == 0; index++) {
                updates.add(new ChunkPaletteUpdate<>(
                        AbstractChunkPalette.createPosition(index, type),
                        elementList.getLast()
                ));
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
            List<String> elementList = createElementList(bitCount);

            int elementCount = type.elementCount();
            List<String> elements = new ArrayList<>();

            for (int index = 0; index < elementCount; index++) {
                String element = elementList.get(index % bitCount);
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