package net.hypejet.jet.server.network.codec;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Something reading objects with certain type from a {@linkplain ByteBuf byte buf}.
 *
 * @param <T> the type of objects that this network reader reads
 * @since 1.0
 * @see ByteBuf
 */
@FunctionalInterface
public interface NetworkReader<T> {
    /**
     * Reads an object from the specified {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf to read the object from
     * @param registryManager registry manager of the server that the object is being read for
     * @return the read object
     * @since 1.0
     */
    @NonNull T read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager);
}