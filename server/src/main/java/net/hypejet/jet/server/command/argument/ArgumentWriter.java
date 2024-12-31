package net.hypejet.jet.server.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes properties of
 * {@linkplain ArgumentType an argument type}.
 *
 * @param <A> a type of the argument type
 * @since 1.0
 * @see ArgumentType
 * @see NetworkWriter
 */
public abstract class ArgumentWriter<A extends ArgumentType<?>> implements NetworkWriter<A> {

    private final int parserId;
    private final Class<A> argumentTypeClass;

    /**
     * Constructs the {@linkplain ArgumentWriter argument writer}.
     *
     * @param parserId an identifier of the parser
     * @param argumentTypeClass a class of the argument type
     * @since 1.0
     */
    public ArgumentWriter(int parserId, @NonNull Class<A> argumentTypeClass) {
        this.parserId = parserId;
        this.argumentTypeClass = argumentTypeClass;
    }

    /**
     * Gets an identifier of parser of the argument type.
     *
     * @return the identifier
     * @since 1.0
     */
    public int parserId() {
        return this.parserId;
    }

    /**
     * Gets a class of the argument type that this writer reads and writes.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<A> argumentTypeClass() {
        return this.argumentTypeClass;
    }
}