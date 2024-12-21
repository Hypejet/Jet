package net.hypejet.jet.server.util.acquisition;

import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.concurrency.object.ObjectAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

/**
 * Represents {@linkplain ObjectAcquisition an object acquisition}, which maps a map of an already existing
 * {@linkplain MapAcquisition map acquisition}.
 *
 * @param <K> a type of key of map of the map acquisition
 * @param <V> a type of value of map of the map acquisition
 * @param <M> a type of map of the map acquisition
 * @param <O> a type that map of the map acquisition map should be mapped to
 * @since 1.0
 * @see MapAcquisition
 * @see ObjectAcquisition
 */
public final class MapToObjectMappedAcquisition<K, V, M extends Map<K, V>, O> implements ObjectAcquisition<O> {

    private final MapAcquisition<K, V, M> originalAcquisition;
    private final Function<M, O> mapper;

    /**
     * Constructs the {@linkplain MapToObjectMappedAcquisition map-to-object mapped acquisition}.
     *
     * @param originalAcquisition the map acquisition that the map should be mapped from
     * @param mapper a function that maps the map to an object
     * @since 1.0
     */
    public MapToObjectMappedAcquisition(@NonNull MapAcquisition<K, V, M> originalAcquisition,
                                        @NonNull Function<M, O> mapper) {
        this.originalAcquisition = NullabilityUtil.requireNonNull(originalAcquisition, "original acquisition");
        this.mapper = NullabilityUtil.requireNonNull(mapper, "mapper");
    }

    @Override
    public @NotNull O get() {
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
}