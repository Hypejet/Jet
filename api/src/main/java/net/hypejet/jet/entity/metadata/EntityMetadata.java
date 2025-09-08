package net.hypejet.jet.entity.metadata;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A metadata of an {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface EntityMetadata {
    /**
     * Gets whether the {@linkplain Entity entity} is set on fire.
     *
     * @return {@code true} if the entity is set on fire, {@code false} otherwise
     * @since 1.0
     */
    boolean onFire();

    /**
     * Gets whether name tag of the {@linkplain Entity entity} is hidden due to sneaking.
     *
     * @return {@code true} if the name tag is hidden, {@code false} otherwise
     * @since 1.0
     */
    boolean sneaking();

    /**
     * Gets whether the {@linkplain Entity entity} plays sprinting particles.
     *
     * @return {@code true} if the entity plays sprinting particles, {@code false} otherwise
     * @since 1.0
     */
    boolean sprinting();

    /**
     * Gets whether the {@linkplain Entity entity} is swimming.
     *
     * @return {@code true} if the entity is swimming, {@code false} otherwise
     * @since 1.0
     */
    boolean swimming();

    /**
     * Gets whether the {@linkplain Entity entity} is invisible.
     *
     * @return {@code true} if the entity is invisible, {@code false} otherwise
     * @since 1.0
     */
    boolean invisible();

    /**
     * Gets whether the {@linkplain Entity entity} has the glowing effect.
     *
     * @return {@code true} if the entity has the glowing effect, {@code false} otherwise
     * @since 1.0
     */
    boolean glowing();

    /**
     * Gets whether the {@linkplain Entity entity} is gliding.
     *
     * @return {@code true} if the entity is gliding, {@code false} otherwise
     * @since 1.0
     */
    boolean gliding();

    /**
     * Gets the remaining air supply for the {@linkplain Entity entity}.
     *
     * @return the remaining air supply
     * @since 1.0
     */
    int airSupply();

    /**
     * Gets whether custom name of the {@linkplain Entity entity} is visible.
     *
     * @return {@code true} if the custom name is visible, {@code false} otherwise
     * @since 1.0
     */
    boolean customNameVisible();

    /**
     * Gets custom name of the {@linkplain Entity entity}.
     *
     * @return the custom name, {@code null} if the entity does not have a custom name
     * @since 1.0
     */
    @Nullable Component customName();

    /**
     * Gets whether the {@linkplain Entity entity} is silent, meaning that it does not play any sounds.
     *
     * @return {@code true} if the entity is silent, {@code false} otherwise
     * @since 1.0
     */
    boolean silent();

    /**
     * Gets whether the {@linkplain Entity entity} has disabled gravity.
     *
     * @return {@code true} if the entity has disabled gravity, {@code false} otherwise
     * @since 1.0
     */
    boolean hasNoGravity();

    /**
     * Gets {@linkplain Pose pose} of the {@linkplain Entity entity}.
     *
     * @return the pose
     * @since 1.0
     */
    Pose pose();

    /**
     * Gets the number of ticks for which the {@linkplain Entity entity} is frozen.
     *
     * @return the number of ticks for which the entity is frozen
     * @since 1.0
     */
    int ticksFrozen();

    /**
     * Creates a {@linkplain Update builder of update} that should be performed
     * on this {@linkplain EntityMetadata entity metadata}.
     *
     * <p>The created update builder is not tracked by the server, meaning that the update can be safely aborted
     * by {@linkplain Plugin plugins} by not {@linkplain Update#performUpdate() performing the update}.</p>
     *
     * @return the created update builder
     * @since 1.0
     * @see Update
     */
    Update createUpdateBuilder();

    /**
     * A builder of an update of an {@linkplain EntityMetadata entity metadata}.
     *
     * <p>Each method of this class is not thread-safe and throws an exception
     * if it is not being executed in the thread running the game logic loop.</p>
     *
     * @since 1.0
     * @see EntityMetadata
     */
    interface Update {
        /**
         * Sets whether the {@linkplain Entity entity} should be set on fire.
         *
         * @param value {@code true} if the entity should be set on fire, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update onFire(boolean value);

        /**
         * Sets whether name tag of the {@linkplain Entity entity} should be hidden due to sneaking.
         *
         * @param value {@code true} if the name tag should be hidden, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update sneaking(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should play sprinting particles.
         *
         * @param value {@code true} if the entity should play sprinting particles, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update sprinting(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should be swimming.
         *
         * @param value {@code true} if the entity should be swimming, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update swimming(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should be invisible.
         *
         * @param value {@code true} if the entity should be invisible, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update invisible(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should have the glowing effect.
         *
         * @param value {@code true} if the entity should have the glowing effect, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update glowing(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should be gliding.
         *
         * @param value {@code true} if the entity should be gliding, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update gliding(boolean value);

        /**
         * Sets number of remaining air supply for the {@linkplain Entity entity}.
         *
         * @param value the remaining air supply that the entity should have
         * @return this update builder
         * @since 1.0
         */
        Update airSupply(int value);

        /**
         * Sets whether custom name of the {@linkplain Entity entity} should be visible.
         *
         * @param value {@code true} if the custom name should be visible, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update customNameVisible(boolean value);

        /**
         * Sets custom name for the {@linkplain Entity entity}.
         *
         * @param value the custom name that the entity should have
         * @return this update builder
         * @since 1.0
         */
        Update customName(@Nullable Component value);

        /**
         * Sets whether the {@linkplain Entity entity} should be silent, meaning that it does not play any sounds.
         *
         * @param value {@code true} if the entity should be silent, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update silent(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should have no gravity.
         *
         * @param value {@code true} if the entity should have no gravity, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        Update hasNoGravity(boolean value);

        /**
         * Sets pose for the {@linkplain Entity entity}.
         *
         * @param value the pose that the entity should have
         * @return this update builder
         * @since 1.0
         */
        Update pose(Pose value);

        /**
         * Sets the number of ticks for which the {@linkplain Entity entity} has been frozen.
         *
         * @param value the number of ticks for which the entity has been frozen
         * @return this update builder
         * @since 1.0
         */
        Update ticksFrozen(int value);

        /**
         * Builds and performs the {@linkplain Update entity metadata update}.
         *
         * <p>After performing the update this builder can be safely reused
         * as the updated value map is being cleared after performing the update.</p>
         *
         * @return this update builder
         * @since 1.0
         */
        Update performUpdate();
    }
}