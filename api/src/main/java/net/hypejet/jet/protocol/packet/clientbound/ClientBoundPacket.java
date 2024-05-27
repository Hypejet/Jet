package net.hypejet.jet.protocol.packet.clientbound;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft packet to send from a server to a client.
 *
 * @since 1.0
 * @author Codestech
 */
public abstract class ClientBoundPacket {

    private final int packetId;
    private final ProtocolState protocolState;

    /**
     * Constructs a {@link ClientBoundPacket client-bound packet}.
     *
     * @param packetId an id of the packet
     * @param protocolState a protocol state of the packet
     * @since 1.0
     */
    public ClientBoundPacket(int packetId, @NonNull ProtocolState protocolState) {
        this.packetId = packetId;
        this.protocolState = protocolState;
    }

    /**
     * Gets an identifier of the packet.
     *
     * @return the identifier.
     * @since 1.0
     */
    public final int getPacketId() {
        return this.packetId;
    }

    /**
     * Gets a {@link ProtocolState protocol state} of the packet.
     *
     * @return the protocol state.
     * @since 1.0
     */
    public final @NonNull ProtocolState getProtocolState() {
        return this.protocolState;
    }

    /**
     * Writes the packet to a {@link NetworkBuffer network buffer}.
     *
     * @param buffer the byte buffer
     * @since 1.0
     */
    public abstract void write(@NonNull NetworkBuffer buffer);
}