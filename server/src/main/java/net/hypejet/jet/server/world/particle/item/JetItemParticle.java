package net.hypejet.jet.server.world.particle.item;

import net.hypejet.jet.inventory.item.ItemStack;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.keys.ItemKeys;
import net.hypejet.jet.server.inventory.item.JetItemStack;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.item.ItemParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;

/**
 * An implementation of the {@linkplain ItemParticle item particle}.
 *
 * @since 1.0
 * @see ItemParticle
 */
@NullMarked
public final class JetItemParticle extends JetParticle implements ItemParticle {

    private final ItemStack itemStack;

    /**
     * Constructs the {@linkplain JetItemParticle item particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param itemStack the value that the item stack field of the particle should have
     * @since 1.0
     */
    JetItemParticle(Holder.Reference<ParticleType> particleType, ItemStack itemStack) {
        super(particleType);
        this.itemStack = Objects.requireNonNull(itemStack, "item stack");
    }

    @Override
    public ItemStack itemStack() {
        return this.itemStack;
    }

    /**
     * An implementation of the {@linkplain ItemParticle.Builder item particle builder}
     * creating {@linkplain JetItemParticle item particles} without additional options.
     *
     * @since 1.0
     * @see JetItemParticle
     * @see ItemParticle.Builder
     */
    public static final class Builder implements ItemParticle.Builder<JetItemParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;
        private ItemStack itemStack;

        /**
         * Constructs the {@linkplain Builder item particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
            this.itemStack = new JetItemStack(new Holder.Reference<>(ItemKeys.STONE), 1, Map.of());
        }

        @Override
        public Builder itemStack(ItemStack value) {
            this.itemStack = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public JetItemParticle build() {
            return new JetItemParticle(this.particleType, this.itemStack);
        }
    }
}