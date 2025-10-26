package net.hypejet.jet.server.world.particle;

import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

/**
 * An implementation of the {@linkplain ParticleType particle type}.
 *
 * @see ParticleType
 * @since 1.0
 */
@NullMarked
public final class JetParticleType implements ParticleType {
    /**
     * An instance of the {@linkplain JetParticleType particle type implementation}.
     *
     * @since 1.0
     */
    public static final JetParticleType INSTANCE = new JetParticleType();

    private JetParticleType() {}
}