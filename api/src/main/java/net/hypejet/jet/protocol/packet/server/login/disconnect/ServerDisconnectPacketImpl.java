package net.hypejet.jet.protocol.packet.server.login.disconnect;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain ServerDisconnectPacket disconnect packet}.
 *
 * @param reason a reason of the disconnection
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectPacket
 */
record ServerDisconnectPacketImpl(@NonNull Component reason) implements ServerDisconnectPacket {
    @Override
    public void write(@NonNull NetworkBuffer buffer) {
        buffer.writeJsonTextComponent(this.reason);
    }

    @Override
    public int getPacketId() {
        return 0;
    }

    @Override
    public @NonNull ProtocolState getProtocolState() {
        return ProtocolState.LOGIN;
    }

    /**
     * Represents an implementation of {@linkplain ServerDisconnectPacket.Builder disconnect packet builder}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerDisconnectPacket.Builder
     */
    static final class Builder implements ServerDisconnectPacket.Builder {

        private Component reason = Component.empty();

        @Override
        public ServerDisconnectPacket.@NonNull Builder reason(@NonNull Component reason) {
            this.reason = reason;
            return this;
        }

        @Override
        public @NonNull ServerDisconnectPacket build() {
            return new ServerDisconnectPacketImpl(this.reason);
        }
    }
}