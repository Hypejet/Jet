package net.hypejet.jet.server.network.codec.aggregate.array.varint;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain AggregateNetworkWriter an aggregate network writer}, which writes a variable-length integer
 * array.
 *
 * @since 1.0
 * @see AggregateNetworkWriter
 */
public final class VarIntArrayNetworkWriter extends AggregateNetworkWriter<int[]> {

    /**
     * An instance of the {@linkplain VarIntArrayNetworkReader variable-length integer array network reader}, which
     * allows for lengths up to {@link Integer#MAX_VALUE} and encodes them.
     *
     * @since 1.0
     */
    public static final VarIntArrayNetworkWriter INSTANCE = new VarIntArrayNetworkWriter(Integer.MAX_VALUE, true);

    /**
     * Constructs the {@linkplain VarIntArrayNetworkWriter variable-length integer array network writer}.
     *
     * @param maxLength a max length that an array can have
     * @param encodeLength whether the length of variable-length integer arrays should be encoded
     * @since 1.0
     */
    public VarIntArrayNetworkWriter(int maxLength, boolean encodeLength) {
        super(maxLength, encodeLength);
    }

    @Override
    protected int length(int @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(int @NonNull [] aggregate, @NonNull ByteBuf buf,
                                  @NonNull JetRegistryManager registryManager) {
        for (int element : aggregate)
            VarIntNetworkCodec.INSTANCE.write(buf, registryManager, element);
    }
}