package net.hypejet.jet.server.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.command.argument.writers.EmptyArgumentWriter;
import net.hypejet.jet.server.command.argument.writers.NumberArgumentWriter;
import net.hypejet.jet.server.command.argument.writers.StringArgumentWriter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain ArgumentWriter argument codecs}.
 *
 * @since 1.0
 * @see ArgumentWriter
 */
public final class ArgumentWriterRegistry {

    private static final Map<Class<? extends ArgumentType<?>>, ArgumentWriter<?>> WRITERS =
            new ArgumentWritersBuilder()
                    .add(new EmptyArgumentWriter<>(0, BoolArgumentType.class))
                    .add(new NumberArgumentWriter<>(1, FloatArgumentType.class, ByteBuf::writeFloat,
                            FloatArgumentType::getMinimum, FloatArgumentType::getMaximum,
                            Float.MIN_VALUE, Float.MAX_VALUE))
                    .add(new NumberArgumentWriter<>(2, DoubleArgumentType.class, ByteBuf::writeDouble,
                            DoubleArgumentType::getMinimum, DoubleArgumentType::getMaximum,
                            Double.MIN_VALUE, Double.MAX_VALUE))
                    .add(new NumberArgumentWriter<>(4, LongArgumentType.class, ByteBuf::writeLong,
                            LongArgumentType::getMinimum, LongArgumentType::getMaximum,
                            Long.MIN_VALUE, Long.MAX_VALUE))
                    .add(new NumberArgumentWriter<>(3, IntegerArgumentType.class, ByteBuf::writeInt,
                            IntegerArgumentType::getMinimum, IntegerArgumentType::getMaximum,
                            Integer.MIN_VALUE, Integer.MAX_VALUE))
                    .add(StringArgumentWriter.INSTANCE)
                    .build();

    /**
     * Gets {@linkplain ArgumentWriter an argument writer}, which writes {@linkplain ArgumentType an argument type}
     * specified.
     *
     * @param argumentTypeClass a class of the argument type
     * @return the argument writer, {@code null} if there is no argument writer eligible to write the argument type
     * @since 1.0
     */
    public static @Nullable ArgumentWriter<?> writer(@NonNull Class<?> argumentTypeClass) {
        return WRITERS.get(argumentTypeClass);
    }

    /**
     * Represents a builder of {@linkplain Map a map} of argument type classes to
     * {@linkplain ArgumentWriter argument writers} writing the argument types.
     *
     * @since 1.0
     * @see Map
     * @see ArgumentWriter
     */
    private static final class ArgumentWritersBuilder {

        private final Map<Class<? extends ArgumentType<?>>, ArgumentWriter<?>> writers = new HashMap<>();

        /**
         * Adds {@linkplain ArgumentWriter an argument writer} specified to the map.
         *
         * @param writer the argument writer
         * @return this builder
         * @since 1.0
         */
        private @NonNull ArgumentWritersBuilder add(@NonNull ArgumentWriter<?> writer) {
            this.writers.put(writer.argumentTypeClass(), writer);
            return this;
        }

        /**
         * Builds the map.
         *
         * @return the map
         * @since 1.0
         */
        private @NonNull Map<Class<? extends ArgumentType<?>>, ArgumentWriter<?>> build() {
            return Map.copyOf(this.writers);
        }
    }
}