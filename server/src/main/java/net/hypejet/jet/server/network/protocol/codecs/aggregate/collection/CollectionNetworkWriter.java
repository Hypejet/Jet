package net.hypejet.jet.server.network.protocol.codecs.aggregate.collection;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents {@linkplain AggregateNetworkWriter an aggregate network writer}, which
 * writes {@linkplain Collection a collection}.
 *
 * @param <E> a type of the elements of the collection
 * @since 1.0
 * @author Codestech
 * @see Collection
 * @see AggregateNetworkWriter
 */
public final class CollectionNetworkWriter<E> extends AggregateNetworkWriter<Collection<E>> {

    private final NetworkWriter<E> elementWriter;

    /**
     * Constructs the {@linkplain CollectionNetworkWriter collection network writer} with a support for lengths
     * up to {@link Integer#MAX_VALUE}.
     *
     * @param elementWriter a network writer to read elements of collections with
     * @since 1.0
     */
    public CollectionNetworkWriter(@NonNull NetworkWriter<E> elementWriter) {
        this(Integer.MAX_VALUE, elementWriter);
    }

    /**
     * Constructs the {@linkplain CollectionNetworkWriter collection network writer}.
     *
     * @param maxLength a max length that a collection can have
     * @param elementWriter a network writer to read elements of collections with
     * @since 1.0
     */
    public CollectionNetworkWriter(int maxLength, @NonNull NetworkWriter<E> elementWriter) {
        super(maxLength);
        this.elementWriter = NullabilityUtil.requireNonNull(elementWriter, "element writer");
    }

    @Override
    protected int length(@NonNull Collection<E> aggregate) {
        return aggregate.size();
    }

    @Override
    protected void encodeElements(@NonNull Collection<E> aggregate, @NonNull ByteBuf buf) {
        aggregate.forEach(element -> this.elementWriter.write(buf, element));
    }
}