package net.hypejet.jet.server.network.codec;

import io.netty.buffer.ByteBuf;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a function that writes {@linkplain T an object} to {@linkplain ByteBuf a byte buf}.
 *
 * @param <T> a type of the object
 * @since 1.0
 * @author Codestech
 */
@FunctionalInterface
public interface NetworkWriter<T> {
    /**
     * Writes {@linkplain T an object} to {@linkplain ByteBuf a byte buf}.
     *
     * @param buf the byte buf
     * @param object the object
     * @since 1.0
     */
    void write(@NonNull ByteBuf buf, @NonNull T object);
}