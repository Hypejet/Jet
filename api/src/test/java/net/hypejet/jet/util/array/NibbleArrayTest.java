package net.hypejet.jet.util.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of {@linkplain NibbleArray a nibble array}.
 *
 * @since 1.0
 */
public final class NibbleArrayTest {
    @Test
    public void testBuilding() {
        int length = 5;

        byte[] expectedValues = new byte[length];
        NibbleArray.Builder builder = new NibbleArray.Builder(length);

        for (int index = 0; index < length; index++) {
            byte value = (byte) Math.clamp(index * index - index * 1.5, 0, 15);
            expectedValues[index] = value;
            builder.set(index, value);
        }

        NibbleArray array = builder.build();
        Assertions.assertEquals(length, array.length());

        for (int index = 0; index < length; index++)
            Assertions.assertEquals(expectedValues[index], array.get(index));
    }

    @Test
    public void testReplacing() {
        int firstReplacementIndex = 2;
        byte firstReplacementValue = 12;

        int secondReplacementIndex = 3;
        byte secondReplacementValue = 7;

        NibbleArray array = new NibbleArray.Builder(5)
                .set(0, (byte) 3)
                .set(1, (byte) 4)
                .set(2, (byte) 10)
                .set(3, (byte) 12)
                .set(4, (byte) 5)
                .build();

        NibbleArray.Builder builder = array.toBuilder();
        builder.set(firstReplacementIndex, firstReplacementValue);
        builder.set(secondReplacementIndex, secondReplacementValue);

        NibbleArray newArray = builder.build();
        Assertions.assertEquals(array.length(), newArray.length());

        for (long index = 0; index < array.length(); index++) {
            byte expected;

            if (index == firstReplacementIndex)
                expected = firstReplacementValue;
            else if (index == secondReplacementIndex)
                expected = secondReplacementValue;
            else expected = array.get(index);

            Assertions.assertEquals(expected, newArray.get(index));
        }
    }

    @Test
    public void testPackedArray() {
        NibbleArray array = new NibbleArray.Builder(5)
                .set(0, (byte) 3)
                .set(1, (byte) 4)
                .set(2, (byte) 5)
                .set(3, (byte) 10)
                .set(4, (byte) 1)
                .build();

        byte[] expectedArray = new byte[] { 52, 90, 16 };
        byte[] packedArray = array.packedArray();

        Assertions.assertArrayEquals(expectedArray, packedArray);
        Assertions.assertEquals(array, new NibbleArray(array.length(), packedArray));
    }
}