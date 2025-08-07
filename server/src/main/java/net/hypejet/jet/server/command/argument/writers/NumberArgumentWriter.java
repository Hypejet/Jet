package net.hypejet.jet.server.command.argument.writers;

import com.mojang.brigadier.arguments.ArgumentType;
import io.netty.buffer.ByteBuf;
import java.util.Objects;
import net.hypejet.jet.server.command.argument.ArgumentWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents {@linkplain ArgumentWriter an argument writer}, which reads and writes
 * {@linkplain ArgumentType an argument type}, which accepts number arguments.
 *
 * @param <N> a type of the number
 * @param <A> the argument type
 * @since 1.0
 * @see ArgumentWriter
 */
public final class NumberArgumentWriter<N extends Number, A extends ArgumentType<N>> extends ArgumentWriter<A> {

    private static final byte HAS_MIN_MASK = 0x01;
    private static final byte HAS_MAX_MASK = 0x02;

    private final BiConsumer<ByteBuf, N> writeFunction;

    private final Function<A, N> minimumValueGetter;
    private final Function<A, N> maximumValueGetter;

    private final N minValue;
    private final N maxValue;

    /**
     * Constructs the {@linkplain NumberArgumentWriter number argument writer}.
     *
     * @param parserId an identifier of the parser
     * @param argumentTypeClass a class of the argument type
     * @param writeFunction a function, which writes a number to a byte buf
     * @param minimumValueGetter a function, which gets minimum number value of an argument
     * @param maximumValueGetter a function, which gets maximum number value of an argument
     * @param minimumValue a minimum value of the number
     * @param maximumValue a maximum value of the number
     * @since 1.0
     */
    public NumberArgumentWriter(int parserId, @NonNull Class<A> argumentTypeClass,
                                @NonNull BiConsumer<ByteBuf, N> writeFunction,
                                @NonNull Function<A, N> minimumValueGetter, @NonNull Function<A, N> maximumValueGetter,
                                @NonNull N minimumValue, @NonNull N maximumValue) {
        super(parserId, argumentTypeClass);
        this.writeFunction = Objects.requireNonNull(writeFunction, "write function");

        this.minimumValueGetter = Objects.requireNonNull(minimumValueGetter, "minimum value getter");
        this.maximumValueGetter = Objects.requireNonNull(maximumValueGetter, "maximum value getter");

        this.minValue = Objects.requireNonNull(minimumValue, "minimum value");
        this.maxValue = Objects.requireNonNull(maximumValue, "maximum value");
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull A object) {
        byte flags = 0;

        N minValue = this.minimumValueGetter.apply(object);
        N maxValue = this.maximumValueGetter.apply(object);

        boolean hasMin = !this.minValue.equals(minValue);
        boolean hasMax = !this.maxValue.equals(maxValue);

        if (hasMin) flags |= HAS_MIN_MASK;
        if (hasMax) flags |= HAS_MAX_MASK;

        buf.writeByte(flags);

        if (hasMin) this.writeFunction.accept(buf, minValue);
        if (hasMax) this.writeFunction.accept(buf, maxValue);
    }
}