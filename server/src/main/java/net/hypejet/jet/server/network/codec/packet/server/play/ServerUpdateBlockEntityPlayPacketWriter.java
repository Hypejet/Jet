package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBlockEntityPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerUpdateBlockEntityPlayPacket a server update block entity play packet}.
 *
 * @since 1.0
 * @see ServerUpdateBlockEntityPlayPacket
 * @see NetworkWriter
 */
public final class ServerUpdateBlockEntityPlayPacketWriter
        implements NetworkWriter<ServerUpdateBlockEntityPlayPacket> {
    /**
     * An instance
     * of the {@linkplain ServerUpdateBlockEntityPlayPacketWriter server update block entity play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerUpdateBlockEntityPlayPacketWriter
            INSTANCE = new ServerUpdateBlockEntityPlayPacketWriter();

    private ServerUpdateBlockEntityPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerUpdateBlockEntityPlayPacket object) {
        BlockPositionNetworkCodec.INSTANCE.write(buf, registryManager, object.position());
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.blockEntityTypeIdentifier());
        BinaryTagNetworkWriter.INSTANCE.write(buf, registryManager, object.data());
    }
}