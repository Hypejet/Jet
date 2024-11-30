package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.play.ClientQueryBlockEntityTagPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientQueryBlockEntityTagPacket a query block entity tag packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientQueryBlockEntityTagPacket
 * @see NetworkReader
 */
public final class ClientQueryBlockEntityTagPacketReader implements NetworkReader<ClientQueryBlockEntityTagPacket> {
    @Override
    public @NonNull ClientQueryBlockEntityTagPacket read(@NonNull ByteBuf buf) {
        return new ClientQueryBlockEntityTagPacket(
                VarIntNetworkCodec.INSTANCE.read(buf),
                BlockPositionNetworkCodec.INSTANCE.read(buf)
        );
    }
}