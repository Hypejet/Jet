package net.hypejet.jet.server.command.argument.codecs;

import com.mojang.brigadier.arguments.ArgumentType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.command.argument.ArgumentCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ArgumentCodec argument codec}, which reads and writes an {@linkplain ArgumentType argument
 * type}, which does not contain any properties.
 *
 * @param <A> the argument type
 * @since 1.0
 * @author Codestech
 * @see ArgumentCodec
 */
public final class EmptyArgumentCodec<A extends ArgumentType<?>> extends ArgumentCodec<A> {

    private final A argumentTypeInstance;

    /**
     * Constructs the {@linkplain EmptyArgumentCodec empty argument codec},
     *
     * @param parserId an identifier of the parser of the argument type
     * @param argumentTypeClass a class of the argument type that the codec should read and write
     * @param argumentTypeInstance an instance of the argument type that should be returned when the properties
     *                             are being read
     * @since 1.0
     */
    public EmptyArgumentCodec(int parserId, @NonNull Class<A> argumentTypeClass, @NonNull A argumentTypeInstance) {
        super(parserId, argumentTypeClass);
        this.argumentTypeInstance = argumentTypeInstance;
    }

    @Override
    public @NonNull A read(@NonNull ByteBuf buf) {
        return this.argumentTypeInstance;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull A object) {
        // NOOP
    }
}