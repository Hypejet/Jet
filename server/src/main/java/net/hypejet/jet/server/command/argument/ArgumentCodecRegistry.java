package net.hypejet.jet.server.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.server.command.argument.codecs.EmptyArgumentCodec;
import net.hypejet.jet.server.command.argument.codecs.NumberArgumentCodec;
import net.hypejet.jet.server.command.argument.codecs.StringArgumentCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain ArgumentCodec argument codecs}.
 *
 * @since 1.0
 * @author Codestech
 * @see ArgumentCodec
 */
public final class ArgumentCodecRegistry {

    private static final IntObjectMap<ArgumentCodec<?>> identifierToCodecMap = new IntObjectHashMap<>();
    private static final Map<Class<? extends ArgumentType<?>>, ArgumentCodec<?>> classToCodecMap = new HashMap<>();

    static {
        register(new EmptyArgumentCodec<>(0, BoolArgumentType.class, BoolArgumentType.bool()));

        register(new NumberArgumentCodec<>(1, FloatArgumentType.class, ByteBuf::readFloat, ByteBuf::writeFloat,
                FloatArgumentType::floatArg, FloatArgumentType::getMinimum, FloatArgumentType::getMaximum,
                Float.MIN_VALUE, Float.MAX_VALUE));
        register(new NumberArgumentCodec<>(2, DoubleArgumentType.class, ByteBuf::readDouble, ByteBuf::writeDouble,
                DoubleArgumentType::doubleArg, DoubleArgumentType::getMinimum, DoubleArgumentType::getMaximum,
                Double.MIN_VALUE, Double.MAX_VALUE));
        register(new NumberArgumentCodec<>(3, IntegerArgumentType.class, ByteBuf::readInt, ByteBuf::writeInt,
                IntegerArgumentType::integer, IntegerArgumentType::getMinimum, IntegerArgumentType::getMaximum,
                Integer.MIN_VALUE, Integer.MAX_VALUE));
        register(new NumberArgumentCodec<>(4, LongArgumentType.class, ByteBuf::readLong, ByteBuf::writeLong,
                LongArgumentType::longArg, LongArgumentType::getMinimum, LongArgumentType::getMaximum,
                Long.MIN_VALUE, Long.MAX_VALUE));

        register(new StringArgumentCodec());
    }

    /**
     * Gets an {@linkplain ArgumentCodec argument codec}, which reads and writes an {@linkplain ArgumentType argument
     * type} specified.
     *
     * @param argumentTypeClass a class of the argument type
     * @return the argument codec, {@code null} if there is no an argument codec eligible for the argument type
     * @since 1.0
     */
    public static @Nullable ArgumentCodec<?> codec(@NonNull Class<?> argumentTypeClass) {
        return classToCodecMap.get(argumentTypeClass);
    }

    /**
     * Gets an {@linkplain ArgumentCodec argument codec} with a parser identifier specified.
     *
     * @param identifier the parser identifier
     * @return the argument codec
     * @since 1.0
     */
    public static @Nullable ArgumentCodec<?> codec(int identifier) {
        return identifierToCodecMap.get(identifier);
    }

    /**
     * Registers an {@linkplain ArgumentCodec argument codec} into the registry.
     *
     * @param codec the argument codec
     * @since 1.0
     */
    private static void register(@NonNull ArgumentCodec<?> codec) {
        identifierToCodecMap.put(codec.getParserId(), codec);
        classToCodecMap.put(codec.getArgumentTypeClass(), codec);
    }
}