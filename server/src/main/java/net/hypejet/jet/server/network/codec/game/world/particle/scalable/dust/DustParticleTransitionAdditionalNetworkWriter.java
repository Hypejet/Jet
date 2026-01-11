package net.hypejet.jet.server.network.codec.game.world.particle.scalable.dust;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.util.color.RGBColorNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.world.particle.scalable.dust.DustTransitionParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional
 * data of {@linkplain DustTransitionParticle dust transition particles}.
 *
 * @since 1.0
 * @see DustTransitionParticle
 * @see NetworkWriter
 */
public final class DustParticleTransitionAdditionalNetworkWriter implements NetworkWriter<DustTransitionParticle> {
    /**
     * An instance of the
     * {@linkplain DustParticleTransitionAdditionalNetworkWriter dust particle transition additional network writer}.
     *
     * @since 1.0
     */
    public static final DustParticleTransitionAdditionalNetworkWriter
            INSTANCE = new DustParticleTransitionAdditionalNetworkWriter();

    private DustParticleTransitionAdditionalNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull DustTransitionParticle object) {
        RGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.fromColor());
        RGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.toColor());
        buf.writeFloat(object.scale());
    }
}