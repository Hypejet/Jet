package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.light.LightSerializationDataNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.chunk.VarIntChunkPositionNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateLightPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which
 * writes {@linkplain ServerUpdateLightPlayPacket a server update light play packet}.
 * 
 * @since 1.0
 * @see ServerUpdateLightPlayPacket
 * @see NetworkWriter
 */
public final class ServerUpdateLightPlayPacketWriter implements NetworkWriter<ServerUpdateLightPlayPacket> {
    /**
     * An instance of the {@linkplain ServerUpdateLightPlayPacketWriter server update light play packet writer}.
     * 
     * @since 1.0
     */
    public static final ServerUpdateLightPlayPacketWriter INSTANCE = new ServerUpdateLightPlayPacketWriter();

    private ServerUpdateLightPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ServerUpdateLightPlayPacket object) {
        VarIntChunkPositionNetworkWriter.INSTANCE.write(buf, registryManager, object.position());
        LightSerializationDataNetworkWriter.INSTANCE.write(buf, registryManager, object.data());
    }
}