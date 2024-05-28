package net.hypejet.jet.server.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.charset.StandardCharsets;

/**
 * An utility used for serializing data allowed by Minecraft protocol, but unsupported by default
 * via {@link ByteBuf byte buf}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class NetworkUtil {

    private static final byte SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    private static final short MAX_STRING_SIZE = 32767;

    private NetworkUtil() {}

    /**
     * Reads a variable-length integer from a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the variable-length integer
     * @throws IllegalArgumentException if the integer is bigger than maximum allowed
     * @since 1.0
     */
    public static int readVarInt(@NonNull ByteBuf buf) {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = buf.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new IllegalArgumentException("VarInt is bigger than maximum allowed");
        }

        return value;
    }

    /**
     * Reads a variable-length long from a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @since 1.0
     * @throws IllegalArgumentException if the long is bigger than maximum allowed
     * @return the variable-length long
     */
    public static long readVarLong(@NonNull ByteBuf buf) {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = buf.readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new IllegalArgumentException("VarLong is bigger than maximum allowed");
        }

        return value;
    }

    /**
     * Reads a string from a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the string
     * @since 1.0
     */
    public static @NonNull String readString(@NonNull ByteBuf buf) {
        int length = readVarInt(buf);

        if (length < 0 || length > MAX_STRING_SIZE)
            throw new IllegalArgumentException("Invalid length of a string - " + length);

        //if (this.buf.isReadable(length))
        //throw new IllegalArgumentException("A buffer does not contain at least " + length + " readable bytes");

        String string = buf.toString(buf.readerIndex(), length, StandardCharsets.UTF_8);
        buf.skipBytes(length);

        return string;
    }

    /**
     * Writes a variable-length integer to a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the variable-length integer
     * @since 1.0
     */
    public static void writeVarInt(@NonNull ByteBuf buf, int value) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                buf.writeByte(value);
                return;
            }

            buf.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7;
        }
    }

    /**
     * Writes a variable-length long to a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the variable-length long
     * @since 1.0
     */
    public static void writeVarLong(@NonNull ByteBuf buf, long value) {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                buf.writeByte((int) value);
                return;
            }

            buf.writeByte((int) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }

    /**
     * Writes as string to a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the string
     * @since 1.0
     */
    public static void writeString(@NonNull ByteBuf buf, @NonNull String value) {
        writeVarInt(buf, ByteBufUtil.utf8Bytes(value));
        buf.writeCharSequence(value, StandardCharsets.UTF_8);
    }
}