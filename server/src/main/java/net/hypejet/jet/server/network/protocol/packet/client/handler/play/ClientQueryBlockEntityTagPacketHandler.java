package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientQueryBlockEntityTagPacket;
import net.hypejet.jet.server.network.protocol.codecs.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientQueryBlockEntityTagPacket a query block entity tag packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientQueryBlockEntityTagPacket
 * @see ClientPacketHandler
 */
public final class ClientQueryBlockEntityTagPacketHandler
        implements ClientPacketHandler<ClientQueryBlockEntityTagPacket> {
    @Override
    public @NonNull ClientQueryBlockEntityTagPacket read(@NonNull ByteBuf buf) {
        return new ClientQueryBlockEntityTagPacket(
                VarIntNetworkCodec.INSTANCE.read(buf),
                BlockPositionNetworkCodec.INSTANCE.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientQueryBlockEntityTagPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}