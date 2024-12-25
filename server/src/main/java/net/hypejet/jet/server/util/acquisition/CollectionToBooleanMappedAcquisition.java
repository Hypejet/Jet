package net.hypejet.jet.server.util.acquisition;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Represents {@linkplain BooleanAcquisition a boolean acquisition}, which maps a collection from an already existing
 * {@linkplain CollectionAcquisition collection acquisition}.
 *
 * @param <V> a type of value of collection of the collection acquisition
 * @param <C> a type of collection of the collection acquisition
 * @since 1.0
 * @see CollectionAcquisition
 * @see BooleanAcquisition
 */
public final class CollectionToBooleanMappedAcquisition<V, C extends Collection<V>> implements BooleanAcquisition {

    private final CollectionAcquisition<V, C> originalAcquisition;
    private final Predicate<C> mapper;

    /**
     * Constructs the {@linkplain CollectionToBooleanMappedAcquisition collection-to-boolean mapped acquisition}.
     *
     * @param originalAcquisition the collection acquisition that the collection should be mapped from
     * @param mapper a function that maps the collection to a boolean
     */
    public CollectionToBooleanMappedAcquisition(@NonNull CollectionAcquisition<V, C> originalAcquisition,
                                                @NonNull Predicate<C> mapper) {
        this.originalAcquisition = NullabilityUtil.requireNonNull(originalAcquisition, "original acquisition");
        this.mapper = NullabilityUtil.requireNonNull(mapper, "mapper");
    }

    @Override
    public boolean get() {
        return this.mapper.test(this.originalAcquisition.collection());
    }

    @Override
    public boolean isUnlocked() {
        return this.originalAcquisition.isUnlocked();
    }

    @Override
    public void close() {
        this.originalAcquisition.close();
    }

    @Override
    public void ensurePermittedAndLocked() {
        this.originalAcquisition.ensurePermittedAndLocked();
    }

    @Override
    public @NotNull AcquisitionType acquisitionType() {
        return this.originalAcquisition.acquisitionType();
    }
}