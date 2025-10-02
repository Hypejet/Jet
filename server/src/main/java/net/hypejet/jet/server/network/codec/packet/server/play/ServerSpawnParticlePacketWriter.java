package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.floats.FloatVectorNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.vector.VectorNetworkCodec;
import net.hypejet.jet.server.network.codec.game.world.particle.ParticleNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSpawnParticlePacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain ServerSpawnParticlePacket server spawn particle packet}.
 *
 * @since 1.0
 * @see ServerSpawnParticlePacket
 * @see NetworkWriter
 */
public final class ServerSpawnParticlePacketWriter implements NetworkWriter<ServerSpawnParticlePacket> {
    /**
     * An instance of the {@linkplain ServerSpawnParticlePacketWriter server spawn particle packet writer}.
     *
     * @since 1.0
     */
    public static final ServerSpawnParticlePacketWriter INSTANCE = new ServerSpawnParticlePacketWriter();

    private ServerSpawnParticlePacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ServerSpawnParticlePacket object) {
        buf.writeBoolean(object.overrideLimiter());
        buf.writeBoolean(object.alwaysShow());
        VectorNetworkCodec.INSTANCE.write(buf, registryManager, object.position());
        FloatVectorNetworkWriter.INSTANCE.write(buf, registryManager, object.offset());
        buf.writeInt(object.count());
        ParticleNetworkWriter.INSTANCE.write(buf, registryManager, object.particle());
    }
}