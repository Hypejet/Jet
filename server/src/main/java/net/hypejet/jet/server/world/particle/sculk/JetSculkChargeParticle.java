package net.hypejet.jet.server.world.particle.sculk;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.sculk.SculkChargeParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain SculkChargeParticle sculk charge particle}.
 *
 * @since 1.0
 * @see SculkChargeParticle
 */
@NullMarked
public final class JetSculkChargeParticle extends JetParticle implements SculkChargeParticle {

    private final float roll;

    /**
     * Constructs the {@linkplain JetSculkChargeParticle sculk charge particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param roll the rotation that the sculk charge particle should have
     * @since 1.0
     */
    JetSculkChargeParticle(Holder.Reference<ParticleType> particleType, float roll) {
        super(particleType);
        this.roll = roll;
    }

    @Override
    public float roll() {
        return this.roll;
    }

    /**
     * An implementation of the {@linkplain SculkChargeParticle.Builder sculk charge particle builder}
     * creating {@linkplain JetSculkChargeParticle sculk charge particles} without additional options.
     *
     * @since 1.0
     * @see JetSculkChargeParticle
     * @see SculkChargeParticle.Builder
     */
    public static final class Builder implements SculkChargeParticle.Builder<JetSculkChargeParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;
        private float roll;

        /**
         * Constructs the {@linkplain Builder sculk charge particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public Builder roll(float value) {
            this.roll = value;
            return this;
        }

        @Override
        public JetSculkChargeParticle build() {
            return new JetSculkChargeParticle(this.particleType, this.roll);
        }
    }
}