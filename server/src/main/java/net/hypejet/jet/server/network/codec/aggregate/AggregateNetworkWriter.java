package net.hypejet.jet.server.network.codec.aggregate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes an aggregate and elements of it.
 *
 * @param <A> a type of the aggregate
 * @since 1.0
 * @see NetworkWriter
 */
public abstract class AggregateNetworkWriter<A> implements NetworkWriter<A> {

    private final int maxLength;
    private final boolean encodeLength;

    /**
     * Constructs the {@linkplain AggregateNetworkWriter aggregate network writer}.
     *
     * @param maxLength a max length that an aggregate can have
     * @param encodeLength whether the length of aggregates should be encoded
     * @since 1.0
     */
    protected AggregateNetworkWriter(int maxLength, boolean encodeLength) {
        this.maxLength = maxLength;
        this.encodeLength = encodeLength;
    }

    @Override
    public final void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull A object) {
        int length = this.length(object);

        if (length > this.maxLength) {
            throw new IllegalArgumentException(String.format(
                    "The aggregate is long than allowed (%s > %s).",
                    length, this.maxLength
            ));
        }

        if (this.encodeLength)
            VarIntNetworkCodec.INSTANCE.write(buf, registryManager, length);
        this.encodeElements(object, buf, registryManager);
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
     * @param registryManager registry manager of server that the elements are being written for
     * @since 1.0
     */
    protected abstract void encodeElements(@NonNull A aggregate, @NonNull ByteBuf buf,
                                           @NonNull JetRegistryManager registryManager);
}