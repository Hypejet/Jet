package net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an {@linkplain AggregateNetworkCodec aggregate network codec}, which reads and writes a byte array.
 *
 * @since 1.0
 * @author Codestech
 * @see AggregateNetworkCodec
 */
public final class ByteArrayNetworkCodec extends AggregateNetworkCodec<byte[]> {

    private static final ByteArrayNetworkCodec INSTANCE = new ByteArrayNetworkCodec(Integer.MAX_VALUE);

    private ByteArrayNetworkCodec(int maxLength) {
        super(maxLength);
    }

    @Override
    protected int length(byte @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(byte @NonNull [] aggregate, @NonNull ByteBuf buf) {
        for (byte element : aggregate) {
            buf.writeByte(element);
        }
    }

    @Override
    protected byte @NonNull [] decodeElements(int length, @NonNull ByteBuf buf) {
        byte[] bytes = new byte[length];
        for (int index = 0; index < length; index++)
            bytes[index] = buf.readByte();
        return bytes;
    }

    /**
     * Gets an instance of the {@linkplain ByteArrayNetworkCodec byte array network codec}, which can handle
     * up to {@link Integer#MAX_VALUE} elements.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ByteArrayNetworkCodec instance() {
        return INSTANCE;
    }

    /**
     * Creates a new {@linkplain ByteArrayNetworkCodec byte array network codec}.
     *
     * @param maxLength a max length of an array that the codec handles
     * @return the codec, {@link #INSTANCE} if the max length specified is {@link Integer#MAX_VALUE}
     * @since 1.0
     */
    public static @NonNull ByteArrayNetworkCodec create(int maxLength) {
        if (maxLength == Integer.MAX_VALUE)
            return INSTANCE;
        return new ByteArrayNetworkCodec(maxLength);
    }
}