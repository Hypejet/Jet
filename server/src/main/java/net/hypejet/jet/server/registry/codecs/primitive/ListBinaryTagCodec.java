package net.hypejet.jet.server.registry.codecs.primitive;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jspecify.annotations.NullMarked;

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
@NullMarked
public final class ListBinaryTagCodec<V> implements BinaryTagCodec<List<V>> {

    private final BinaryTagCodec<V> valueCodec;
    private final boolean compact;

    /**
     * Constructs the {@linkplain ListBinaryTagCodec list binary-tag codec} without compact list support.
     *
     * @param valueCodec a binary-tag codec that should handle serialization of list values
     * @since 1.0
     */
    public ListBinaryTagCodec(BinaryTagCodec<V> valueCodec) {
        this(valueCodec, false);
    }

    /**
     * Constructs the {@linkplain ListBinaryTagCodec list binary-tag codec}.
     *
     * @param valueCodec a binary-tag codec that should handle serialization of list values
     * @param compact whether the codec should support direct value serialization if the list has only one element
     * @since 1.0
     */
    public ListBinaryTagCodec(BinaryTagCodec<V> valueCodec, boolean compact) {
        this.valueCodec = Objects.requireNonNull(valueCodec, "value codec");
        this.compact = compact;
    }

    @Override
    public List<V> decode(BinaryTag binaryTag, JetMinecraftServer server) {
        try {
            ListBinaryTag tag = ((ListBinaryTag) binaryTag).unwrapHeterogeneity();
            List<V> list = new ArrayList<>();
            for (BinaryTag valueTag : tag.unwrapHeterogeneity())
                list.add(this.valueCodec.decode(valueTag, server));
            return List.copyOf(list);
        } catch (Exception listDecodeException) {
            if (!this.compact)
                throw new IllegalArgumentException("The encoded tag must be of list type to decode it to a list");

            try {
                return List.of(this.valueCodec.decode(binaryTag, server));
            } catch (Exception valueDecodeException) {
                throw new IllegalArgumentException("Failed to decode the compact list");
            }
        }
    }

    @Override
    public BinaryTag encode(List<V> value, JetMinecraftServer server) {
        if (this.compact && value.size() == 1) {
            return this.valueCodec.encode(value.getFirst(), server);
        } else {
            ListBinaryTag.Builder<BinaryTag> builder = ListBinaryTag.heterogeneousListBinaryTag();
            for (V listValue : value)
                builder.add(this.valueCodec.encode(listValue, server));
            return builder.build().wrapHeterogeneity();
        }
    }
}