package net.hypejet.jet.server.util.bytes;

/**
 * Represents a utility for byte management.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ByteUtil {

    private ByteUtil() {}

    /**
     * Gets whether a bitmask is enabled on a byte using a bitwise {@code AND} operator.
     *
     * @param value the byte
     * @param bitmask the bitmask
     * @return {@code true} if the bitmask is enabled on the byte, {@code false} otherwise
     * @since 1.0
     */
    public static boolean isBitMaskEnabled(byte value, byte bitmask) {
        return (value & bitmask) != 0;
    }
}