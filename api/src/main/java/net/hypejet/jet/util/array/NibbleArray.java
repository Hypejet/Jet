package net.hypejet.jet.util.array;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an array, which stores unsigned 4-bit values.
 *
 * @since 1.0
 */
public final class NibbleArray {

    private static final byte VALUES_PER_BYTE = 2;
    private static final byte VALUE_BITS = Byte.SIZE / VALUES_PER_BYTE;

    private static final long MAXIMUM_ACTUAL_LENGTH = (long) Integer.MAX_VALUE * VALUES_PER_BYTE;
    private static final long MAXIMUM_INDEX = MAXIMUM_ACTUAL_LENGTH - 1;

    private static final byte MAX_VALUE = (byte) (-1 >>> Integer.SIZE - Byte.SIZE >>> Byte.SIZE - VALUE_BITS);

    private final @IntRange(from = 0, to = MAXIMUM_ACTUAL_LENGTH) long actualLength;
    private final byte[] packedArray;

    /**
     * Constructs the {@linkplain NibbleArray nibble array}.
     *
     * @param length a number of elements that the nibble array should have
     * @param packedArray an array, which stores values that the nibble array should have in a way where one byte
     *                    represents two values, where the least significant bits are for bigger index values
     * @since 1.0
     */
    public NibbleArray(@IntRange(from = 0, to = MAXIMUM_ACTUAL_LENGTH) long length, byte @NonNull [] packedArray) {
        this.actualLength = length;
        this.packedArray = createPackedArray(length, packedArray);
    }

    /**
     * Gets number of elements that this {@linkplain NibbleArray nibble array} stores.
     *
     * @return the number
     * @since 1.0
     */
    public @IntRange(from = 0, to = MAXIMUM_ACTUAL_LENGTH) long length() {
        return this.actualLength;
    }

    /**
     * Gets a 4-bit unsigned value from this array at an index specified.
     *
     * @param index the index
     * @return the value, as a byte
     * @since 1.0
     */
    public @IntRange(from = 0, to = MAX_VALUE) byte get(@IntRange(from = 0, to = MAXIMUM_INDEX) long index) {
        byte actualArrayValue = this.packedArray[createdPackedArrayIndex(index)];
        byte bitShiftCount = createBitShiftCount(index);
        return (byte) (actualArrayValue >>> bitShiftCount & MAX_VALUE);
    }

    /**
     * Creates a copy of this {@linkplain NibbleArray nibble array}, with a replaced value at an index specified
     * to a value specified.
     *
     * @param index the index
     * @param value the value that value in the array should be replaced to
     * @return the copy, {@code this} if the value is the same as it was before the change
     * @since 1.0
     */
    public @NonNull NibbleArray withValue(@IntRange(from = 0, to = MAXIMUM_INDEX) long index,
                                          @IntRange(from = 0, to = MAX_VALUE) byte value) {
        if (this.get(index) == value)
            return this;
        return this.toBuilder().set(index, value).build();
    }

    /**
     * Creates {@linkplain Builder a nibble array builder} and fills it with all values from
     * this {@linkplain NibbleArray nibble array}.
     *
     * @return the nibble array builder
     * @since 1.0
     */
    public @NonNull Builder toBuilder() {
        return new Builder(this);
    }

