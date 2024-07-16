package net.hypejet.jet.server.network.protocol.codecs.aggregate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents an {@linkplain AggregateNetworkCodec aggregate network codec}, which reads and writes
 * a {@linkplain Collection collection}.
 *
 * <p>Note that the collection cannot contain elements, which are {@code null}.</p>
 *
 * @param <T> a type of elements in the collection
 * @since 1.0
 * @author Codestech
 * @see Collection
 * @see AggregateNetworkCodec
 */
public final class CollectionNetworkCodec<T> extends AggregateNetworkCodec<Collection<T>> {

    private final NetworkCodec<T> elementCodec;

    private CollectionNetworkCodec(@NonNull NetworkCodec<T> elementCodec, int maxLength) {
        super(maxLength);
        this.elementCodec = Objects.requireNonNull(elementCodec, "The element codec must not be null");
    }

    @Override
    protected int length(@NonNull Collection<T> aggregate) {
        return aggregate.size();
    }

    @Override
    protected void encodeElements(@NonNull Collection<T> aggregate, @NonNull ByteBuf buf) {
        aggregate.forEach(element -> this.elementCodec.write(buf, element));
    }

    @Override
    protected @NonNull Collection<T> decodeElements(int length, @NonNull ByteBuf buf) {
        List<T> list = new ArrayList<>(length);
        for (int index = 0; index < length; index++)
            list.set(index, this.elementCodec.read(buf));
        return List.copyOf(list);
    }

    /**
     * Creates a {@linkplain CollectionNetworkCodec collection network codec}, with maximum length
     * of {@link Integer#MAX_VALUE}.
     *
     * @param elementCodec a network codec, which reads and writes elements of the collection
     * @since 1.0
     */
    public static <T> @NonNull CollectionNetworkCodec<T> create(@NonNull NetworkCodec<T> elementCodec) {
        return create(elementCodec, Integer.MAX_VALUE);
    }

    /**
     * Creates a {@linkplain CollectionNetworkCodec collection network codec}.
     *
     * @param elementCodec a network codec, which reads and writes elements of the collection
     * @param maxLength a maximum length of elements of collections that the codec should handle
     * @since 1.0
     */
    public static <T> @NonNull CollectionNetworkCodec<T> create(@NonNull NetworkCodec<T> elementCodec, int maxLength) {
        return new CollectionNetworkCodec<>(elementCodec, maxLength);
    }
}