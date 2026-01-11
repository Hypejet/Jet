package net.hypejet.jet.server.util.number;

/**
 * Utilities for {@code byte} primitive type.
 *
 * @since 1.0
 */
public final class ByteUtil {

    private ByteUtil() {}

    /**
     * Gets whether the bit at the specified bit index of the specified {@code byte} value is set to {@code 1}.
     *
     * @param value the byte value
     * @param bitIndex the bit index, where {@code 0} is the least significant bit
     * @return {@code true} if the bit is set to {@code 1}, {@code false} otherwise
     * @since 1.0
     */
    public static boolean bitSet(byte value, int bitIndex) {
        return (value & (1 << bitIndex)) != 0;
    }

    /**
     * Creates a {@code byte} value that is a copy of the specified {@code byte}
     * value with bit at the specified index set to the specified value.
     *
     * @param value the byte value that should be copied
     * @param bitIndex the index of the bit to set to the specified value, where {@code 0} is the least significant bit
     * @param setBit {@code true} if the bit at the specified index should be set to {@code 1},
     *               {@code false} if it should be set to {@code 0}
     * @return the copied byte value with the bit change acknowledged
     * @since 1.0
     */
    public static byte withBit(byte value, int bitIndex, boolean setBit) {
        byte bitOperationArgument = (byte) (1 << bitIndex);
        if (setBit) {
            return value |= bitOperationArgument;
        } else {
            return value &= (byte) ~bitOperationArgument;
        }
    }
}