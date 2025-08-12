package net.hypejet.jet.server.entity.acquisition.world;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents {@linkplain NotNullObjectAcquisition a not-null object acquisition} of {@linkplain World a world}
 * of {@linkplain net.hypejet.jet.entity.Entity an entity}.
 *
 * @param <A> a type of world acquisition that this acquisition wraps
 * @since 1.0
 * @see NotNullObjectAcquisition
 * @see World
 * @see net.hypejet.jet.entity.Entity
 */
public class EntityWorldAcquisition<A extends NotNullObjectAcquisition<JetWorld>>
        implements NotNullObjectAcquisition<World> {

    protected final A acquisition;

    /**
     * Constructs the {@linkplain EntityWorldAcquisition entity world acquisition}.
     *
     * @param acquisition a world acquisition that the entity world acquisition should wrap
     * @since 1.0
     */
    public EntityWorldAcquisition(@NonNull A acquisition) {
        this.acquisition = Objects.requireNonNull(acquisition, "acquisition");
    }

    @Override
    public final @NotNull JetWorld get() {
        return this.acquisition.get();
    }

    @Override
    public final boolean isUnlocked() {
        return this.acquisition.isUnlocked();
    }

    @Override
    public final void close() {
        this.acquisition.close();
    }

    @Override
    public final void ensurePermittedAndLocked() {
        this.acquisition.ensurePermittedAndLocked();
    }

    @Override
    public final @NotNull AcquisitionType acquisitionType() {
        return this.acquisition.acquisitionType();
    }
}