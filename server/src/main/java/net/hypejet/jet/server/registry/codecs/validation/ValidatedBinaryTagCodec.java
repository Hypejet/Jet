package net.hypejet.jet.server.registry.codecs.validation;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A wrapped {@linkplain BinaryTagCodec binary-tag codec} which runs a {@linkplain Predicate predicate} on an object
 * before reading or writing and throws a {@linkplain RuntimeException runtime exception} if the predicate
 * is not satisfied.
 *
 * @param codec the wrapped binary-tag codec
 * @param predicate the predicate that tests the objects whose serialization is handled
 * @param exceptionProvider a function providing runtime exceptions for objects that do not satisfy the predicate
 * @param <V> the type of objects whose serialization is handled by this binary-tag codec
 * @since 1.0
 * @see Predicate
 * @see RuntimeException
 * @see BinaryTagCodec
 */
@NullMarked
public record ValidatedBinaryTagCodec<V>(
        BinaryTagCodec<V> codec, Predicate<V> predicate,
        Function<V, RuntimeException> exceptionProvider
) implements BinaryTagCodec<V> {
    /**
     * Constructs the {@linkplain ValidatedBinaryTagCodec validated binary-tag codec}.
     *
     * @param codec the binary-tag codec that should be wrapped
     * @param predicate the predicate that should test the objects whose serialization is
     *                  going to be handled by the validated binary-tag codec in construction
     * @param exceptionProvider a function that should provide runtime exceptions
     *                          for objects that do not satisfy the predicate
     * @since 1.0
     */
    public ValidatedBinaryTagCodec {
        Objects.requireNonNull(codec, "codec");
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(exceptionProvider, "exception provider");
    }

    @Override
    public V decode(BinaryTag binaryTag) {
        return this.validateValue(this.codec.decode(binaryTag));
    }

    @Override
    public BinaryTag encode(V value) {
        return this.codec.encode(this.validateValue(value));
    }

    private V validateValue(V value) {
        if (this.predicate.test(value)) return value;
        throw this.exceptionProvider.apply(value);
    }
}