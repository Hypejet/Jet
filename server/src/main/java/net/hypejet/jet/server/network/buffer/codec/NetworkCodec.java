package net.hypejet.jet.server.network.buffer.codec;

import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that writes and reads an object from a {@linkplain NetworkBuffer network buffer}.
 *
 * @param <T> a type of the object
 * @since 1.0
 */
public interface NetworkCodec<T> {
    /**
     * Writes an {@linkplain T object} to the {@linkplain NetworkBuffer network buffer}.
     *
     * @param buffer the network buffer
     * @param object the object
     * @since 1.0
     */
    void write(@NonNull NetworkBuffer buffer, @NonNull T object);

    /**
     * Reads an {@linkplain T object} from the {@linkplain NetworkBuffer network buffer}.
     *
     * @param buffer the network buffer
     * @return the object
     * @since 1.0
     */
    @NonNull T read(@NonNull NetworkBuffer buffer);
}