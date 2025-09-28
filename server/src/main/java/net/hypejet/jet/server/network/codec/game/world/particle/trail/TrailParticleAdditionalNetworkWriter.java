package net.hypejet.jet.server.network.codec.game.world.particle.trail;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.vector.VectorNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.util.color.RGBColorNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.trail.JetTrailParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain JetTrailParticle trail particles}.
 *
 * @since 1.0
 * @see JetTrailParticle
 * @see NetworkWriter
 */
public final class TrailParticleAdditionalNetworkWriter implements NetworkWriter<JetTrailParticle> {
    /**
     * An instance of the {@linkplain TrailParticleAdditionalNetworkWriter trail particle additional network writer}.
     *
     * @since 1.0
     */
    public static final TrailParticleAdditionalNetworkWriter INSTANCE = new TrailParticleAdditionalNetworkWriter();

    private TrailParticleAdditionalNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull JetTrailParticle object) {
        VectorNetworkCodec.INSTANCE.write(buf, registryManager, object.target());
        RGBColorNetworkWriter.INSTANCE.write(buf, registryManager, object.color());
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.travelDuration());
    }
}