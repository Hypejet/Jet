package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRequestActionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRequestActionPlayPacket.Action;
import net.hypejet.jet.server.util.index.IndexUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientRequestActionPlayPacket a request action play packet}.
 *
 * @since 1.0
 * @see ClientRequestActionPlayPacket
 * @see NetworkReader
 */
public final class ClientRequestActionPlayPacketReader implements NetworkReader<ClientRequestActionPlayPacket> {

    /**
     * An instance of the {@linkplain ClientRequestActionPlayPacketReader client request action play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientRequestActionPlayPacketReader INSTANCE = new ClientRequestActionPlayPacketReader();

    private static final IndexNetworkCodec<Action, Integer> ACTION_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, Action.PERFORM_RESPAWN,
                    1, Action.REQUEST_STATS
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private ClientRequestActionPlayPacketReader() {}

    @Override
    public @NonNull ClientRequestActionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRequestActionPlayPacket(ACTION_CODEC.read(buf));
    }
}