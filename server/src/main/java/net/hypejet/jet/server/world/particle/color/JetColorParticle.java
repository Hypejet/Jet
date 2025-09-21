package net.hypejet.jet.server.world.particle.color;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.color.ColorParticle;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.util.ARGBLike;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain ColorParticle color particle}.
 *
 * @since 1.0
 * @see ColorParticle
 */
@NullMarked
public final class JetColorParticle extends JetParticle implements ColorParticle {

    private final ARGBLike color;

    /**
     * Constructs the {@linkplain JetColorParticle color particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param color the color that the particle should have
     * @since 1.0
     */
    private JetColorParticle(Holder.Reference<ParticleType> particleType, ARGBLike color) {
        super(particleType);
        this.color = Objects.requireNonNull(color, "color");
    }

    @Override
    public ARGBLike color() {
        return this.color;
    }

    /**
     * An implementation of the {@linkplain ColorParticle.Builder color particle builder}
     * creating {@linkplain JetColorParticle color particles} without additional options.
     *
     * @since 1.0
     * @see JetColorParticle
     * @see ColorParticle.Builder
     */
    public static final class Builder implements ColorParticle.Builder<JetColorParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;
        private ARGBLike color = ShadowColor.shadowColor(255, 255, 255, 255); // TODO: Replace with custom ARGB-like impl

        /**
         * Constructs the {@linkplain Builder color particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public Builder color(ARGBLike value) {
            this.color = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public JetColorParticle build() {
            return new JetColorParticle(this.particleType, this.color);
        }
    }
}