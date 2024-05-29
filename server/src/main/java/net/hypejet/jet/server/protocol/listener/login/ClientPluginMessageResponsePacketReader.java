package net.hypejet.jet.server.protocol.listener.login;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponsePacket;
import net.hypejet.jet.server.protocol.listener.PacketReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketReader packet reader} for a {@linkplain ClientPluginMessageResponsePacket plugin
 * message response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessageResponsePacket
 * @see PacketReader
 */
public final class ClientPluginMessageResponsePacketReader extends PacketReader<ClientPluginMessageResponsePacket> {

    /**
     * Constructs a {@link ClientPluginMessageResponsePacketReader plugin message response packet reader}.
     *
     * @since 1.0
     */
    public ClientPluginMessageResponsePacketReader() {
        super(2, ProtocolState.LOGIN);
    }

    @Override
    public @NonNull ClientPluginMessageResponsePacket read(@NonNull NetworkBuffer buffer) {
        return new ClientPluginMessageResponsePacketImpl(
                buffer.readVarInt(),
                buffer.readBoolean(),
                buffer.readByteArray(false)
        );
    }

    /**
     * Represents an implementation of {@linkplain ClientPluginMessageResponsePacket plugin message response packet}.
     *
     * @param messageId an id of the plugin message
     * @param successful whether a client understood the plugin message
     * @param data an optional response data sent by a client
     * @since 1.0
     * @author Codestech
     * @see ClientPluginMessageResponsePacket
     */
    private record ClientPluginMessageResponsePacketImpl(int messageId, boolean successful, byte @NonNull [] data)
            implements ClientPluginMessageResponsePacket {}
}