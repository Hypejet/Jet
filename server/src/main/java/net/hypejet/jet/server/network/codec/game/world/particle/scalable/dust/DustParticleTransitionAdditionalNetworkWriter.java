package net.hypejet.jet.server.network.codec.game.world.particle.scalable.dust;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.util.color.RGBColorNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.scalable.dust.JetDustTransitionParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional
 * data of {@linkplain JetDustTransitionParticle dust transition particles}.
 *
 * @since 1.0
 * @see JetDustTransitionParticle
 * @see NetworkWriter
 */
public final class DustParticleTransitionAdditionalNetworkWriter implements NetworkWriter<JetDustTransitionParticle> {
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
                      @NonNull JetDustTransitionParticle object) {
        RGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.fromColor());
        RGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.toColor());
        buf.writeFloat(object.scale());
    }
}