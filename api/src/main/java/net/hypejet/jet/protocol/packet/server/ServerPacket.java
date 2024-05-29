package net.hypejet.jet.protocol.packet.server;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.network.NetworkWritable;
import net.hypejet.jet.protocol.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft packet to send from a server to a client.
 *
 * @since 1.0
 * @author Codestech
 */
public interface ServerPacket extends NetworkWritable {
    /**
     * Gets an identifier of the packet.
     *
     * @return the identifier.
     * @since 1.0
     */
    int getPacketId();

    /**
     * Gets a {@linkplain ProtocolState protocol state} of the packet.
     *
     * @return the protocol state.
     * @since 1.0
     */
    @NonNull ProtocolState getProtocolState();

    /**
     * Writes the packet to a {@linkplain NetworkBuffer network buffer}.
     *
     * @param buffer the byte buffer
     * @since 1.0
     */
    @Override
    void write(@NonNull NetworkBuffer buffer);
}