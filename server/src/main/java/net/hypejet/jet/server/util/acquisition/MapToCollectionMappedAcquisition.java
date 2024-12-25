package net.hypejet.jet.server.util.acquisition;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents {@linkplain CollectionAcquisition a collection acquisition}, which maps a map of an already
 * existing {@linkplain MapAcquisition map acquisition}.
 *
 * @param <MK> a type of key of guarded map of the map acquisition
 * @param <MV> a type of value of guarded map of the map acquisition
 * @param <M> a type of guarded map of the map acquisition
 * @param <CV> a type of value of guarded collection that the map should be mapped to
 * @param <C> a type of guarded collection that the map should be mapped to
 * @since 1.0
 * @see MapAcquisition
 * @see CollectionAcquisition
 */
public final class MapToCollectionMappedAcquisition<MK, MV, M extends Map<MK, MV>, CV, C extends Collection<CV>>
        implements CollectionAcquisition<CV, C> {

    private final MapAcquisition<MK, MV, M> originalAcquisition;
    private final Function<M, C> mapper;

    /**
     * Constructs the {@linkplain MapToCollectionMappedAcquisition map-to-collection mapped acquisition}.
     *
     * @param originalAcquisition an original acquisition that the map should be mapped from
     * @param mapper a function that maps the map to a collection
     * @since 1.0
     */
    public MapToCollectionMappedAcquisition(@NonNull MapAcquisition<MK, MV, M> originalAcquisition,
                                            @NonNull Function<M, C> mapper) {
        this.originalAcquisition = NullabilityUtil.requireNonNull(originalAcquisition, "original acquisition");
        this.mapper = NullabilityUtil.requireNonNull(mapper, "mapper");
    }

    @Override
    public @NotNull C collection() {
        return this.mapper.apply(this.originalAcquisition.map());
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