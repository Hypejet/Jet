package net.hypejet.jet.server.network.codec;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Something writing objects with certain type to a {@linkplain ByteBuf byte buf}.
 *
 * @param <T> the type of objects that this network writer writes
 * @since 1.0
 * @see ByteBuf
 */
@FunctionalInterface
public interface NetworkWriter<T> {
    /**
     * Writes the specified object {@linkplain T object} to the specified {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf to write the object to
     * @param registryManager registry manager of the server that the object is being written for
     * @param object the object to write
     * @since 1.0
     */
    void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull T object);
}