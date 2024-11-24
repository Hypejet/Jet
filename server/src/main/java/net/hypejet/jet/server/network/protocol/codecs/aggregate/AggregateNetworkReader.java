package net.hypejet.jet.server.network.protocol.codecs.aggregate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads an aggregate and elements of it.
 *
 * @param <A> a type of the aggregate
 * @since 1.0
 * @author Codestech
 * @see NetworkReader
 */
public abstract class AggregateNetworkReader<A> implements NetworkReader<A> {

    private final int maxLength;

    /**
     * Constructs the {@linkplain AggregateNetworkReader aggregate network reader}.
     *
     * @param maxLength a max length that an aggregate can have
     * @since 1.0
     */
    protected AggregateNetworkReader(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public @NonNull A read(@NonNull ByteBuf buf) {
        int length = VarIntNetworkCodec.instance().read(buf);
        if (length > this.maxLength)
            throw tooLongAggregateException(length, this.maxLength);
        return this.decodeElements(length, buf);
    }

    /**
     * Decodes elements and creates an aggregate with those elements.
     *
     * @param length an amount of elements that the aggregate should have
     * @param buf a byte buf to decode elements from
     * @return the aggregate created
     * @since 1.0
     */
    protected abstract @NonNull A decodeElements(int length, @NonNull ByteBuf buf);

    private static @NonNull IllegalArgumentException tooLongAggregateException(int length, int maxLength) {
        return new IllegalArgumentException(String.format(
                "The aggregate is long than allowed (%s > %s).",
                length, maxLength
        ));
    }
}