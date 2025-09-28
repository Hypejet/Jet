package net.hypejet.jet.server.network.codec.game.world.particle.sculk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.sculk.JetSculkChargeParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data
 * of {@linkplain JetSculkChargeParticle sculk charge particles}.
 *
 * @since 1.0
 * @see JetSculkChargeParticle
 * @see NetworkWriter
 */
public final class SculkChargeParticleAdditionalNetworkWriter implements NetworkWriter<JetSculkChargeParticle> {
    /**
     * An instance of the
     * {@linkplain SculkChargeParticleAdditionalNetworkWriter sculk charge particle additional network writer}.
     *
     * @since 1.0
     */
    public static final SculkChargeParticleAdditionalNetworkWriter
            INSTANCE = new SculkChargeParticleAdditionalNetworkWriter();

    private SculkChargeParticleAdditionalNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull JetSculkChargeParticle object) {
        buf.writeFloat(object.roll());
    }
}