package net.hypejet.jet.server.util;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A utility used for deserializing and serializing data from/to a {@linkplain ByteBuf byte buf}.
 *
 * @since 1.0
 * @author Codestech
 * @see ByteBuf
 */
public final class NetworkUtil {

    private NetworkUtil() {}

    /**
     * Reads all remaining bytes from a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the remaining bytes
     * @since 1.0
     */
    public static byte @NonNull [] readRemainingBytes(@NonNull ByteBuf buf) {
        return readBytes(buf, buf.readableBytes());
    }

    /**
     * Reads bytes from a {@linkplain ByteBuf byte buf}.
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
     * Reads an optional value from a {@linkplain ByteBuf byte buf}.
     *
     * @param codec a codec, which reads the value
     * @param buf the byte buf
     * @return the value, {@code null} if not present
     * @param <T> a type of the value
     * @since 1.0
     */
    public static <T> @Nullable T readOptional(@NonNull NetworkCodec<T> codec, @NonNull ByteBuf buf) {
        if (!buf.readBoolean()) return null;
        return codec.read(buf);
    }

    /**
     * Writes an optional value to a {@linkplain ByteBuf byte buf}.
     *
     * @param value the value, {@code null} if not present
     * @param codec a codec, which writes the value
     * @param buf the byte buf
     * @param <T> a type of the value
     * @since 1.0
     */
    public static <T> void writeOptional(@Nullable T value, @NonNull NetworkCodec<T> codec, @NonNull ByteBuf buf) {
        boolean present = value != null;
        buf.writeBoolean(present);
        if (present) codec.write(buf, value);
    }
}