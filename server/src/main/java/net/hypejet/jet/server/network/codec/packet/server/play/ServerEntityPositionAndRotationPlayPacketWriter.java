package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.AngleNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityPositionAndRotationPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntityPositionAndRotationPlayPacket server entity position and rotation play packets}.
 *
 * @since 1.0
 * @see ServerEntityPositionAndRotationPlayPacket
 * @see NetworkWriter
 */
public final class ServerEntityPositionAndRotationPlayPacketWriter
        implements NetworkWriter<ServerEntityPositionAndRotationPlayPacket> {
    /**
     * An instance of the {@linkplain ServerEntityPositionAndRotationPlayPacketWriter server entity position
     * and rotation play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityPositionAndRotationPlayPacketWriter
            INSTANCE = new ServerEntityPositionAndRotationPlayPacketWriter();

    private ServerEntityPositionAndRotationPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerEntityPositionAndRotationPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.entityId());
        buf.writeShort(object.deltaX());
        buf.writeShort(object.deltaY());
        buf.writeShort(object.deltaZ());
        AngleNetworkWriter.INSTANCE.write(buf, registryManager, object.yaw());
        AngleNetworkWriter.INSTANCE.write(buf, registryManager, object.pitch());
        buf.writeBoolean(object.onGround());
    }
}