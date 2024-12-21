package net.hypejet.jet.server.util.acquisition;

import net.hypejet.concurrency.object.ObjectAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents {@linkplain ObjectAcquisition an object acquisition}, which maps an object of an already existing
 * {@linkplain ObjectAcquisition object acquisition}.
 *
 * @param <O> a type of object of the original acquisition
 * @param <M> a type that the object should be mapped to
 * @since 1.0
 * @see ObjectAcquisition
 */
public final class MappedObjectAcquisition<O, M> implements ObjectAcquisition<M> {

    private final ObjectAcquisition<O> originalAcquisition;
    private final Function<O, M> mapper;

    /**
     * Constructs the {@linkplain MappedObjectAcquisition mapped object acquisition}.
     *
     * @param originalAcquisition the original acquisition that the object should be mapped from
     * @param mapper a function that maps the object to another object
     * @since 1.0
     */
    public MappedObjectAcquisition(@NotNull ObjectAcquisition<O> originalAcquisition, @NotNull Function<O, M> mapper) {
        this.originalAcquisition = NullabilityUtil.requireNonNull(originalAcquisition, "original acquisition");
        this.mapper = NullabilityUtil.requireNonNull(mapper, "mapper");
    }

    @Override
    public @NotNull M get() {
        return this.mapper.apply(this.originalAcquisition.get());
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