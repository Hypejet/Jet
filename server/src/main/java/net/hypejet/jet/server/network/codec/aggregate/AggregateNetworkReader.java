package net.hypejet.jet.server.network.codec.aggregate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads an aggregate and elements of it.
 *
 * @param <A> a type of the aggregate
 * @since 1.0
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
    public @NonNull A read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        int length = VarIntNetworkCodec.INSTANCE.read(buf, registryManager);
        if (length > this.maxLength) {
            throw new IllegalArgumentException(String.format(
                    "The aggregate is long than allowed (%s > %s).",
                    length, this.maxLength
            ));
        }
        return this.decodeElements(length, buf, registryManager);
    }

    /**
     * Decodes elements and creates an aggregate with those elements.
     *
     * @param length an amount of elements that the aggregate should have
     * @param buf a byte buf to decode elements from
     * @param registryManager registry manager of server that the elements are being decoded for
     * @return the aggregate created
     * @since 1.0
     */
    protected abstract @NonNull A decodeElements(int length, @NonNull ByteBuf buf,
                                                 @NonNull JetRegistryManager registryManager);
}