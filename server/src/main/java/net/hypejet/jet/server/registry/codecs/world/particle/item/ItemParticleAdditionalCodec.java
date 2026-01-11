package net.hypejet.jet.server.registry.codecs.world.particle.item;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.item.ItemParticle;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain ItemParticle item particles}.
 *
 * @since 1.0
 * @see ItemParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class ItemParticleAdditionalCodec implements ParticleAdditionalCodec<ItemParticle> {
    /**
     * An instance of the {@linkplain ItemParticleAdditionalCodec item particle additional codec}.
     *
     * @since 1.0
     */
    public static final ItemParticleAdditionalCodec INSTANCE = new ItemParticleAdditionalCodec();

    private ItemParticleAdditionalCodec() {}

    @Override
    public ItemParticle decode(Holder.Reference<ParticleType> particleType, CompoundBinaryTag compound) {
        // TODO: Implement
        throw new UnsupportedOperationException("Item particle decoding has not been implemented yet");
    }

    @Override
    public void encode(ItemParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        // TODO: Implement
        throw new UnsupportedOperationException("Item particle encoding has not been implemented yet");
    }
}