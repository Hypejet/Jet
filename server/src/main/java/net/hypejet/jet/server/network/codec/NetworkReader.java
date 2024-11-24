package net.hypejet.jet.server.network.codec;

import io.netty.buffer.ByteBuf;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a function that reads {@linkplain T an object} from {@linkplain ByteBuf a byte buf}.
 *
 * @param <T> a type of the object
 * @since 1.0
 * @author Codestech
 */
@FunctionalInterface
public interface NetworkReader<T> {
    /**
     * Reads {@linkplain T an object} from {@linkplain ByteBuf a byte buf}.
     *
     * @param buf the byte buf
     * @return the object
     * @since 1.0
     */
    @NonNull T read(@NonNull ByteBuf buf);
}