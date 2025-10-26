package net.hypejet.jet.server.network.codec.game.world.particle.block;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.registry.blockstate.JetBlockStateRegistry;
import net.hypejet.jet.server.world.block.state.JetBlockState;
import net.hypejet.jet.world.particle.block.BlockParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain BlockParticle block particles}.
 *
 * @since 1.0
 * @see BlockParticle
 * @see NetworkWriter
 */
public final class BlockParticleAdditionalNetworkWriter implements NetworkWriter<BlockParticle> {
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
                      @NonNull BlockParticle object) {
        JetBlockStateRegistry blockStateRegistry = registryManager.blockStateRegistry();
        JetBlockState blockState = blockStateRegistry.blockState(object.blockStateReference());
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, blockStateRegistry.indexOf(blockState));
    }
}