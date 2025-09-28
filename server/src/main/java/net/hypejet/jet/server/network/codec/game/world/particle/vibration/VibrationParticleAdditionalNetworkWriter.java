package net.hypejet.jet.server.network.codec.game.world.particle.vibration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.source.PositionSourceNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.validation.ValidatedNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.vibration.JetVibrationParticle;
import net.hypejet.jet.world.coordinate.source.EntityPositionSource;
import net.hypejet.jet.world.coordinate.source.PositionSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional
 * data of {@linkplain JetVibrationParticle vibration particles}.
 *
 * @since 1.0
 * @see JetVibrationParticle
 * @see NetworkWriter
 */
public final class VibrationParticleAdditionalNetworkWriter implements NetworkWriter<JetVibrationParticle> {

    private static final NetworkWriter<PositionSource> VALIDATED_POSITION_SOURCE_WRITER = new ValidatedNetworkWriter<>(
            PositionSourceNetworkWriter.INSTANCE,
            positionSource -> !(positionSource instanceof EntityPositionSource),
            ignored -> new IllegalArgumentException("Entity position sources are not allowed in vibration particles")
    );

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
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull JetVibrationParticle object) {
        VALIDATED_POSITION_SOURCE_WRITER.write(buf, registryManager, object.destination());
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.arrivalDuration());
    }
}