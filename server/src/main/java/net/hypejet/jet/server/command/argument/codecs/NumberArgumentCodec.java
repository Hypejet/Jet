package net.hypejet.jet.server.command.argument.codecs;

import com.mojang.brigadier.arguments.ArgumentType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.command.argument.ArgumentCodec;
import net.hypejet.jet.server.util.bytes.ByteUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a {@linkplain ArgumentCodec argument codec}, which reads and writes a {@linkplain ArgumentType argument
 * type}, which accepts number arguments.
 *
 * @param <N> a type of the number
 * @param <A> the argument type
 * @since 1.0
 */
public final class NumberArgumentCodec<N extends Number, A extends ArgumentType<N>> extends ArgumentCodec<A> {

    private static final byte HAS_MIN_MASK = 0x01;
    private static final byte HAS_MAX_MASK = 0x02;

    private final Function<ByteBuf, N> readFunction;
    private final BiConsumer<ByteBuf, N> writeFunction;

    private final BiFunction<N, N, A> argumentSupplier;

    private final Function<A, N> minValueGetter;
    private final Function<A, N> maxValueGetter;

    private final N minValue;
    private final N maxValue;

    /**
     * Constructs the {@linkplain ArgumentCodec argument codec}.
     *
     * @param parserId an identifier of the parser
     * @param argumentTypeClass a class of the argument type
     * @param readFunction a function, which reads a number from a byte buf
     * @param writeFunction a function, which writes a number to a byte buf
     * @param argumentSupplier a function, which creates an argument with values specified
     * @param minValueGetter a function, which gets a minimum value of an argument
     * @param maxValueGetter a function, which gets a maximum value of an argument
     * @param minValue a minimum value of the number
     * @param maxValue a maximum value of the number
     * @since 1.0
     */
    public NumberArgumentCodec(int parserId, @NonNull Class<A> argumentTypeClass,
                               @NonNull Function<ByteBuf, N> readFunction,
                               @NonNull BiConsumer<ByteBuf, N> writeFunction,
                               @NonNull BiFunction<N, N, A> argumentSupplier,
                               @NonNull Function<A, N> minValueGetter, @NonNull Function<A, N> maxValueGetter,
                               @NonNull N minValue, @NonNull N maxValue) {
        super(parserId, argumentTypeClass);

        this.readFunction = readFunction;
        this.writeFunction = writeFunction;

        this.argumentSupplier = argumentSupplier;

        this.minValueGetter = minValueGetter;
        this.maxValueGetter = maxValueGetter;

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public @NonNull A read(@NonNull ByteBuf buf) {
        byte flags = buf.readByte();

        N min = this.minValue;
        N max = this.maxValue;

        if (ByteUtil.isBitMaskEnabled(flags, HAS_MIN_MASK))
            min = this.readFunction.apply(buf);
        if (ByteUtil.isBitMaskEnabled(flags, HAS_MAX_MASK))
            max = this.readFunction.apply(buf);

        return this.argumentSupplier.apply(min, max);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull A object) {
        byte flags = 0;

        N minValue = this.minValueGetter.apply(object);
        N maxValue = this.maxValueGetter.apply(object);

        boolean hasMin = !this.minValue.equals(minValue);
        boolean hasMax = !this.maxValue.equals(maxValue);

        if (hasMin) flags |= HAS_MIN_MASK;
        if (hasMax) flags |= HAS_MAX_MASK;

        buf.writeByte(flags);

        if (hasMin) this.writeFunction.accept(buf, minValue);
        if (hasMax) this.writeFunction.accept(buf, maxValue);
    }
}