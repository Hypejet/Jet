package net.hypejet.jet.server.world.particle.scalable;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.scalable.ScalableParticle;
import org.jspecify.annotations.NullMarked;

/**
 * An implementation of the {@linkplain ScalableParticle scalable particle}.
 *
 * @since 1.0
 * @see ScalableParticle
 */
@NullMarked
public class JetScalableParticle extends JetParticle implements ScalableParticle {

    private final float scale;

    /**
     * Constructs the {@linkplain JetScalableParticle scalable particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param scale the scale that the particle should have
     * @since 1.0
     */
    protected JetScalableParticle(Holder.Reference<ParticleType> particleType, float scale) {
        super(particleType);
        this.scale = Math.clamp(scale, 0.01f, 4f);
    }

    @Override
    public final float scale() {
        return this.scale;
    }
}