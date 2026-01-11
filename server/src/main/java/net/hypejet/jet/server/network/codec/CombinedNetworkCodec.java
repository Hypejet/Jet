package net.hypejet.jet.server.network.codec;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which uses {@linkplain NetworkReader a network reader}
 * and {@linkplain NetworkWriter a network writer} specified for reading and writing.
 *
 * @param <T> a type of the object that the codec should read and write
 * @since 1.0
 * @see NetworkReader
 * @see NetworkWriter
 * @see NetworkCodec
 */
public final class CombinedNetworkCodec<T> implements NetworkCodec<T> {

    private final NetworkReader<T> reader;
    private final NetworkWriter<T> writer;

    /**
     * Constructs the {@linkplain CombinedNetworkCodec combined network codec}.
     *
     * @param reader the network reader
     * @param writer the network writer
     * @since 1.0
     */
    public CombinedNetworkCodec(@NonNull NetworkReader<T> reader, @NonNull NetworkWriter<T> writer) {
        this.reader = Objects.requireNonNull(reader, "reader");
        this.writer = Objects.requireNonNull(writer, "writer");
    }

    @Override
    public @NonNull T read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return this.reader.read(buf, registryManager);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull T object) {
        this.writer.write(buf, registryManager, object);
    }
}