package net.hypejet.jet.server.network.protocol.listener;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a reader of {@link P server-bound packet}.
 *
 * @since 1.0
 * @author Codestech
 */
public abstract class PacketReader<P extends ClientPacket> {

    private final int packetId;
    private final ProtocolState protocolState;

    /**
     * Constructs a {@link PacketReader packet reader}.
     *
     * @param packetId an identifier of {@link P a packet}, which this reader reads
     * @param protocolState a protocol state of {@link P a packet}, which this reader reads
     * @since 1.0
     */
    protected PacketReader(int packetId, @NonNull ProtocolState protocolState) {
        this.packetId = packetId;
        this.protocolState = protocolState;
    }

    /**
     * Gets an identifier of {@link P a packet}, which this reader reads.
     *
     * @return the identifier
     * @since 1.0
     */
    public final int getPacketId() {
        return this.packetId;
    }

    /**
     * Gets a protocol state of {@link P a packet}, which this reader reads.
     *
     * @return the protocol state
     * @since 1.0
     */
    public final @NonNull ProtocolState getProtocolState() {
        return this.protocolState;
    }

    /**
     * Reads a {@link P packet} from a {@link NetworkBuffer network buffer}
     *
     * @param buffer the byte buffer
     * @return the {@link P packet}
     */
    public abstract @NonNull P read(@NonNull NetworkBuffer buffer);
}