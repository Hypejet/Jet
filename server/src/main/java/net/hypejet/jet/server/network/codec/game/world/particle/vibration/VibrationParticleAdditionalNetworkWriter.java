package net.hypejet.jet.server.network.codec.game.world.particle.vibration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.vibration.JetVibrationParticle;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain NetworkWriter network writer} of additional
 * data of {@linkplain JetVibrationParticle vibration particles}.
 *
 * @since 1.0
 * @see JetVibrationParticle
 * @see NetworkWriter
 */
@NullMarked
public final class VibrationParticleAdditionalNetworkWriter implements NetworkWriter<JetVibrationParticle> {

    /**
     * An instance of the
     * {@linkplain VibrationParticleAdditionalNetworkWriter vibration particle additional network writer}.
     *
     * @since 1.0
     */
    public static final VibrationParticleAdditionalNetworkWriter
            INSTANCE = new VibrationParticleAdditionalNetworkWriter();

    private VibrationParticleAdditionalNetworkWriter() {}

    @Override
    public void write(ByteBuf buf, JetRegistryManager registryManager, JetVibrationParticle object) {
        writeBlockPositionSource(buf, registryManager, object.destination());
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.arrivalDuration());
    }

    private static void writeBlockPositionSource(ByteBuf buf,
                                                 JetRegistryManager registryManager,
                                                 BlockPosition position) {
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, 0); // 0 is block position source type registry index
        BlockPositionNetworkCodec.INSTANCE.write(buf, registryManager, position);
    }
}