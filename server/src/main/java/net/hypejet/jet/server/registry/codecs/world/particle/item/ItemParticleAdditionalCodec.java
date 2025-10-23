package net.hypejet.jet.server.registry.codecs.world.particle.item;

import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.item.JetItemParticle;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain JetItemParticle item particles}.
 *
 * @since 1.0
 * @see JetItemParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class ItemParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetItemParticle, JetItemParticle.Builder> {
    /**
     * An instance of the {@linkplain ItemParticleAdditionalCodec item particle additional codec}.
     *
     * @since 1.0
     */
    public static final ItemParticleAdditionalCodec INSTANCE = new ItemParticleAdditionalCodec();

    private ItemParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetItemParticle.Builder particleBuilder) {
        // TODO: Implement
        throw new UnsupportedOperationException("Item particle decoding has not been implemented yet");
    }

    @Override
    public void encode(JetItemParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        // TODO: Implement
        throw new UnsupportedOperationException("Item particle encoding has not been implemented yet");
    }
}