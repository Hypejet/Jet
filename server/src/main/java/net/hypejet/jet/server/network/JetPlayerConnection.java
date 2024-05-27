package net.hypejet.jet.server.network;

import net.hypejet.jet.player.PlayerConnection;
import net.hypejet.jet.protocol.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a server implementation of {@link PlayerConnection}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetPlayerConnection implements PlayerConnection {

    private int protocolVersion = -1;
    private ProtocolState state = ProtocolState.HANDSHAKE;

    @Override
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public @NonNull ProtocolState getProtocolState() {
        return this.state;
    }

    /**
     * Sets a protocol version of the connection.
     *
     * @param protocolVersion the protocol version
     * @since 1.0
     */
    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
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
}