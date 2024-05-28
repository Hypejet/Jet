package net.hypejet.jet.server.player;

import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.player.PlayerConnection;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import net.hypejet.jet.protocol.packet.clientbound.login.disconnect.DisconnectPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a server implementation of {@link PlayerConnection}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetPlayerConnection implements PlayerConnection {

    private ProtocolState state = ProtocolState.HANDSHAKE;

    private final SocketChannel channel;

    public JetPlayerConnection(@NonNull SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public @NonNull ProtocolState getProtocolState() {
        return this.state;
    }

    @Override
    public void sendPacket(@NonNull ClientBoundPacket packet) {
        if (this.state != packet.getProtocolState())
            throw unsupportedProtocolState(packet);
        this.channel.writeAndFlush(packet);
    }

    @Override
    public void kick(@NonNull Component reason) {
        this.sendPacket(DisconnectPacket.builder()
                .reason(reason)
                .build());
        this.close();
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
     * Gets a {@link SocketChannel socket channel} of the {@link PlayerConnection player connection}.
     *
     * @return the socket channel
     * @since 1.0
     */
    public @NonNull SocketChannel getChannel() {
        return this.channel;
    }

    private static @NonNull RuntimeException unsupportedProtocolState(@NonNull ClientBoundPacket packet) {
        return new IllegalArgumentException("The current protocol state does not support packet "
                + packet.getClass().getSimpleName());
    }
}