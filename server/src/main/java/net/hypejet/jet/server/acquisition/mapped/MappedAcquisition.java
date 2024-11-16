package net.hypejet.jet.server.acquisition.mapped;

import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * Represents {@linkplain Acquisition an acquisition}, which returns a mapped value of an original acquisition,
 * which was specified during creation of the mapped acquisition.
 *
 * @param originalAcquisition the original acquisition
 * @param mapper a function that maps the value
 * @param <O> a type value of the original acquisition
 * @param <M> a type of the mapped value
 * @since 1.0
 */
public record MappedAcquisition<O, M>(@NonNull Acquisition<O> originalAcquisition,
                                      @NonNull Function<O, M> mapper) implements Acquisition<M> {
    /**
     * Constructs the {@linkplain MappedAcquisition mapped acquisition}.
     *
     * @param originalAcquisition the original acquisition
     * @param mapper              a function that maps the value
     * @since 1.0
     */
    public MappedAcquisition {
        NullabilityUtil.requireNonNull(originalAcquisition, "original acquisition");
        NullabilityUtil.requireNonNull(mapper, "mapper");
    }

    @Override
    public @NonNull M get() {
        M mappedValue = this.mapper.apply(this.originalAcquisition.get());
        NullabilityUtil.requireNonNull(mappedValue, "mapped value");
        return mappedValue;
    }

    @Override
    public void close() {
        this.originalAcquisition.close();
    }

    @Override
    public boolean isUnlocked() {
        return this.originalAcquisition.isUnlocked();
    }
}