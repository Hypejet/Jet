package net.hypejet.jet.server.command.argument.writers;

import com.mojang.brigadier.arguments.ArgumentType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.command.argument.ArgumentWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ArgumentWriter an argument writer}, which reads and writes
 * {@linkplain ArgumentType an argument type}, which does not contain any properties.
 *
 * @param <A> the argument type
 * @since 1.0
 * @see ArgumentWriter
 */
public final class EmptyArgumentWriter<A extends ArgumentType<?>> extends ArgumentWriter<A> {
    /**
     * Constructs the {@linkplain EmptyArgumentWriter empty argument writer},
     *
     * @param parserId an identifier of the parser of the argument type
     * @param argumentTypeClass a class of the argument type that the writer should read and write
     * @since 1.0
     */
    public EmptyArgumentWriter(int parserId, @NonNull Class<A> argumentTypeClass) {
        super(parserId, argumentTypeClass);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull A object) {
        // NOOP
    }
}