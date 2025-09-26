package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.varint.VarIntArrayNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerRemoveEntitiesPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerRemoveEntitiesPlayPacket server remove entities play packets}.
 *
 * @since 1.0
 * @see ServerRemoveEntitiesPlayPacket
 * @see NetworkWriter
 */
public final class ServerRemoveEntitiesPlayPacketWriter implements NetworkWriter<ServerRemoveEntitiesPlayPacket> {
    /**
     * An instance of the {@linkplain ServerRemoveEntitiesPlayPacketWriter server remove entities play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerRemoveEntitiesPlayPacketWriter INSTANCE = new ServerRemoveEntitiesPlayPacketWriter();

    private ServerRemoveEntitiesPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerRemoveEntitiesPlayPacket object) {
        VarIntArrayNetworkWriter.INSTANCE.write(buf, registryManager, object.entityIds());
    }
}