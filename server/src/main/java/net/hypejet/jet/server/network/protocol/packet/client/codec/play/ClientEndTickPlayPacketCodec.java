package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientEndTickPlayPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientEndTickPlayPacket client end tick play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientEndTickPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientEndTickPlayPacketCodec extends ClientPacketCodec<ClientEndTickPlayPacket> {
    /**
     * Constructs the {@linkplain ClientEndTickPlayPacketCodec client end tick packet codec}.
     *
     * @since 1.0
     */
    public ClientEndTickPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_CLIENT_END_TICK, ClientEndTickPlayPacket.class);
    }

    @Override
    public @NonNull ClientEndTickPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientEndTickPlayPacket();
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientEndTickPlayPacket object) {
        // NOOP
    }

    @Override
    public void handle(@NonNull ClientEndTickPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}