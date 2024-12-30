package net.hypejet.jet.server.util;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A utility used for deserializing and serializing data from/to {@linkplain ByteBuf a byte buf}.
 *
 * @since 1.0
 * @see ByteBuf
 */
public final class NetworkUtil {

    private NetworkUtil() {}

    /**
     * Reads all remaining bytes from {@linkplain ByteBuf a byte buf}.
     *
     * @param buf the byte buf
     * @return the remaining bytes
     * @since 1.0
     */
    public static byte @NonNull [] readRemainingBytes(@NonNull ByteBuf buf) {
        return readBytes(buf, buf.readableBytes());
    }

    /**
     * Reads bytes from {@linkplain ByteBuf a byte buf}.
     *
     * @param buf the byte buf
     * @param length an amount of bytes to read
     * @return the bytes
     * @since 1.0
     */
    public static byte @NonNull [] readBytes(@NonNull ByteBuf buf, int length) {
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return bytes;
    }

    /**
     * Reads an optional value from {@linkplain ByteBuf a byte buf}.
     *
     * @param reader a network reader, which should read the value
     * @param buf the byte buf
     * @return the value, {@code null} if not present
     * @param <T> a type of the value
     * @since 1.0
     */
    public static <T> @Nullable T readOptional(@NonNull NetworkReader<T> reader, @NonNull ByteBuf buf) {
        if (!buf.readBoolean()) return null;
        return reader.read(buf);
    }

    /**
     * Writes an optional value to {@linkplain ByteBuf a byte buf}.
     *
     * @param value the value, {@code null} if not present
     * @param writer a network writer, which should write the value
     * @param buf the byte buf
     * @param <T> a type of the value
     * @since 1.0
     */
    public static <T> void writeOptional(@Nullable T value, @NonNull NetworkWriter<T> writer, @NonNull ByteBuf buf) {
        boolean present = value != null;
        buf.writeBoolean(present);
        if (present) writer.write(buf, value);
    }
}