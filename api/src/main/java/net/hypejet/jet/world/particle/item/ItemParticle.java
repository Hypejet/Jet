package net.hypejet.jet.world.particle.item;

import net.hypejet.jet.inventory.item.ItemStack;
import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain Particle particle} containing an {@linkplain ItemStack item stack} field.
 *
 * @since 1.0
 * @see ItemStack
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ItemParticle extends Particle {
    /**
     * Gets the {@linkplain ItemStack item stack} field value of this particle.
     *
     * @return the item stack
     * @since 1.0
     */
    ItemStack itemStack();

    /**
     * A {@linkplain Particle.Builder particle builder} of an {@linkplain ItemParticle item particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see ItemParticle
     * @see Particle.Builder
     */
    interface Builder<P extends ItemParticle, B extends Builder<P, B>> extends Particle.Builder<P> {
        /**
         * Sets the {@linkplain ItemStack item stack} field value that the particle should have.
         *
         * @param value the item stack
         * @return this particle builder
         * @since 1.0
         */
        B itemStack(ItemStack value);
    }
}