package net.hypejet.jet.server.network.codec.aggregate.array.bytes;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain AggregateNetworkWriter an aggregate network writer}, which writes a byte array.
 *
 * @since 1.0
 * @see AggregateNetworkWriter
 */
public final class ByteArrayNetworkWriter extends AggregateNetworkWriter<byte[]> {

    /**
     * An instance of the {@linkplain ByteArrayNetworkWriter byte array network writer}, which allows for lengths
     * up to {@link Integer#MAX_VALUE} and encodes them.
     *
     * @since 1.0
     */
    public static final ByteArrayNetworkWriter INSTANCE = new ByteArrayNetworkWriter(Integer.MAX_VALUE, true);

    /**
     * Constructs the {@linkplain ByteArrayNetworkWriter byte array network writer}.
     *
     * @param maxLength a max length that a byte array can have
     * @param encodeLength whether length of byte arrays should be encoded
     * @since 1.0
     */
    public ByteArrayNetworkWriter(int maxLength, boolean encodeLength) {
        super(maxLength, encodeLength);
    }

    @Override
    protected int length(byte @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(byte @NonNull [] aggregate, @NonNull ByteBuf buf) {
        for (byte element : aggregate)
            buf.writeByte(element);
    }
}