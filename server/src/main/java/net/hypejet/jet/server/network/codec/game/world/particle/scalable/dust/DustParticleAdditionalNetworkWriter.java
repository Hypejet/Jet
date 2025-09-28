package net.hypejet.jet.server.network.codec.game.world.particle.scalable.dust;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.util.color.RGBColorNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.scalable.dust.JetDustParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain JetDustParticle dust particles}.
 *
 * @since 1.0
 * @see JetDustParticle
 * @see NetworkWriter
 */
public final class DustParticleAdditionalNetworkWriter implements NetworkWriter<JetDustParticle> {
    /**
     * An instance of the {@linkplain DustParticleAdditionalNetworkWriter dust particle additional network writer}.
     *
     * @since 1.0
     */
    public static final DustParticleAdditionalNetworkWriter INSTANCE = new DustParticleAdditionalNetworkWriter();

    private DustParticleAdditionalNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull JetDustParticle object) {
        RGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.color());
        buf.writeFloat(object.scale());
    }
}