package net.hypejet.jet.server.world.particle.shriek;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.shriek.ShriekParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain ShriekParticle shriek particle}.
 *
 * @since 1.0
 * @see ShriekParticle
 */
@NullMarked
public final class JetShriekParticle extends JetParticle implements ShriekParticle {

    private final int delay;

    /**
     * Constructs the {@linkplain JetShriekParticle shriek particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param delay the time after which the particle should be actually displayed after spawning
     * @since 1.0
     */
    JetShriekParticle(Holder.Reference<ParticleType> particleType, int delay) {
        super(particleType);
        this.delay = delay;
    }

    @Override
    public int delay() {
        return this.delay;
    }

    /**
     * An implementation of the {@linkplain ShriekParticle.Builder shriek particle builder}.
     *
     * @since 1.0
     * @see ShriekParticle.Builder
     */
    public static final class Builder implements ShriekParticle.Builder<JetShriekParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;
        private int delay;

        /**
         * Constructs the {@linkplain Builder shriek particle builder implementation}.
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
        public JetShriekParticle build() {
            return new JetShriekParticle(this.particleType, this.delay);
        }
    }
}