package net.hypejet.jet.server.world.particle.scalable.dust;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.scalable.JetScalableParticle;
import net.hypejet.jet.util.color.Color;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.scalable.dust.DustTransitionParticle;
import net.kyori.adventure.util.RGBLike;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain DustTransitionParticle dust transition particle}.
 *
 * @since 1.0
 * @see DustTransitionParticle
 */
@NullMarked
public final class JetDustTransitionParticle extends JetScalableParticle implements DustTransitionParticle {

    private final RGBLike fromColor;
    private final RGBLike toColor;

    /**
     * Constructs the {@linkplain JetDustTransitionParticle dust transition particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param scale the scale that the particle should have
     * @param fromColor the initial color (before transition) that the dust particle should have
     * @param toColor the last color (after transition) that the dust particle should have
     * @since 1.0
     */
    JetDustTransitionParticle(Holder.Reference<ParticleType> particleType, float scale,
                              RGBLike fromColor, RGBLike toColor) {
        super(particleType, scale);
        this.fromColor = Objects.requireNonNull(fromColor, "from color");
        this.toColor = Objects.requireNonNull(toColor, "to color");
    }

    @Override
    public RGBLike fromColor() {
        return this.fromColor;
    }

    @Override
    public RGBLike toColor() {
        return this.toColor;
    }

    /**
     * An implementation of the {@linkplain DustTransitionParticle.Builder dust transition particle builder}
     * creating {@linkplain JetDustTransitionParticle dust transition particles} without additional options.
     *
     * @since 1.0
     * @see JetDustTransitionParticle
     * @see DustTransitionParticle.Builder
     */
    public static final class Builder implements DustTransitionParticle.Builder<JetDustTransitionParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;

        private float scale = 1f;
        private RGBLike fromColor = Color.fromRGB(255, 255, 255);
        private RGBLike toColor = Color.fromRGB(255, 255, 255);

        /**
         * Constructs the {@linkplain Builder dust transition particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public Builder fromColor(RGBLike value) {
            this.fromColor = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public Builder toColor(RGBLike value) {
            this.toColor = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public Builder scale(float value) {
            this.scale = value;
            return this;
        }

        @Override
        public JetDustTransitionParticle build() {
            return new JetDustTransitionParticle(this.particleType, this.scale, this.fromColor, this.toColor);
        }
    }
}