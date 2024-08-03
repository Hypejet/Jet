package net.hypejet.jet.server.util;

import io.netty.buffer.ByteBuf;
import org.checkerframework.checker.nullness.qual.NonNull;

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
}