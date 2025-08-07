package net.hypejet.jet.server.util.acquisition;

import net.hypejet.concurrency.Acquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents {@linkplain NullableObjectAcquisition a nullable object acquisition}, which provides a mapped value from
 * {@linkplain OA an already existing acquisition}.
 *
 * @param <OA> a type of the already existing acquisition
 * @param <M> a type that the object should be mapped to
 * @since 1.0
 * @see NullableObjectAcquisition
 */
public final class NullableObjectMappedAcquisition<OA extends Acquisition, M> implements NullableObjectAcquisition<M> {

    private final OA originalAcquisition;
    private final Function<OA, M> mapper;

    /**
     * Constructs the {@linkplain NullableObjectMappedAcquisition nullable object mapped acquisition}.
     *
     * @param originalAcquisition the already existing acquisition
     * @param mapper a function, which maps the value
     * @since 1.0
     */
    public NullableObjectMappedAcquisition(@NotNull OA originalAcquisition, @NotNull Function<OA, M> mapper) {
        this.originalAcquisition = Objects.requireNonNull(originalAcquisition, "original acquisition");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    @Override
    public @Nullable M get() {
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