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
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain List lists}.
 *
 * @param <V> the value type of lists whose serialization is handled by this binary tag codec
 * @since 1.0
 * @see List
 * @see BinaryTagCodec
 */
public final class ListBinaryTagCodec<V> implements BinaryTagCodec<List<V>> {

    private final BinaryTagCodec<V> valueCodec;
    private final boolean compact;

    /**
     * Constructs the {@linkplain ListBinaryTagCodec list binary-tag codec} without compact list support.
     *
     * @param valueCodec a binary-tag codec that should handle serialization of list values
     * @since 1.0
     */
    public ListBinaryTagCodec(@NonNull BinaryTagCodec<V> valueCodec) {
        this(valueCodec, false);
    }

    /**
     * Constructs the {@linkplain ListBinaryTagCodec list binary-tag codec}.
     *
     * @param valueCodec a binary-tag codec that should handle serialization of list values
     * @param compact whether the codec should support direct value serialization if the list has only one element
     * @since 1.0
     */
    public ListBinaryTagCodec(@NonNull BinaryTagCodec<V> valueCodec, boolean compact) {
        this.valueCodec = Objects.requireNonNull(valueCodec, "value codec");
        this.compact = compact;
    }

    @Override
    public @NotNull List<V> decode(@NotNull BinaryTag encoded) throws Exception {
        try {
            ListBinaryTag tag = ((ListBinaryTag) encoded).unwrapHeterogeneity();
            List<V> list = new ArrayList<>();
            for (BinaryTag valueTag : tag.unwrapHeterogeneity())
                list.add(this.valueCodec.decode(valueTag));
            return List.copyOf(list);
        } catch (Exception listDecodeException) {
            if (!this.compact)
                throw new IllegalArgumentException("The encoded tag must be of list type to decode it to a list");

            try {
                return List.of(this.valueCodec.decode(encoded));
            } catch (Exception valueDecodeException) {
                throw new IllegalArgumentException("Failed to decode the compact list");
            }
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull List<V> decoded) throws Exception {
        if (this.compact && decoded.size() == 1) {
            return this.valueCodec.encode(decoded.getFirst());
        } else {
            ListBinaryTag.Builder<BinaryTag> builder = ListBinaryTag.heterogeneousListBinaryTag();
            for (V value : decoded)
                builder.add(this.valueCodec.encode(value));
            return builder.build().wrapHeterogeneity();
        }
    }
}