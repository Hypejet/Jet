package net.hypejet.jet.server.network.codec.aggregate.array.object;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.IntFunction;

/**
 * Represents {@linkplain AggregateNetworkReader an aggregate network reader}, which reads an array of objects
 * with a type specified.
 *
 * @param <E> a type of the element
 * @since 1.0
 * @see AggregateNetworkReader
 */
public final class ObjectArrayNetworkReader<E> extends AggregateNetworkReader<E[]> {

    private final IntFunction<E[]> arraySupplier;
    private final NetworkReader<E> elementReader;

    /**
     * Constructs the {@linkplain ObjectArrayNetworkReader object array network reader} with a support for lengths
     * up to {@link Integer#MAX_VALUE}.
     *
     * @param arraySupplier a supplier of arrays of the objects with the type specified
     * @param elementReader a network reader, which reads elements of the array
     * @since 1.0
     */
    public ObjectArrayNetworkReader(@NonNull IntFunction<E[]> arraySupplier, @NonNull NetworkReader<E> elementReader) {
        super(Integer.MAX_VALUE);
        this.arraySupplier = NullabilityUtil.requireNonNull(arraySupplier, "array supplier");
        this.elementReader = NullabilityUtil.requireNonNull(elementReader, "element reader");
    }

    /**
     * Constructs the {@linkplain ObjectArrayNetworkReader object array network reader}.
     *
     * @param maxLength a max length that an object array can have
     * @param arraySupplier a supplier of arrays of the objects with the type specified
     * @param elementReader a network reader, which reads elements of the array
     * @since 1.0
     */
    public ObjectArrayNetworkReader(int maxLength, @NonNull IntFunction<E[]> arraySupplier,
                                    @NonNull NetworkReader<E> elementReader) {
        super(maxLength);
        this.arraySupplier = NullabilityUtil.requireNonNull(arraySupplier, "array supplier");
        this.elementReader = NullabilityUtil.requireNonNull(elementReader, "element reader");
    }

    @Override
    protected E @NonNull [] decodeElements(int length, @NonNull ByteBuf buf) {
        E[] array = this.arraySupplier.apply(length);
        for (int index = 0; index < length; index++)
            array[index] = this.elementReader.read(buf);
        return array;
    }
}