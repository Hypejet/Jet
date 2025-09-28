package net.hypejet.jet.server.network.codec.game.world.particle.color;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.util.color.ARGBColorNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.color.JetColorParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain JetColorParticle color particles}.
 *
 * @since 1.0
 * @see JetColorParticle
 * @see NetworkWriter
 */
public final class ColorParticleAdditionalNetworkWriter implements NetworkWriter<JetColorParticle> {
    /**
     * An instance of the {@linkplain ColorParticleAdditionalNetworkWriter color particle additional network writer}.
     *
     * @since 1.0
     */
    public static final ColorParticleAdditionalNetworkWriter INSTANCE = new ColorParticleAdditionalNetworkWriter();

    private ColorParticleAdditionalNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull JetColorParticle object) {
        ARGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.color());
    }
}