package net.hypejet.jet.server.util.acquisition;

import net.hypejet.concurrency.Acquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents {@linkplain BooleanAcquisition a boolean acquisition}, which provides a mapped value from
 * {@linkplain OA an already existing acquisition}.
 *
 * @param <OA> a type of the already existing acquisition
 * @since 1.0
 * @see BooleanAcquisition
 */
public final class BooleanMappedAcquisition<OA extends Acquisition> implements BooleanAcquisition {

    private final OA originalAcquisition;
    private final Predicate<OA> mapper;

    /**
     * Constructs the {@linkplain BooleanMappedAcquisition boolean mapped acquisition}.
     *
     * @param originalAcquisition the already existing acquisition
     * @param mapper a function, which maps the value
     * @since 1.0
     */
    public BooleanMappedAcquisition(@NonNull OA originalAcquisition, @NonNull Predicate<OA> mapper) {
        this.originalAcquisition = Objects.requireNonNull(originalAcquisition, "original acquisition");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    @Override
    public boolean get() {
        return this.mapper.test(this.originalAcquisition);
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
