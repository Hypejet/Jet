package net.hypejet.jet.protocol.packet.clientbound.login.disconnect;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@link DisconnectPacket disconnect packet}.
 *
 * @param reason a reason of the disconnection
 * @since 1.0
 * @author Codestech
 * @see DisconnectPacket
 */
record DisconnectPacketImpl(@NonNull Component reason) implements DisconnectPacket {
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
     * Represents an implementation of {@link DisconnectPacket.Builder disconnect packet builder}.
     *
     * @since 1.0
     * @author Codestech
     * @see DisconnectPacket.Builder
     */
    static final class Builder implements DisconnectPacket.Builder {

        private Component reason = Component.empty();

        @Override
        public DisconnectPacket.@NonNull Builder reason(@NonNull Component reason) {
            this.reason = reason;
            return this;
        }

        @Override
        public @NonNull DisconnectPacket build() {
            return new DisconnectPacketImpl(this.reason);
        }
    }
}