package net.hypejet.jet.protocol.packet.server.login.plugin;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain ServerPluginMessageRequestPacket plugin message request packet}.
 *
 * @param messageId an identifier of the plugin message, should be unique to the connection
 * @param channel a name of a plugin channel used to send the data
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestPacket
 */
record ServerPluginMessageRequestPacketImpl(int messageId, @NonNull Key channel,
                                            byte @NonNull [] data) implements ServerPluginMessageRequestPacket {
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
     * Represents an implementation of {@linkplain ServerPluginMessageRequestPacket.Builder plugin message request
     * packet builder}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerPluginMessageRequestPacket.Builder
     */
    static final class Builder implements ServerPluginMessageRequestPacket.Builder {

        private int messageId;
        private Key channel = Key.key("jet", "dummy-message");
        private byte[] data = new byte[0];

        @Override
        public ServerPluginMessageRequestPacket.@NonNull Builder messageId(int messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public ServerPluginMessageRequestPacket.@NonNull Builder channel(@NonNull Key channel) {
            this.channel = channel;
            return this;
        }

        @Override
        public ServerPluginMessageRequestPacket.@NonNull Builder data(byte @NonNull [] data) {
            this.data = data;
            return this;
        }

        @Override
        public @NonNull ServerPluginMessageRequestPacket build() {
            return new ServerPluginMessageRequestPacketImpl(this.messageId, this.channel, this.data);
        }
    }
}
