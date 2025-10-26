package net.hypejet.jet.server.network.codec.game.world.particle.shriek;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.world.particle.shriek.ShriekParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain ShriekParticle shriek particles}.
 *
 * @since 1.0
 * @see ShriekParticle
 * @see NetworkWriter
 */
public final class ShriekParticleAdditionalNetworkWriter implements NetworkWriter<ShriekParticle> {
    /**
     * An instance of the {@linkplain ShriekParticleAdditionalNetworkWriter shriek particle additional network writer}.
     *
     * @since 1.0
     */
    public static final ShriekParticleAdditionalNetworkWriter INSTANCE = new ShriekParticleAdditionalNetworkWriter();

    private ShriekParticleAdditionalNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ShriekParticle object) {
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.delay());
    }
}