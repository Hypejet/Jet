package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientKeepAlivePlayPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveResponseHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientKeepAlivePlayPacket keep alive play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKeepAlivePlayPacket
 * @see ClientPacketCodec
 */
public final class ClientKeepAlivePlayPacketCodec extends ClientPacketCodec<ClientKeepAlivePlayPacket> {
    /**
     * Constructs the {@linkplain ClientKeepAlivePlayPacketCodec keep alive play packet codec}.
     *
     * @since 1.0
     */
    public ClientKeepAlivePlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_KEEP_ALIVE, ClientKeepAlivePlayPacket.class);
    }

    @Override
    public @NonNull ClientKeepAlivePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientKeepAlivePlayPacket(buf.readLong());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientKeepAlivePlayPacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }

    @Override
    public void handle(@NonNull ClientKeepAlivePlayPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof KeepAliveResponseHandler keepAliveResponseHandler))
            throw new IllegalArgumentException("The current session task is must be a keep alive response handler");
        keepAliveResponseHandler.handleKeepAliveResponse(packet.keepAliveIdentifier());
    }
}