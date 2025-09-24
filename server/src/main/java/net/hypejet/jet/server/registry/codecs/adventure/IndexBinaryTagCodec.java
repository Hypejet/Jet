package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} encoding keys of an {@linkplain Index index} by converting
 * them to values and using the specified codec.
 *
 * @param <K> the key type of the index
 * @param <V> the value type of the index
 * @since 1.0
 * @see Index
 * @see BinaryTagCodec
 */
@NullMarked
public final class IndexBinaryTagCodec<K, V> implements BinaryTagCodec<K> {

    private final Index<K, V> index;
    private final BinaryTagCodec<V> valueCodec;

    /**
     * Constructs the {@linkplain IndexBinaryTagCodec index binary tag codec}.
     *
     * @param index the index to convert keys and values with
     * @param valueCodec the codec to write index values with
     * @since 1.0
     */
    public IndexBinaryTagCodec(Index<K, V> index, BinaryTagCodec<V> valueCodec) {
        this.index = Objects.requireNonNull(index, "index");
        this.valueCodec = Objects.requireNonNull(valueCodec, "value codec");
    }

    @Override
    public K decode(BinaryTag binaryTag, JetMinecraftServer server) {
        return this.index.keyOrThrow(this.valueCodec.decode(binaryTag, server));
    }

    @Override
    public BinaryTag encode(K value, JetMinecraftServer server) {
        return this.valueCodec.encode(this.index.valueOrThrow(value), server);
    }
}