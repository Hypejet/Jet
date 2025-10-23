package net.hypejet.jet.server.network.codec.validation;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A wrapped {@linkplain NetworkWriter network writer} which runs a {@linkplain Predicate predicate} on an object
 * before writing and throws a {@linkplain RuntimeException runtime exception} if the predicate is not satisfied.
 *
 * @param writer the wrapped network writer
 * @param predicate the predicate that tests the written objects
 * @param exceptionProvider a function providing runtime exceptions for objects that do not satisfy the predicate
 * @param <V> the type of objects that this network writer writes
 * @since 1.0
 * @see Predicate
 * @see RuntimeException
 * @see NetworkWriter
 */
@NullMarked
public record ValidatedNetworkWriter<V>(NetworkWriter<V> writer, Predicate<V> predicate,
                                        Function<V, RuntimeException> exceptionProvider) implements NetworkWriter<V> {
    /**
     * Constructs the {@linkplain ValidatedNetworkWriter validated network writer}.
     *
     * @param writer the network writer that should be wrapped
     * @param predicate the predicate that should test written objects
     * @param exceptionProvider a function that should provide runtime exceptions
     *                          for objects that do not satisfy the predicate
     * @since 1.0
     */
    public ValidatedNetworkWriter {
        Objects.requireNonNull(writer, "writer");
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(exceptionProvider, "exception provider");
    }

    @Override
    public void write(ByteBuf buf, JetRegistryManager registryManager, V object) {
        if (!this.predicate.test(object))
            throw this.exceptionProvider.apply(object);
        this.writer.write(buf, registryManager, object);
    }
}