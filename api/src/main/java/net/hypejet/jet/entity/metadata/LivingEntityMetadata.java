package net.hypejet.jet.entity.metadata;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.particle.ParticleOptions;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * A metadata of a living {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface LivingEntityMetadata extends EntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the {@linkplain Entity entity} is currently using an item.
     *
     * @return {@code true} if the entity is currently using an item, {@code false} otherwise
     * @since 1.0
     */
    boolean usingItem();

    /**
     * Gets whether the {@linkplain Entity entity} is currently in auto-spin attack,
     * meaning that it is attacking using trident with riptide enchantment.
     *
     * @return {@code true} if the entity is currently in auto-spin attack, {@code false} otherwise
     * @since 1.0
     */
    boolean inAutoSpinAttack();

    /**
     * Gets the {@linkplain Entity.InteractionHand interaction hand}
     * where the {@linkplain Entity entity} holds the currently used item.
     *
     * @return the interaction hand containing currently used item
     * @since 1.0
     */
    Entity.InteractionHand usedItemHand();

    /**
     * Gets health of the {@linkplain Entity entity}.
     *
     * @return the health
     * @since 1.0
     */
    float health();

    /**
     * Gets a {@linkplain List list} of {@linkplain ParticleOptions particle effects}
     * that should be played around the {@linkplain Entity entity}.
     *
     * @return the particle effect list
     * @since 1.0
     */
    List<ParticleOptions> particleEffects();

    /**
     * Gets whether the number of {@linkplain ParticleOptions particle effects}
     * played around the {@linkplain Entity entity} should be decreased.
     *
     * @return {@code true} if the number of particle effects should be decreased, {@code false} otherwise
     * @since 1.0
     */
    boolean reduceParticleEffects();

    /**
     * Gets a number of arrows currently attached to the {@linkplain Entity entity}.
     *
     * @return the attached arrow number
     * @since 1.0
     */
    int arrowCount();

    /**
     * Gets a number of stingers currently attached to the {@linkplain Entity entity}.
     *
     * @return the attached stinger number
     * @since 1.0
     */
    int stingerCount();

    /**
     * Gets a {@linkplain BlockPosition block position} of the bed
     * that the {@linkplain Entity entity} is currently sleeping in.
     *
     * @return the block position of bed that the entity is currently sleeping in,
     *         {@code null} if the entity is not currently sleeping
     * @since 1.0
     */
    @Nullable BlockPosition sleepingPosition();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain LivingEntityMetadata living entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see LivingEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends EntityMetadata.Update<U> {
        /**
         * Sets whether the {@linkplain Entity entity} should be using an item.
         *
         * @param value {@code true} if the entity should be using an item
         * @return this update builder
         * @since 1.0
         */
        U usingItem(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should be in an auto-spin attack,
         * meaning that it is attacking using trident with riptide enchantment.
         *
         * @param value {@code true} if the entity should be in an auto-spin attack, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U inAutoSpinAttack(boolean value);

        /**
         * Sets the {@linkplain Entity.InteractionHand interaction hand}
         * where the {@linkplain Entity entity} holds currently using item.
         *
         * @param value the interaction hand containing currently used item
         * @return this update builder
         * @since 1.0
         */
        U usedItemHand(Entity.InteractionHand value);

        /**
         * Sets health that the {@linkplain Entity entity} should have.
         *
         * @param value the health that the entity should have
         * @return this update builder
         * @since 1.0
         */
        U health(float value);

        /**
         * Sets a {@linkplain List list} of {@linkplain ParticleOptions particle effects}
         * that should be played around the {@linkplain Entity entity}.
         *
         * @param value the particle effect list
         * @return this update builder
         * @since 1.0
         */
        U particleEffects(List<ParticleOptions> value);

        /**
         * Sets whether the number of {@linkplain ParticleOptions particle effects}
         * played around the {@linkplain Entity entity} should be decreased.
         *
         * @param value {@code true} if the number of particle effects should be decreased, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U reducedParticleEffects(boolean value);

        /**
         * Sets a number of arrows that should be attached to the {@linkplain Entity entity}.
         *
         * @param value the attached arrow number
         * @return this update builder
         * @since 1.0
         */
        U arrowCount(int value);

        /**
         * Sets a number of stingers that should be attached to the {@linkplain Entity entity}.
         *
         * @param value the attached stinger number
         * @return this update builder
         * @since 1.0
         */
        U stingerCount(int value);

        /**
         * a {@linkplain BlockPosition block position} of the bed
         * that the {@linkplain Entity entity} should be sleeping in.
         *
         * @param value the block position of the bed that the entity should be sleeping in,
         *              {@code null} if the entity should not be sleeping
         * @return this update builder
         * @since 1.0
         */
        U sleepingPosition(@Nullable BlockPosition value);
    }
}