package net.hypejet.jet.server.network.protocol.writer;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that writes a {@linkplain  P server packet} to a {@linkplain NetworkBuffer network buffer}.
 *
 * @param <P> a type of the packet
 * @since 1.0
 * @author Codestech
 */
public abstract class PacketWriter<P extends ServerPacket> {

    private final int packetId;
    private final ProtocolState state;
    private final Class<P> clazz;

    /**
     * Constructs a {@linkplain PacketWriter packet writer}.
     *
     * @param packetId an id of a {@linkplain P packet} that this writer writes
     * @param state a {@linkplain ProtocolState protocol state} of a {@linkplain P packet} that this writer writes
     * @param clazz a class of a {@linkplain P packet} that this writer writes
     * @since 1.0
     */
    public PacketWriter(int packetId, @NonNull ProtocolState state, @NonNull Class<P> clazz) {
        this.packetId = packetId;
        this.state = state;
        this.clazz = clazz;
    }

    /**
     * Gets an identifier of a {@linkplain P packet} that this writer writes.
     *
     * @return the identifier
     * @since 1.0
     */
    public final int getPacketId() {
        return this.packetId;
    }

    /**
     * Gets a {@linkplain ProtocolState protocol state} of a {@linkplain P packet} that his writer writes.
     *
     * @return the protocol state
     * @since 1.0
     */
    public final @NonNull ProtocolState getState() {
        return this.state;
    }

    /**
     * Gets a class of a {@linkplain P packet} that this writer writes.
     *
     * @return the class
     * @since 1.0
     */
    public final @NonNull Class<P> getPacketClass() {
        return this.clazz;
    }

    /**
     * Writes a {@linkplain P packet} to a {@linkplain NetworkBuffer network buffer}.
     *
     * @param packet the packet
     * @param buffer the network buffer
     * @since 1.0
     */
    public abstract void write(@NonNull P packet, @NonNull NetworkBuffer buffer);
}