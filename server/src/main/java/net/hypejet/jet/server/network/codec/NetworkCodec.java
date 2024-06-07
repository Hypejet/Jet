package net.hypejet.jet.server.network.codec;

import io.netty.buffer.ByteBuf;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that reads and writes an {@linkplain T object} from a {@linkplain ByteBuf byte buf}.
 *
 * @param <T> a type of the object
 * @since 1.0
 */
public interface NetworkCodec<T> {
    /**
     * Reads an {@linkplain T object} from a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the object
     * @since 1.0
     */
    @NonNull T read(@NonNull ByteBuf buf);

    /**
     * Writes an {@linkplain T object} to a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param object the object
     * @since 1.0
     */
    void write(@NonNull ByteBuf buf, @NonNull T object);
}