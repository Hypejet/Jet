package net.hypejet.jet.server.player;

import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.player.PlayerConnection;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.login.compression.ServerEnableCompressionPacket;
import net.hypejet.jet.protocol.packet.server.login.disconnect.ServerDisconnectPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@link PlayerConnection}, which is handled by netty's
 * {@link SocketChannel socket channel}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class SocketPlayerConnection implements PlayerConnection {

    private final SocketChannel channel;

    private ProtocolState state = ProtocolState.HANDSHAKE;
    private int compressionThreshold = -1;

    /**
     * Constructs a {@link SocketPlayerConnection socket player connection}.
     *
     * @param channel a {@link SocketChannel socket channel}, which handles the connection
     * @since 1.0
     */
    public SocketPlayerConnection(@NonNull SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public @NonNull ProtocolState getProtocolState() {
        return this.state;
    }

    @Override
    public void sendPacket(@NonNull ServerPacket packet) {
        this.channel.writeAndFlush(packet);
    }

    @Override
    public void kick(@NonNull Component reason) {
        this.sendPacket(ServerDisconnectPacket.builder()
                .reason(reason)
                .build());
        this.close();
    }

    @Override
    public int compressionThreshold() {
        return this.compressionThreshold;
    }

    /**
     * Closes the {@link PlayerConnection player connection}.
     *
     * @since 1.0
     */
    public void close() {
        try {
            this.channel.close().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a protocol state of this connection.
     *
     * @param state the protocol state
     * @since 1.0
     */
    public void setProtocolState(@NonNull ProtocolState state) {
        this.state = state;
    }

    /**
     * Sets a compression threshold of this connection.
     *
     * @param compressionThreshold the compression threshold
     * @since 1.0
     */
    public void setCompressionThreshold(int compressionThreshold) {
        this.sendPacket(ServerEnableCompressionPacket.builder()
                .threshold(compressionThreshold)
                .build());
        this.compressionThreshold = compressionThreshold;
    }

    /**
     * Gets a {@link SocketChannel socket channel} of the {@link PlayerConnection player connection}.
     *
     * @return the socket channel
     * @since 1.0
     */
    public @NonNull SocketChannel getChannel() {
        return this.channel;
    }
}