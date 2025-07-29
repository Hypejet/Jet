package net.hypejet.jet.server.registry.codecs.primitive;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain List lists}.
 *
 * @param <V> the value type of lists whose serialization is handled by this binary tag codec
 * @since 1.0
 * @see List
 * @see BinaryTagCodec
 */
public final class ListBinaryTagCodec<V> implements BinaryTagCodec<List<V>> {

    private final BinaryTagCodec<V> valueCodec;

    /**
     * Constructs the {@linkplain ListBinaryTagCodec list binary tag codec}.
     *
     * @param valueCodec a binary tag codec that should handle serialization of list values
     * @since 1.0
     */
    public ListBinaryTagCodec(@NonNull BinaryTagCodec<V> valueCodec) {
        this.valueCodec = Objects.requireNonNull(valueCodec, "value codec");
    }

    @Override
    public @NotNull List<V> decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof ListBinaryTag tag) {
            List<V> list = new ArrayList<>();
            for (BinaryTag valueTag : tag.unwrapHeterogeneity())
                list.add(this.valueCodec.decode(valueTag));
            return List.copyOf(list);
        } else {
            throw new IllegalArgumentException("The encoded tag must be of list type to decode it to a list");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull List<V> decoded) throws Exception {
        ListBinaryTag.Builder<BinaryTag> builder = ListBinaryTag.heterogeneousListBinaryTag();
        for (V value : decoded)
            builder.add(this.valueCodec.encode(value));
        return builder.build().wrapHeterogeneity();
    }
}