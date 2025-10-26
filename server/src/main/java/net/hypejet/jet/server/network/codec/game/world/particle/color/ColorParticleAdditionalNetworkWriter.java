package net.hypejet.jet.server.network.codec.game.world.particle.color;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.util.color.ARGBColorNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.world.particle.color.ColorParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain ColorParticle color particles}.
 *
 * @since 1.0
 * @see ColorParticle
 * @see NetworkWriter
 */
public final class ColorParticleAdditionalNetworkWriter implements NetworkWriter<ColorParticle> {
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
                      @NonNull ColorParticle object) {
        ARGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.color());
    }
}