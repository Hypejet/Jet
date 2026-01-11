package net.hypejet.jet.server.network.codec.aggregate.array.bytes;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkReader;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain AggregateNetworkReader an aggregate network reader}, which reads a byte array.
 *
 * @since 1.0
 * @see AggregateNetworkReader
 */
public final class ByteArrayNetworkReader extends AggregateNetworkReader<byte[]> {

    /**
     * An instance of the {@linkplain ByteArrayNetworkReader byte array network reader}, which allows lengths
     * up to {@link Integer#MAX_VALUE}.
     *
     * @since 1.0
     */
    public static final ByteArrayNetworkReader INSTANCE = new ByteArrayNetworkReader(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain ByteArrayNetworkReader byte array network reader}.
     *
     * @param maxLength a max length that a byte array can have
     * @since 1.0
     */
    public ByteArrayNetworkReader(int maxLength) {
        super(maxLength);
    }

    @Override
    protected byte @NonNull [] decodeElements(int length, @NonNull ByteBuf buf,
                                              @NonNull JetRegistryManager registryManager) {
        byte[] bytes = new byte[length];
        for (int index = 0; index < length; index++)
            bytes[index] = buf.readByte();
        return bytes;
    }
}