package net.hypejet.jet.protocol.packet.clientbound.login.plugin;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@link ServerPluginRequestPacket plugin request packet}.
 *
 * @param messageId an identifier of a message, should be unique to the connection
 * @param channel a name of a plugin channel used to send the data
 * @param data a data of a message
 * @since 1.0
 * @author Codestech
 * @see ServerPluginRequestPacket
 */
record ServerPluginRequestPacketImpl(int messageId, @NonNull Key channel,
                                     byte @NonNull [] data) implements ServerPluginRequestPacket {
    @Override
    public int getPacketId() {
        return 4;
    }

    @Override
    public @NonNull ProtocolState getProtocolState() {
        return ProtocolState.LOGIN;
    }

    @Override
    public void write(@NonNull NetworkBuffer buffer) {
        buffer.writeVarInt(this.messageId);
        buffer.writeIdentifier(this.channel);
        buffer.writeByteArray(this.data, false);
    }

    /**
     * Represents an implementation of {@link ServerPluginRequestPacket.Builder plugin request packet builder}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerPluginRequestPacket.Builder
     */
    static final class Builder implements ServerPluginRequestPacket.Builder {

        private int messageId;
        private Key channel = Key.key("jet", "dummy-message");
        private byte[] data = new byte[0];

        @Override
        public ServerPluginRequestPacket.@NonNull Builder messageId(int messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public ServerPluginRequestPacket.@NonNull Builder channel(@NonNull Key channel) {
            this.channel = channel;
            return this;
        }

        @Override
        public ServerPluginRequestPacket.@NonNull Builder data(byte @NonNull [] data) {
            this.data = data;
            return this;
        }

        @Override
        public @NonNull ServerPluginRequestPacket build() {
            return new ServerPluginRequestPacketImpl(this.messageId, this.channel, this.data);
        }
    }
}
