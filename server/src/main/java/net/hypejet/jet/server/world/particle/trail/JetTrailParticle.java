package net.hypejet.jet.server.world.particle.trail;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.util.color.RGBColor;
import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.trail.TrailParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain TrailParticle trail particle}.
 *
 * @since 1.0
 * @see TrailParticle
 */
@NullMarked
public final class JetTrailParticle extends JetParticle implements TrailParticle {

    private final Vector target;
    private final RGBColor color;
    private final int travelDuration;

    /**
     * Constructs the {@linkplain JetTrailParticle trail particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param target the destination that this trail particle should start travelling to after spawning
     * @param color the trail particle color
     * @param travelDuration the particle travel time, in ticks
     * @since 1.0
     */
    JetTrailParticle(Holder.Reference<ParticleType> particleType, Vector target, RGBColor color, int travelDuration) {
        super(particleType);
        this.target = Objects.requireNonNull(target, "target");
        this.color = Objects.requireNonNull(color, "color");
        this.travelDuration = travelDuration;
    }

    @Override
    public Vector target() {
        return this.target;
    }

    @Override
    public RGBColor color() {
        return this.color;
    }

    @Override
    public int travelDuration() {
        return this.travelDuration;
    }

    /**
     * An implementation of the {@linkplain TrailParticle.Builder trail particle builder}
     * creating {@linkplain JetTrailParticle trail particles} without additional options.
     *
     * @since 1.0
     * @see JetTrailParticle
     * @see TrailParticle.Builder
     */
    public static final class Builder implements TrailParticle.Builder<JetTrailParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;

        private Vector target = Vector.zero();
        private RGBColor color = RGBColor.fromRGB(255, 255, 255);
        private int travelDuration;

        /**
         * Constructs the {@linkplain Builder trail particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public Builder target(Vector value) {
            this.target = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public Builder color(RGBColor value) {
            this.color = Objects.requireNonNull(value);
            return this;
        }

        @Override
        public Builder travelDuration(int value) {
            this.travelDuration = value;
            return this;
        }

        @Override
        public JetTrailParticle build() {
            return new JetTrailParticle(this.particleType, this.target, this.color, this.travelDuration);
        }
    }
}