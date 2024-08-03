package net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.IntFunction;

/**
 * Represents an {@linkplain AggregateNetworkCodec aggregate network codec}, which reads and writes a byte array.
 *
 * @since 1.0
 * @author Codestech
 * @see AggregateNetworkCodec
 */
public final class ObjectArrayNetworkCodec<T> extends AggregateNetworkCodec<T[]> {

    private final NetworkCodec<T> elementCodec;
    private final IntFunction<T[]> arraySupplier;

    private ObjectArrayNetworkCodec(int maxLength, @NonNull NetworkCodec<T> elementCodec,
                                    @NonNull IntFunction<T[]> arraySupplier) {
        super(maxLength);
        this.elementCodec = elementCodec;
        this.arraySupplier = arraySupplier;
    }

    @Override
    protected int length(T @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(T @NonNull [] aggregate, @NonNull ByteBuf buf) {
        for (T element : aggregate) {
            this.elementCodec.write(buf, element);
        }
    }

    @Override
    protected T @NonNull [] decodeElements(int length, @NonNull ByteBuf buf) {
        T[] elements = this.arraySupplier.apply(length);
        for (int index = 0; index < length; index++)
            elements[index] = this.elementCodec.read(buf);
        return elements;
    }

    /**
     * Creates a new {@linkplain ObjectArrayNetworkCodec object array network codec}, with maximum size
     * of {@link Integer#MAX_VALUE}.
     *
     * @param elementCodec a network codec, which reads and writes elements of the array
     * @param arraySupplier a function, which creates an array with a length specified
     * @return the codec
     * @since 1.0
     */
    public static <T> @NonNull ObjectArrayNetworkCodec<T> create(@NonNull NetworkCodec<T> elementCodec,
                                                                 @NonNull IntFunction<T[]> arraySupplier) {
        return new ObjectArrayNetworkCodec<>(Integer.MAX_VALUE, elementCodec, arraySupplier);
    }

    /**
     * Creates a new {@linkplain ObjectArrayNetworkCodec object array network codec}.
     *
     * @param maxLength a max length of an array that the codec handles
     * @param elementCodec a network codec, which reads and writes elements of the array
     * @param arraySupplier a function, which creates an array with a length specified
     * @return the codec
     * @since 1.0
     */
    public static <T> @NonNull ObjectArrayNetworkCodec<T> create(int maxLength, @NonNull NetworkCodec<T> elementCodec,
                                                                 @NonNull IntFunction<T[]> arraySupplier) {
        return new ObjectArrayNetworkCodec<>(maxLength, elementCodec, arraySupplier);
    }
}