package net.hypejet.jet.server.test.world.chunk.light.storage;

import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.DirectLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.util.array.NibbleArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Represents a test of {@linkplain AbstractLightStorage an abstract light storage}.
 *
 * @since 1.0
 * @see AbstractLightStorage
 */
public final class AbstractLightStorageTest {
    @Test
    public void testEmptyCreation() {
        NibbleArray array = new NibbleArray.Builder(ChunkPaletteType.BLOCK_STATE.elementCount()).build();
        AbstractLightStorage storage = AbstractLightStorage.create(array);
        Assertions.assertInstanceOf(EmptyLightStorage.class, storage);
        Assertions.assertSame(EmptyLightStorage.INSTANCE, storage);
    }

    @Test
    public void testDirectCreation() {
        NibbleArray array = new NibbleArray.Builder(ChunkPaletteType.BLOCK_STATE.elementCount())
                .set(0, (byte) 12)
                .set(1, (byte) 5)
                .set(103, (byte) 3)
                .set(142, (byte) 4)
                .set(324, (byte) 3)
                .set(325, (byte) 15)
                .build();

        AbstractLightStorage storage = AbstractLightStorage.create(array);
        Assertions.assertInstanceOf(DirectLightStorage.class, storage);
        Assertions.assertEquals(array, storage.data());
    }

    @Test
    public void testConversion() {
        NibbleArray emptyStorageArray = new NibbleArray.Builder(ChunkPaletteType.BLOCK_STATE.elementCount()).build();

        AbstractLightStorage emptyStorage = AbstractLightStorage.create(emptyStorageArray);
        Assertions.assertInstanceOf(EmptyLightStorage.class, emptyStorage);

        LightStorageUpdate emptyToDirectUpdate = new LightStorageUpdate(
                new ChunkPaletteRelativePosition(
                        (byte) 5, (byte) 5, (byte) 5,
                        ChunkPaletteType.BLOCK_STATE
                ),
                (byte) 15
        );

        AbstractLightStorage directStorage = emptyStorage.withUpdates(Set.of(emptyToDirectUpdate));
        Assertions.assertInstanceOf(DirectLightStorage.class, directStorage);

        NibbleArray directStorageArray = directStorage.data();
        int indexInSearch = AbstractChunkPalette.calculateElementIndex(emptyToDirectUpdate.position());

        for (long index = 0; index < directStorageArray.length(); index++) {
            byte previousValue = emptyStorageArray.get(index);
            byte newValue = directStorageArray.get(index);

            if (indexInSearch == index) {
                Assertions.assertNotSame(previousValue, newValue);
                Assertions.assertSame((byte) 0, previousValue);
                Assertions.assertSame(emptyToDirectUpdate.value(), newValue);
                continue;
            }

            Assertions.assertSame(previousValue, newValue);
            Assertions.assertSame((byte) 0, newValue);
        }

        LightStorageUpdate directToEmptyUpdate = new LightStorageUpdate(emptyToDirectUpdate.position(), (byte) 0);
        AbstractLightStorage secondEmptyStorage = directStorage.withUpdates(Set.of(directToEmptyUpdate));

        NibbleArray secondEmptyStorageArray = secondEmptyStorage.data();
        for (long index = 0; index < secondEmptyStorageArray.length(); index++) {
            byte newValue = emptyStorageArray.get(index);
            Assertions.assertEquals((byte) 0, newValue);
        }
    }
}