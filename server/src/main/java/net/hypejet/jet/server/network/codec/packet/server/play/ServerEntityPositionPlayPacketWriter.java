package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityPositionPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntityPositionPlayPacket server entity position play packets}.
 *
 * @since 1.0
 * @see ServerEntityPositionPlayPacket
 * @see NetworkWriter
 */
public final class ServerEntityPositionPlayPacketWriter implements NetworkWriter<ServerEntityPositionPlayPacket> {
    /**
     * An instance of the {@linkplain ServerEntityPositionPlayPacketWriter server entity position play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityPositionPlayPacketWriter INSTANCE = new ServerEntityPositionPlayPacketWriter();

    private ServerEntityPositionPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerEntityPositionPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.entityId());
        buf.writeShort(object.deltaX());
        buf.writeShort(object.deltaY());
        buf.writeShort(object.deltaZ());
        buf.writeBoolean(object.onGround());
    }
}