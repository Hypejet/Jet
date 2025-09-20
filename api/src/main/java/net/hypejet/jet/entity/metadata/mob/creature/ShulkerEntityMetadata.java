package net.hypejet.jet.entity.metadata.mob.creature;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;
import net.hypejet.jet.world.direction.Direction;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * A metadata of a shulker {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface ShulkerEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets the attach face of the shulker {@linkplain Entity entity}.
     *
     * @return the {@linkplain Direction direction} the shulker is attached to
     * @since 1.0
     */
    Direction attachFace();

    /**
     * Gets the shield height of the shulker {@linkplain Entity entity}.
     *
     * @return shield height
     * @since 1.0
     */
    int shieldHeight();

    /**
     * Gets the color of the shulker {@linkplain Entity entity}.
     *
     * @return the {@linkplain NamedTextColor color} of the shulker
     * @since 1.0
     */
    NamedTextColor color();

    /**
     * An {@linkplain MobEntityMetadata.Update entity metadata update} for shulker.
     *
     * @param <U> the type of this update
     * @since 1.0
     * @see MobEntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets the attach face of the shulker.
         *
         * @param value the {@linkplain Direction direction} to attach to
         * @return this update builder
         * @since 1.0
         */
        U attachFace(Direction value);

        /**
         * Sets the shield height of the shulker.
         *
         * @param value shield height
         * @return this update builder
         * @since 1.0
         */
        U shieldHeight(int value);

        /**
         * Sets the color of the shulker.
         *
         * @param value color
         * @return this update builder
         * @since 1.0
         */
        U color(NamedTextColor value);
    }
}
