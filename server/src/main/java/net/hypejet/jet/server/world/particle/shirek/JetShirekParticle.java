package net.hypejet.jet.server.world.particle.shirek;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.shirek.ShirekParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain ShirekParticle shirek particle}.
 *
 * @since 1.0
 * @see ShirekParticle
 */
@NullMarked
public final class JetShirekParticle extends JetParticle implements ShirekParticle {

    private final int delay;

    /**
     * Constructs the {@linkplain JetShirekParticle shirek particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param delay the time after which the particle should be actually displayed after spawning
     * @since 1.0
     */
    JetShirekParticle(Holder.Reference<ParticleType> particleType, int delay) {
        super(particleType);
        this.delay = delay;
    }

    @Override
    public int delay() {
        return this.delay;
    }

    /**
     * An implementation of the {@linkplain ShirekParticle.Builder shirek particle builder}.
     *
     * @since 1.0
     * @see ShirekParticle.Builder
     */
    public static final class Builder implements ShirekParticle.Builder<JetShirekParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;
        private int delay;

        /**
         * Constructs the {@linkplain Builder shirek particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public Builder delay(int value) {
            this.delay = value;
            return this;
        }

        @Override
        public JetShirekParticle build() {
            return new JetShirekParticle(this.particleType, this.delay);
        }
    }
}