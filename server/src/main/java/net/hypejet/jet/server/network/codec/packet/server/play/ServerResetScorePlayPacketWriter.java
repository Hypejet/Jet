package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerResetScorePlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer} which writes
 * {@linkplain ServerResetScorePlayPacket a server reset score play packet}.
 *
 * @since 1.0
 * @see ServerResetScorePlayPacket
 * @see NetworkWriter
 */
public final class ServerResetScorePlayPacketWriter implements NetworkWriter<ServerResetScorePlayPacket> {
    /**
     * An instance of the {@linkplain ServerResetScorePlayPacketWriter server reset score play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerResetScorePlayPacketWriter INSTANCE = new ServerResetScorePlayPacketWriter();

    private ServerResetScorePlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerResetScorePlayPacket object) {
        StringNetworkCodec.INSTANCE.write(buf, registryManager, object.entityName());
        NetworkUtil.writeOptional(object.objectiveName(), StringNetworkCodec.INSTANCE, buf, registryManager);
    }
}