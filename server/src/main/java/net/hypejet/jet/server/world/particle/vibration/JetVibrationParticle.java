package net.hypejet.jet.server.world.particle.vibration;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.source.BlockPositionSource;
import net.hypejet.jet.world.coordinate.source.EntityPositionSource;
import net.hypejet.jet.world.coordinate.source.PositionSource;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.vibration.VibrationParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain VibrationParticle vibration particle}.
 *
 * @since 1.
 * @see VibrationParticle
 */
@NullMarked
public final class JetVibrationParticle extends JetParticle implements VibrationParticle {

    private final PositionSource destination;
    private final int arrivalDuration;

    /**
     * Constructs the {@linkplain JetVibrationParticle vibration particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @since 1.0
     */
    JetVibrationParticle(Holder.Reference<ParticleType> particleType,
                         PositionSource destination, int arrivalDuration) {
        super(particleType);
        this.destination = Objects.requireNonNull(destination, "destination");
        this.arrivalDuration = arrivalDuration;

        if (destination instanceof EntityPositionSource)
            throw new IllegalArgumentException("The destination may not use entity position source");
    }

    @Override
    public PositionSource destination() {
        return this.destination;
    }

    @Override
    public int arrivalDuration() {
        return this.arrivalDuration;
    }

    /**
     * An implementation of the {@linkplain VibrationParticle.Builder vibration particle builder}
     * creating {@linkplain JetVibrationParticle vibration particles} without additional options.
     *
     * @since 1.0
     * @see JetVibrationParticle
     * @see VibrationParticle.Builder
     */
    public static final class Builder implements VibrationParticle.Builder<JetVibrationParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;

        private PositionSource destination = new BlockPositionSource(BlockPosition.zero());
        private int arrivalDuration;

        /**
         * Constructs the {@linkplain Builder vibration particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public Builder destination(PositionSource value) {
            this.destination = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public Builder arrivalDuration(int value) {
            this.arrivalDuration = value;
            return this;
        }

        @Override
        public JetVibrationParticle build() {
            return new JetVibrationParticle(this.particleType, this.destination, this.arrivalDuration);
        }
    }
}