package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizeRotationPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerSynchronizeRotationPlayPacket a server synchronize rotation play packet}.
 *
 * @since 1.0
 * @see NetworkWriter
 * @see ServerSynchronizeRotationPlayPacket
 */
public final class ServerSynchronizeRotationPlayPacketWriter
        implements NetworkWriter<ServerSynchronizeRotationPlayPacket> {
    /**
     * An instance of the {@linkplain ServerSynchronizeRotationPlayPacketWriter server synchronize rotation play packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerSynchronizeRotationPlayPacketWriter
            INSTANCE = new ServerSynchronizeRotationPlayPacketWriter();

    private ServerSynchronizeRotationPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerSynchronizeRotationPlayPacket object) {
        buf.writeFloat(object.yaw());
        buf.writeFloat(object.pitch());
    }
}