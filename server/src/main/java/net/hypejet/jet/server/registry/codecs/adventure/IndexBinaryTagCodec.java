package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

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
    public IndexBinaryTagCodec(@NonNull Index<K, V> index, @NonNull BinaryTagCodec<V> valueCodec) {
        this.index = Objects.requireNonNull(index, "index");
        this.valueCodec = Objects.requireNonNull(valueCodec, "value codec");
    }

    @Override
    public @NotNull K decode(@NotNull BinaryTag encoded) throws Exception {
        return this.index.keyOrThrow(this.valueCodec.decode(encoded));
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull K decoded) throws Exception {
        return this.valueCodec.encode(this.index.valueOrThrow(decoded));
    }
}