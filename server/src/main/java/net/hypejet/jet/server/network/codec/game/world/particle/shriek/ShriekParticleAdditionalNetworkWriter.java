package net.hypejet.jet.server.network.codec.game.world.particle.shriek;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.shriek.JetShriekParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain JetShriekParticle shriek particles}.
 *
 * @since 1.0
 * @see JetShriekParticle
 * @see NetworkWriter
 */
public final class ShriekParticleAdditionalNetworkWriter implements NetworkWriter<JetShriekParticle> {
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
                      @NonNull JetShriekParticle object) {
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.delay());
    }
}