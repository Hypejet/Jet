package net.hypejet.jet.server.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes properties of
 * an {@linkplain ArgumentType argument type}.
 *
 * @param <A> the argument type
 * @since 1.0
 * @author Codestech
 * @see ArgumentType
 * @see NetworkCodec
 */
public abstract class ArgumentCodec<A extends ArgumentType<?>> implements NetworkCodec<A> {

    private final int parserId;
    private final Class<A> argumentTypeClass;

    /**
     * Constructs the {@linkplain ArgumentCodec argument codec}.
     *
     * @param parserId an identifier of the parser
     * @param argumentTypeClass a class of the argument type
     * @since 1.0
     */
    public ArgumentCodec(int parserId, @NonNull Class<A> argumentTypeClass) {
        this.parserId = parserId;
        this.argumentTypeClass = argumentTypeClass;
    }

    /**
     * Gets an identifier of the parser of the argument type.
     *
     * @return the identifier
     * @since 1.0
     */
    public int getParserId() {
        return this.parserId;
    }

    /**
     * Gets a class of the argument type that this codec reads and writes.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<A> getArgumentTypeClass() {
        return this.argumentTypeClass;
    }
}