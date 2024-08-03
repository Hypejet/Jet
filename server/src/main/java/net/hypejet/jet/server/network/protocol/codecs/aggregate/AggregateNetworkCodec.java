package net.hypejet.jet.server.network.protocol.codecs.aggregate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes aggregates and their elements.
 *
 * @param <T> a type of the aggregate that this network codec reads and writes
 * @since 1.0
 * @author Codestech
 * @see NetworkCodec
 */
public abstract class AggregateNetworkCodec<T> implements NetworkCodec<T> {

    private final int maxLength;

    /**
     * Constructs the {@linkplain AggregateNetworkCodec aggregate network codec}.
     *
     * @param maxLength a max length that an aggregate can have
     * @since 1.0
     */
    protected AggregateNetworkCodec(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public final @NonNull T read(@NonNull ByteBuf buf) {
        int length = VarIntNetworkCodec.instance().read(buf);
        if (length > this.maxLength)
            throw tooLongAggregateException(length, this.maxLength);
        return this.decodeElements(length, buf);
    }

    @Override
    public final void write(@NonNull ByteBuf buf, @NonNull T object) {
        int length = this.length(object);

        if (length > this.maxLength)
            throw tooLongAggregateException(length, this.maxLength);

        VarIntNetworkCodec.instance().write(buf, length);
        this.encodeElements(object, buf);
    }

    /**
     * Gets a length of an aggregate.
     *
     * @param aggregate the aggregate
     * @return the length
     * @since 1.0
     */
    protected abstract int length(@NonNull T aggregate);

    /**
     * Encodes elements of an aggregate.
     *
     * @param aggregate the aggregate
     * @param buf a byte buf to encode the elements to
     * @since 1.0
     */
    protected abstract void encodeElements(@NonNull T aggregate, @NonNull ByteBuf buf);

    /**
     * Decodes elements and creates an aggregate with those elements.
     *
     * @param length an amount of elements that the aggregate should have
     * @param buf a byte buf to decode elements from
     * @return the aggregate created
     * @since 1.0
     */
    protected abstract @NonNull T decodeElements(int length, @NonNull ByteBuf buf);

    private static @NonNull IllegalArgumentException tooLongAggregateException(int length, int maxLength) {
        return new IllegalArgumentException(
                "A length of an aggregate is higher than allowed (" + length + " > " + maxLength + ")."
        );
    }
}