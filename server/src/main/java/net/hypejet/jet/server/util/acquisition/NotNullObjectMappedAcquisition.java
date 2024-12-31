package net.hypejet.jet.server.util.acquisition;

import net.hypejet.concurrency.Acquisition;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents {@linkplain NotNullObjectAcquisition a not-null object acquisition}, which provides a mapped value from
 * {@linkplain OA an already existing acquisition}.
 *
 * @param <OA> a type of the already existing acquisition
 * @param <M> a type that the object should be mapped to
 * @since 1.0
 * @see NotNullObjectAcquisition
 */
public final class NotNullObjectMappedAcquisition<OA extends Acquisition, M> implements NotNullObjectAcquisition<M> {

    private final OA originalAcquisition;
    private final Function<OA, M> mapper;

    /**
     * Constructs the {@linkplain NotNullObjectMappedAcquisition not-null object mapped acquisition}.
     *
     * @param originalAcquisition the already existing acquisition
     * @param mapper a function, which maps the value
     * @since 1.0
     */
    public NotNullObjectMappedAcquisition(@NotNull OA originalAcquisition, @NotNull Function<OA, M> mapper) {
        this.originalAcquisition = NullabilityUtil.requireNonNull(originalAcquisition, "original acquisition");
        this.mapper = NullabilityUtil.requireNonNull(mapper, "mapper");
    }

    @Override
    public @NotNull M get() {
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