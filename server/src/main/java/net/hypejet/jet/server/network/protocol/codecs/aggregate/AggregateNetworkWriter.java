package net.hypejet.jet.server.network.protocol.codecs.aggregate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes an aggregate and elements of it.
 *
 * @param <A> a type of the aggregate
 * @since 1.0
 * @author Codestech
 * @see NetworkWriter
 */
public abstract class AggregateNetworkWriter<A> implements NetworkWriter<A> {

    private final int maxLength;

    /**
     * Constructs the {@linkplain AggregateNetworkWriter aggregate network writer}.
     *
     * @param maxLength a max length that an aggregate can have
     * @since 1.0
     */
    protected AggregateNetworkWriter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public final void write(@NonNull ByteBuf buf, @NonNull A object) {
        int length = this.length(object);

        if (length > this.maxLength)
            throw tooLongAggregateException(length, this.maxLength);

        VarIntNetworkCodec.INSTANCE.write(buf, length);
        this.encodeElements(object, buf);
    }

    /**
     * Gets a length of an aggregate.
     *
     * @param aggregate the aggregate
     * @return the length
     * @since 1.0
     */
    protected abstract int length(@NonNull A aggregate);

    /**
     * Encodes elements of an aggregate.
     *
     * @param aggregate the aggregate
     * @param buf a byte buf to encode the elements to
     * @since 1.0
     */
    protected abstract void encodeElements(@NonNull A aggregate, @NonNull ByteBuf buf);

    private static @NonNull IllegalArgumentException tooLongAggregateException(int length, int maxLength) {
        return new IllegalArgumentException(String.format(
                "The aggregate is long than allowed (%s > %s).",
                length, maxLength
        ));
    }
}