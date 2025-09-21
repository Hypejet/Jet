package net.hypejet.jet.server.world.particle.scalable.dust;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.scalable.JetScalableParticle;
import net.hypejet.jet.util.color.Color;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.scalable.dust.DustParticle;
import net.kyori.adventure.util.RGBLike;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain DustParticle dust particle}.
 *
 * @since 1.0
 * @see DustParticle
 */
@NullMarked
public final class JetDustParticle extends JetScalableParticle implements DustParticle {

    private final RGBLike color;

    /**
     * Constructs the {@linkplain JetDustParticle dust particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param scale the scale that the particle should have
     * @since 1.0
     */
    JetDustParticle(Holder.Reference<ParticleType> particleType, float scale, RGBLike color) {
        super(particleType, scale);
        this.color = Objects.requireNonNull(color, "color");
    }

    @Override
    public RGBLike color() {
        return this.color;
    }

    /**
     * An implementation of the {@linkplain DustParticle.Builder dust particle builder}
     * creating {@linkplain JetDustParticle dust particles} without additional options.
     *
     * @since 1.0
     * @see JetDustParticle
     * @see DustParticle.Builder
     */
    public static final class Builder implements DustParticle.Builder<JetDustParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;

        private float scale = 1f;
        private RGBLike color = Color.fromRGB(255, 255, 255);

        /**
         * Constructs the {@linkplain Builder dust particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public Builder color(RGBLike value) {
            this.color = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public Builder scale(float value) {
            this.scale = value;
            return this;
        }

        @Override
        public JetDustParticle build() {
            return new JetDustParticle(this.particleType, this.scale, this.color);
        }
    }
}