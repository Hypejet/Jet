package net.hypejet.jet.server.network.codec.index;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.kyori.adventure.util.Index;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * A {@linkplain NetworkCodec network codec} handling serialization using an {@linkplain Index index}.
 *
 * @param <K> the key type of the index
 * @param <V> the value type of the index
 * @since 1.0
 * @see Index
 * @see NetworkCodec
 */
public final class IndexNetworkCodec<K, V> implements NetworkCodec<K> {

    private final Index<K, V> index;
    private final NetworkCodec<V> codec;

    /**
     * Constructs the {@linkplain IndexNetworkCodec index network codec}.
     *
     * @param index the index to use
     * @param codec a network reader handling serialization of values specified in the specified index
     * @since 1.0
     */
    public IndexNetworkCodec(@NonNull Index<K, V> index, @NonNull NetworkCodec<V> codec) {
        this.index = Objects.requireNonNull(index, "index");
        this.codec = Objects.requireNonNull(codec, "codec");
    }

    @Override
    public @NonNull K read(@NonNull ByteBuf buf) {
        return this.index.keyOrThrow(this.codec.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull K object) {
        this.codec.write(buf, this.index.valueOrThrow(object));
    }
}