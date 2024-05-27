package net.hypejet.jet.server.network;

import net.hypejet.jet.protocol.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class NetworkManager {

    private int protocolVersion = -1;
    private ProtocolState state = ProtocolState.HANDSHAKE;

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public @NonNull ProtocolState getState() {
        return this.state;
    }

    public void setState(@NonNull ProtocolState state) {
        this.state = state;
    }
}