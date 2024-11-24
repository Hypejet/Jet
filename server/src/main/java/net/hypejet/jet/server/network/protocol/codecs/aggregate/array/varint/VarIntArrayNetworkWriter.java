package net.hypejet.jet.server.network.protocol.codecs.aggregate.array.varint;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain AggregateNetworkWriter an aggregate network writer}, which writes a variable-length integer
 * array.
 *
 * @since 1.0
 * @author Codestech
 * @see AggregateNetworkWriter
 */
public final class VarIntArrayNetworkWriter extends AggregateNetworkWriter<int[]> {

    /**
     * An instance {@linkplain VarIntArrayNetworkReader a variable-length integer array network reader}, which
     * allows length up to {@link Integer#MAX_VALUE}.
     *
     * @since 1.0
     */
    public static final VarIntArrayNetworkWriter INSTANCE = new VarIntArrayNetworkWriter(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain VarIntArrayNetworkWriter variable-length integer array network writer}.
     *
     * @param maxLength a max length that an array can have
     * @since 1.0
     */
    public VarIntArrayNetworkWriter(int maxLength) {
        super(maxLength);
    }

    @Override
    protected int length(int @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(int @NonNull [] aggregate, @NonNull ByteBuf buf) {
        for (int element : aggregate)
            VarIntNetworkCodec.instance().write(buf, element);
    }
}