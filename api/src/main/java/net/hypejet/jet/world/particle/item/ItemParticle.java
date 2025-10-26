package net.hypejet.jet.world.particle.item;

import net.hypejet.jet.inventory.item.ItemStack;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain Particle particle} containing an {@linkplain ItemStack item stack} field.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param itemStack the item stack field value of this particle
 * @since 1.0
 * @see ItemStack
 * @see Particle
 */
@NullMarked
public record ItemParticle(Holder.Reference<ParticleType> particleType, ItemStack itemStack) implements Particle {
    /**
     * Constructs the {@linkplain ItemParticle item particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param itemStack the value that the item stack field of the particle should have
     * @since 1.0
     */
    public ItemParticle {
        Objects.requireNonNull(particleType, "particle type");
        Objects.requireNonNull(itemStack, "item stack");
    }
}