package net.hypejet.jet.server.test.util.storage;

import net.hypejet.jet.server.util.storage.BitStorage;
import net.hypejet.jet.server.util.storage.BitStorageUpdate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of {@linkplain BitStorage a bit storage}.
 *
 * @since 1.0
 * @see BitStorage
 */
public final class BitStorageTest {
    @Test
    public void testCreation() {
        int[] array = new int[] { 43, 12, 41, 330021, 12314, 5431, 43234, 14325, 43141, 34134, 2097151, 0, 32134, 4};
        BitStorage storage = new BitStorage((byte) 21, array);
        Assertions.assertEquals(storage.size(), array.length);

        for (int index = array.length - 1; index >= 0; index--) {
            int expected = array[index];
            int actual = storage.getElement(index);
            Assertions.assertEquals(expected, actual);
        }

        Assertions.assertArrayEquals(
                new long[] {
                        180_319_932_121_131L, 23_885_816_426_465_573L,
                        189_736_154_577_283_298L, 4_398_044_448_086L,
                        8_420_742L
                },
                storage.data()
        );
    }

    @Test
    public void testUpdate() {
        BitStorage storage = new BitStorage((byte) 27, new int[] { 126, 12, 43, 330021, 127, 43141, 54134, 2097151 });
        Assertions.assertSame(storage, storage.withUpdates());

        Assertions.assertSame(storage, storage.withUpdates(
                new BitStorageUpdate(1, 12),
                new BitStorageUpdate(0, 126),
                new BitStorageUpdate(2, 43),
                new BitStorageUpdate(3, 330021)
        ));

        BitStorageUpdate update = new BitStorageUpdate(3, 424);
        BitStorage updatedStorage = storage.withUpdates(update);

        Assertions.assertNotEquals(storage, updatedStorage);
        Assertions.assertEquals(storage.size(), updatedStorage.size());
        Assertions.assertNotEquals(updatedStorage.data(), updatedStorage.data());

        for (int index = 0; index < storage.size(); index++) {
            int storageElement = storage.getElement(index);
            int updatedStorageElement = updatedStorage.getElement(index);

            if (update.elementIndex() == index) {
                Assertions.assertNotEquals(storage.getElement(index), updatedStorageElement);
                Assertions.assertEquals(update.newElement(), updatedStorageElement);
                continue;
            }

            Assertions.assertEquals(storageElement, updatedStorageElement);
        }
    }
}