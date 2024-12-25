package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRequestActionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRequestActionPlayPacket.Action;
import org.checkerframework.checker.nullness.qual.NonNull;

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

    private static final MapperNetworkCodec<Action, Integer> ACTION_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(Action.class, int.class)
                    .register(Action.PERFORM_RESPAWN, 0)
                    .register(Action.REQUEST_STATS, 1)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    private ClientRequestActionPlayPacketReader() {}

    @Override
    public @NonNull ClientRequestActionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRequestActionPlayPacket(ACTION_CODEC.read(buf));
    }
}