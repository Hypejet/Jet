package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.scoreboard.position.ScoreboardPositionNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSetObjectiveDisplayedPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer} which writes
 * {@linkplain ServerSetObjectiveDisplayedPlayPacket a server set objective displayed play packet}.
 *
 * @since 1.0
 * @see ServerSetObjectiveDisplayedPlayPacket
 * @see NetworkWriter
 */
public final class ServerSetObjectiveDisplayedPlayPacketWriter
        implements NetworkWriter<ServerSetObjectiveDisplayedPlayPacket> {
    /**
     * An instance of
     * the {@linkplain ServerSetObjectiveDisplayedPlayPacketWriter server set objective displayed play packet writer}.
     *
     * @since 1.0
     * @see ServerSetObjectiveDisplayedPlayPacketWriter
     */
    public static final ServerSetObjectiveDisplayedPlayPacketWriter
            INSTANCE = new ServerSetObjectiveDisplayedPlayPacketWriter();

    private ServerSetObjectiveDisplayedPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerSetObjectiveDisplayedPlayPacket object) {
        ScoreboardPositionNetworkWriter.INSTANCE.write(buf, registryManager, object.position());
        String name = object.name();
        StringNetworkCodec.INSTANCE.write(buf, registryManager, name == null ? "" : name);
    }
}