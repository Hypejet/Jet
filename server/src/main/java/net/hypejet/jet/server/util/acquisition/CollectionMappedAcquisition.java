package net.hypejet.jet.server.util.acquisition;

import net.hypejet.concurrency.Acquisition;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

/**
 * Represents {@linkplain CollectionAcquisition a collection acquisition}, which provides a mapped value from
 * {@linkplain OA an already existing acquisition}.
 *
 * @param <OA> a type of the already existing acquisition
 * @param <CE> a type of elements of collection of the collection acquisition
 * @param <C> a type of collection of the collection acquisition
 * @since 1.0
 * @see CollectionAcquisition
 */
public final class CollectionMappedAcquisition<OA extends Acquisition, CE, C extends Collection<CE>>
        implements CollectionAcquisition<CE, C> {

    private final OA originalAcquisition;
    private final Function<OA, C> mapper;

    /**
     * Constructs the {@linkplain CollectionMappedAcquisition object-to-collection mapped acquisition}.
     *
     * @param originalAcquisition an original acquisition that the object should be mapped from
     * @param mapper a function that maps the object to a collection
     * @since 1.0
     */
    public CollectionMappedAcquisition(@NonNull OA originalAcquisition, @NonNull Function<OA, C> mapper) {
        this.originalAcquisition = NullabilityUtil.requireNonNull(originalAcquisition, "original acquisition");
        this.mapper = NullabilityUtil.requireNonNull(mapper, "mapper");
    }

    @Override
    public @NotNull C collection() {
        return this.mapper.apply(this.originalAcquisition);
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