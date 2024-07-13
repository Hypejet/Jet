package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientQueryBlockEntityTagPacket;
import net.hypejet.jet.server.network.protocol.codecs.position.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
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
        return new ClientQueryBlockEntityTagPacket(NetworkUtil.readVarInt(buf),
                BlockPositionNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientQueryBlockEntityTagPacket object) {
        NetworkUtil.writeVarInt(buf, object.transactionId());
        BlockPositionNetworkCodec.instance().write(buf, object.blockPosition());
    }

    @Override
    public void handle(@NonNull ClientQueryBlockEntityTagPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}