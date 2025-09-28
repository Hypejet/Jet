package net.hypejet.jet.server.network.codec.game.world.particle.block;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.block.JetBlockParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain JetBlockParticle block particles}.
 *
 * @since 1.0
 * @see JetBlockParticle
 * @see NetworkWriter
 */
public final class BlockParticleAdditionalNetworkWriter implements NetworkWriter<JetBlockParticle> {
    /**
     * An instance of the {@linkplain BlockParticleAdditionalNetworkWriter block particle additional network writer}.
     *
     * @since 1.0
     */
    public static final BlockParticleAdditionalNetworkWriter INSTANCE = new BlockParticleAdditionalNetworkWriter();

    private BlockParticleAdditionalNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull JetBlockParticle object) {
        VarIntNetworkCodec.INSTANCE.write(
                buf, registryManager,
                registryManager.blockStateRegistry().indexOf(object.blockState())
        );
    }
}