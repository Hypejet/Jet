package net.hypejet.jet.server.util.storage;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.math.MathUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a storage of integer values, which are stored in a long array. Elements of the array can store multiple
 * values at once.
 *
 * @since 1.0
 */
public final class BitStorage {

    private final byte bitsPerElement;
    private final byte elementsPerDataValue;

    private final int size;
    private final long maxValue;

    private final long[] data;

    /**
     * Constructs the {@linkplain BitStorage bit storage}.
     *
     * @param bitsPerElement a number defining how many bits should one element use
     * @param elements elements that the bit storage should have
     * @since 1.0
     */
    public BitStorage(byte bitsPerElement, int @NonNull [] elements) {
        NullabilityUtil.requireNonNull(elements, "elements");

        if (bitsPerElement <= 0)
            throw new IllegalArgumentException("The bit-per-element value cannot be negative or zero");

        if (bitsPerElement > Long.SIZE) {
            throw new IllegalArgumentException(String.format(
                    "The bits-per-element value specified (%d) is invalid, the maximum allowed value is %d",
                    bitsPerElement, Long.SIZE
            ));
        }

        this.bitsPerElement = bitsPerElement;
        this.elementsPerDataValue = (byte) (Math.floorDiv(Long.SIZE, bitsPerElement));

        this.size = elements.length;
        this.maxValue = createBinaryNumber(this.bitsPerElement);

        this.data = new long[Math.ceilDiv(elements.length, elementsPerDataValue)];
        for (int index = 0; index < elements.length; index++)
            setValue(this.data, index, elements[index]);
    }

    /**
     * Constructs the {@linkplain BitStorage bit storage}.
     *
     * @param bitsPerElement a number defining how many bits one element uses
     * @param elementsPerDataValue a number defining how many elements one data element can store
     * @param size a number of elements that the bit storage should store
     * @param maxValue a maximum value that one element can have
     * @param data a raw data that the bit storage should have
     * @since 1.0
     */
    private BitStorage(byte bitsPerElement, byte elementsPerDataValue, int size, long maxValue,
                       long @NonNull [] data) {
        this.bitsPerElement = bitsPerElement;
        this.elementsPerDataValue = elementsPerDataValue;
        this.size = size;
        this.maxValue = maxValue;
        this.data = NullabilityUtil.requireNonNull(data, "data").clone();
    }

    /**
     * Gets an element stored in this bit storage.
     *
     * @param elementIndex an index of the element
     * @return the element
     * @since 1.0
     */
    @Contract(pure = true)
    public int getElement(int elementIndex) {
        if (elementIndex >= this.size)
            throw new IndexOutOfBoundsException(elementIndex);

        long dataElement = this.data[this.createDataIndex(elementIndex)];
        int bitShift = this.createDataValuePositionBitShift(elementIndex);
        return (int) ((this.maxValue << bitShift & dataElement) >>> bitShift);
    }

    /**
     * Gets a long array that stores elements of this {@linkplain BitStorage bit storage}. Elements of the long array
     * can store multiple values at once.
     *
     * @return the long array
     * @since 1.0
     */
    public long @NonNull [] data() {
        return this.data.clone();
    }

    /**
     * Creates a new {@linkplain BitStorage bit storage}, which is a copy of this bit storage with updates specified
     * applied.
     *
     * @param updates the updates
     * @return the bit storage
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull BitStorage withUpdates(@NonNull BitStorageUpdate @NonNull ... updates) {
        long[] data = this.data.clone();
        for (BitStorageUpdate update : updates)
            setValue(data, update.elementIndex(), update.newElement());
        return new BitStorage(this.bitsPerElement, this.elementsPerDataValue, this.size, this.maxValue, data);
    }

    /**
     * Creates an array, which contains all elements of this bit storage directly in their original order.
     *
     * @return the array
     * @since 1.0
     */
    public int @NonNull [] unpack() {
        int[] unpackedData = new int[this.size];
        for (int index = 0; index < unpackedData.length; index++)
            unpackedData[index] = this.getElement(index);
        return unpackedData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BitStorage storage)) return false;
        return this.bitsPerElement == storage.bitsPerElement
                && this.size == storage.size
                && Objects.deepEquals(this.data, storage.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bitsPerElement, this.size, Arrays.hashCode(this.data));
    }

    @Override
    public String toString() {
        return "BitStorage{" +
                "bitsPerElement=" + this.bitsPerElement +
                ", size=" + this.size +
                ", data=" + Arrays.toString(this.data) +
                '}';
    }

    private void setValue(long @NonNull [] data, int elementIndex, int element) {
        int elementBits = MathUtil.bitCount(element);
        if (elementBits > this.bitsPerElement) {
            throw new IllegalArgumentException(String.format(
                    "Count of bits of element with index of %d is higher than the bits-per-element value (%d>%d)",
                    elementIndex, elementBits, this.bitsPerElement
            ));
        }

        int dataIndex = createDataIndex(elementIndex);
        int bitShift = this.createDataValuePositionBitShift(elementIndex);

        long dataValue = data[dataIndex];
        dataValue &= ~(this.maxValue << bitShift); // Remove the previous value
        dataValue |= (long) element << bitShift;

        data[dataIndex] = dataValue;
    }

    private int createDataIndex(int elementIndex) {
        return Math.floorDiv(elementIndex, this.elementsPerDataValue);
    }

    private int createDataValuePositionBitShift(int elementIndex) {
        return elementIndex % this.elementsPerDataValue * this.bitsPerElement;
    }

    private static long createBinaryNumber(int setBitCount) {
        long number = 0;
        for (int exponent = 0; exponent < setBitCount; exponent++)
            number |= 1L << exponent;
        return number;
    }
}