package net.hypejet.jet.server.network.protocol.codecs.aggregate.array.object;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain AggregateNetworkWriter an aggregate network writer}, which writes an array of objects
 * with a type specified.
 *
 * @param <E> the type of the objects
 * @since 1.0
 * @author Codestech
 * @see AggregateNetworkWriter
 */
public final class ObjectArrayNetworkWriter<E> extends AggregateNetworkWriter<E[]> {

    private final NetworkWriter<E> elementWriter;

    /**
     * Constructs the {@linkplain ObjectArrayNetworkWriter an object array network writer} with support for lengths
     * up to {@link Integer#MAX_VALUE}.
     *
     * @param elementWriter a network writer, which writes elements of the array
     * @since 1.0
     */
    public ObjectArrayNetworkWriter(@NonNull NetworkWriter<E> elementWriter) {
        super(Integer.MAX_VALUE);
        this.elementWriter = NullabilityUtil.requireNonNull(elementWriter, "element writer");
    }

    /**
     * Constructs the {@linkplain ObjectArrayNetworkWriter an object array network writer}.
     *
     * @param maxLength a max length that an object array can have
     * @param elementWriter a network writer, which writes elements of the array
     * @since 1.0
     */
    public ObjectArrayNetworkWriter(int maxLength, @NonNull NetworkWriter<E> elementWriter) {
        super(maxLength);
        this.elementWriter = NullabilityUtil.requireNonNull(elementWriter, "element writer");
    }

    @Override
    protected int length(E @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(E @NonNull [] aggregate, @NonNull ByteBuf buf) {
        for (E element : aggregate)
            this.elementWriter.write(buf, element);
    }
}