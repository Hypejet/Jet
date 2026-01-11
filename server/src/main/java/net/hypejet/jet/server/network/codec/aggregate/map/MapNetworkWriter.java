package net.hypejet.jet.server.network.codec.aggregate.map;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * An {@linkplain AggregateNetworkWriter aggregate network-writer} of {@linkplain Map maps}.
 *
 * @param <K> the key type of maps that this map network-writer writes
 * @param <V> the value type of maps that this map network-writer writes
 * @since 1.0
 * @see Map
 * @see AggregateNetworkWriter
 */
public final class MapNetworkWriter<K, V> extends AggregateNetworkWriter<Map<K, V>> {

    private final NetworkWriter<K> keyWriter;
    private final NetworkWriter<V> valueWriter;

    /**
     * Constructs the {@linkplain MapNetworkWriter map network-writer} with max map size of {@link Integer#MAX_VALUE}.
     *
     * @param keyWriter a network writer that should write keys of maps that the map network-writer should write
     * @param valueWriter a network writer that should write values of maps that the map network-writer should write
     * @since 1.0
     */
    public MapNetworkWriter(@NonNull NetworkWriter<K> keyWriter, @NonNull NetworkWriter<V> valueWriter) {
        this(Integer.MAX_VALUE, keyWriter, valueWriter);
    }

    /**
     * Constructs the {@linkplain MapNetworkWriter map network-writer}.
     *
     * @param maxLength a maximum length that maps written by the constructed map network-write can have
     * @param keyWriter a network writer that should write keys of maps that the map network-writer should write
     * @param valueWriter a network writer that should write values of maps that the map network-writer should write
     * @since 1.0
     */
    public MapNetworkWriter(int maxLength, @NonNull NetworkWriter<K> keyWriter,
                            @NonNull NetworkWriter<V> valueWriter) {
        super(maxLength, true);
        this.keyWriter = Objects.requireNonNull(keyWriter, "key writer");
        this.valueWriter = Objects.requireNonNull(valueWriter, "value writer");
    }

    @Override
    protected int length(@NonNull Map<K, V> aggregate) {
        return aggregate.size();
    }

    @Override
    protected void encodeElements(@NonNull Map<K, V> aggregate, @NonNull ByteBuf buf,
                                  @NonNull JetRegistryManager registryManager) {
        aggregate.forEach((key, value) -> {
            this.keyWriter.write(buf, registryManager, key);
            this.valueWriter.write(buf, registryManager, value);
        });
    }
}