package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientQueryBlockEntityTagPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientQueryBlockEntityTagPacket query block entity tag packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientQueryBlockEntityTagPacket
 * @see ClientPacketCodec
 */
public final class ClientQueryBlockEntityTagPacketCodec extends ClientPacketCodec<ClientQueryBlockEntityTagPacket> {
    /**
     * Constructs the {@linkplain ClientQueryBlockEntityTagPacket query block entity tag packet}.
     *
     * @since 1.0
     */
    public ClientQueryBlockEntityTagPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_QUERY_BLOCK_ENTITY_TAG, ClientQueryBlockEntityTagPacket.class);
    }

    @Override
    public @NonNull ClientQueryBlockEntityTagPacket read(@NonNull ByteBuf buf) {
        return new ClientQueryBlockEntityTagPacket(VarIntNetworkCodec.instance().read(buf),
                BlockPositionNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientQueryBlockEntityTagPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.transactionId());
        BlockPositionNetworkCodec.instance().write(buf, object.blockPosition());
    }

    @Override
    public void handle(@NonNull ClientQueryBlockEntityTagPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}