    /**
     * Gets a copy of a packed byte array, which is an actual storage of values
     * of this {@linkplain NibbleArray nibble array}.
     *
     * <p>The array stores values in a way where one byte represents two values, where the least significant bits
     * are for bigger index values.</p>
     *
     * @return the copy
     * @since 1.0
     */
    public byte @NonNull [] packedArray() {
        return this.packedArray.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NibbleArray that)) return false;
        return this.actualLength == that.actualLength && Objects.deepEquals(this.packedArray, that.packedArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.actualLength, Arrays.hashCode(this.packedArray));
    }

    @Override
    public String toString() {
        return "NibbleArray{" +
                "actualLength=" + this.actualLength +
                ", packedArray=" + Arrays.toString(this.packedArray) +
                '}';
    }

    private static byte @NonNull [] createPackedArray(long length, byte @NotNull [] packedArray) {
        int expectedArrayLength = createPackedArrayLength(length);
        int actualArrayLength = packedArray.length;

        if (actualArrayLength < expectedArrayLength) {
            throw new IllegalArgumentException(String.format(
                    "Length of the packed array (%d) is less than minimum needed" +
                            " (%d) to create a nibble array with length of (%d)",
                    actualArrayLength, expectedArrayLength, length
            ));
        }

        byte[] packedArrayCreated = new byte[expectedArrayLength];
        System.arraycopy(packedArray, 0, packedArrayCreated, 0, packedArrayCreated.length);
        return packedArrayCreated;
    }

    private static int createPackedArrayLength(long length) {
        if (length < 0)
            throw new IllegalArgumentException("The length must not be negative");

        if (length > MAXIMUM_ACTUAL_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "The length specified is higher than maximum allowed (%d>%d)",
                    length, MAXIMUM_ACTUAL_LENGTH
            ));
        }

        return (int) Math.ceilDiv(length, VALUES_PER_BYTE);
    }

    private static int createdPackedArrayIndex(@IntRange(from = 0, to = MAXIMUM_INDEX) long index) {
        if (index < 0)
            throw new IllegalArgumentException("The index must not be negative");

        if (index > MAXIMUM_INDEX) {
            throw new IllegalArgumentException(String.format(
                    "The index specified is higher than maximum allowed (%d>%d)",
                    index, MAXIMUM_INDEX
            ));
        }

        return (int) (index / VALUES_PER_BYTE);
    }

    private static byte createBitShiftCount(long index) {
        byte nibbleShiftCount = (byte) (VALUES_PER_BYTE - (index % VALUES_PER_BYTE) - 1);
        return (byte) (VALUE_BITS * nibbleShiftCount);
    }

    /**
     * Represents a builder of {@linkplain NibbleArray a nibble array}.
     *
     * @since 1.0
     * @see NibbleArray
     */
    public static final class Builder {

        private final @IntRange(from = 0, to = MAXIMUM_ACTUAL_LENGTH) long actualLength;
        private final byte[] packedArray;

        /**
         * Constructs the {@linkplain Builder nibble array builder}.
         *
         * @param length a length that the nibble array should have
         * @since 1.0
         */
        public Builder(@IntRange(from = 0, to = MAXIMUM_ACTUAL_LENGTH) long length) {
            this.actualLength = length;
            this.packedArray = new byte[createPackedArrayLength(length)];
        }

        /**
         * Constructs the {@linkplain Builder nibble array builder} by filling it with all values
         * from {@linkplain NibbleArray a nibble array} specified.
         *
         * @param array the nibble array
         * @since 1.0
         */
        private Builder(@NonNull NibbleArray array) {
            this.actualLength = array.actualLength;
            this.packedArray = array.packedArray.clone();
        }

        /**
         * Sets a value specified to be at an index specified of the nibble array,
         *
         * @param index the index
         * @param value the value
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder set(@IntRange(from = 0, to = MAXIMUM_INDEX) long index,
                                    @IntRange(from = 0, to = MAX_VALUE) byte value) {
            if (value < 0)
                throw new IllegalArgumentException("The value must not be negative");

            if (value > MAX_VALUE) {
                throw new IllegalArgumentException(String.format(
                        "The value specified is higher than maximum allowed (%d>%d)",
                        value, MAX_VALUE
                ));
            }

            int packedArrayIndex = createdPackedArrayIndex(index);
            byte packedArrayValue = this.packedArray[packedArrayIndex];

            byte bitShiftCount = createBitShiftCount(index);
            packedArrayValue &= (byte) ~(MAX_VALUE << bitShiftCount);
            packedArrayValue |= (byte) (value << bitShiftCount);

            this.packedArray[packedArrayIndex] = packedArrayValue;
            return this;
        }

        /**
         * Builds {@linkplain NibbleArray a nibble array} with values set in this builder.
         *
         * @return the nibble array
         * @since 1.0
         */
        public @NonNull NibbleArray build() {
            return new NibbleArray(this.actualLength, this.packedArray);
        }
    }
}