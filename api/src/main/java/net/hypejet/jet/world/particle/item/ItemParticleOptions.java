package net.hypejet.jet.world.particle.item;

import net.hypejet.jet.inventory.item.ItemStack;
import net.hypejet.jet.world.particle.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * {@linkplain ParticleOptions Particle options} containing an {@linkplain ItemStack item stack} field.
 *
 * @since 1.0
 * @see ItemStack
 * @see ParticleOptions
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ItemParticleOptions extends ParticleOptions {
    /**
     * Gets the {@linkplain ItemStack item stack} field value
     * of this {@linkplain ItemParticleOptions item particle options}.
     *
     * @return the item stack
     * @since 1.0
     */
    ItemStack itemStack();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of a {@linkplain ItemParticleOptions item particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see ItemParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends ItemParticleOptions, B extends Builder<O, B>> extends ParticleOptions.Builder<O> {
        /**
         * Sets the {@linkplain ItemStack item stack} field value that
         * the {@linkplain ItemParticleOptions item particle options} should have.
         *
         * @param value the item stack
         * @return this particle options builder
         * @since 1.0
         */
        B itemStack(ItemStack value);
    }
}