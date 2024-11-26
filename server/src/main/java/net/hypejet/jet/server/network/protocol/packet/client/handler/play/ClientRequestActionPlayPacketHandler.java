package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.protocol.packet.client.play.ClientRequestActionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientRequestActionPlayPacket.Action;
import net.hypejet.jet.server.network.protocol.codecs.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientRequestActionPlayPacket a request action play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRequestActionPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientRequestActionPlayPacketHandler implements ClientPacketHandler<ClientRequestActionPlayPacket> {

    private static final MapperNetworkCodec<Action, Integer> ACTION_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(Action.class, int.class)
                    .register(Action.PERFORM_RESPAWN, 0)
                    .register(Action.REQUEST_STATS, 1)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    @Override
    public @NonNull ClientRequestActionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRequestActionPlayPacket(ACTION_CODEC.read(buf));
    }

    @Override
    public void handle(@NonNull ClientRequestActionPlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}