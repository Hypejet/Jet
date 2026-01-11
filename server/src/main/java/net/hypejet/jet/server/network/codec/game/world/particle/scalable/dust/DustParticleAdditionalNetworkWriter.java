package net.hypejet.jet.server.network.codec.game.world.particle.scalable.dust;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.util.color.RGBColorNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.world.particle.scalable.dust.DustParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain DustParticle dust particles}.
 *
 * @since 1.0
 * @see DustParticle
 * @see NetworkWriter
 */
public final class DustParticleAdditionalNetworkWriter implements NetworkWriter<DustParticle> {
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
                      @NonNull DustParticle object) {
        RGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.color());
        buf.writeFloat(object.scale());
    }
}