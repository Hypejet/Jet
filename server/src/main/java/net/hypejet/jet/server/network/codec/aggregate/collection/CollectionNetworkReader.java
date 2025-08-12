package net.hypejet.jet.server.network.codec.aggregate.collection;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents {@linkplain AggregateNetworkReader an aggregate network reader}, which reads
 * {@linkplain Collection a collection}.
 *
 * @param <E> a type of elements of the collection
 * @since 1.0
 * @see Collection
 * @see AggregateNetworkReader
 */
public final class CollectionNetworkReader<E> extends AggregateNetworkReader<Collection<E>> {

    private final NetworkReader<E> elementReader;

    /**
     * Constructs the {@linkplain CollectionNetworkReader collection network reader} with support for lengths up to
     * {@link Integer#MAX_VALUE}.
     *
     * @param elementReader a network reader to read elements of collection with
     * @since 1.0
     */
    public CollectionNetworkReader(@NonNull NetworkReader<E> elementReader) {
        super(Integer.MAX_VALUE);
        this.elementReader = Objects.requireNonNull(elementReader, "element reader");
    }

    /**
     * Constructs the {@linkplain CollectionNetworkReader collection network reader}.
     *
     * @param maxLength a max length that a collection can have
     * @param elementReader a network reader to read elements of collection with
     * @since 1.0
     */
    public CollectionNetworkReader(int maxLength, @NonNull NetworkReader<E> elementReader) {
        super(maxLength);
        this.elementReader = Objects.requireNonNull(elementReader, "element reader");
    }

    @Override
    protected @NonNull Collection<E> decodeElements(int length, @NonNull ByteBuf buf) {
        Collection<E> collection = new ArrayList<>();
        for (int index = 0; index < length; index++)
            collection.add(this.elementReader.read(buf));
        return List.copyOf(collection);
    }
}