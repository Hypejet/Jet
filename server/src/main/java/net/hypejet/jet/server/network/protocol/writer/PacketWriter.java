package net.hypejet.jet.server.network.protocol.writer;

import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes
 * {@linkplain ServerPacket server packets}.
 *
 * @param <P> the type of the packet that this writer serializes
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see NetworkCodec
 */
public abstract class PacketWriter<P extends ServerPacket> implements NetworkCodec<P> {

    private final int packetId;
    private final Class<P> packetClass;

    /**
     * Constructs a {@linkplain PacketWriter packet writer}.
     *
     * @param packetId an identifier of the packet that this writer serializes
     * @param packetClass a {@linkplain Class class} of the packet that this writer serializes
     * @since 1.0
     */
    protected PacketWriter(int packetId, @NonNull Class<P> packetClass) {
        this.packetId = packetId;
        this.packetClass = packetClass;
    }

    /**
     * Gets an identifier of a {@linkplain ServerPacket server packet} that this writer serializes.
     *
     * @return the identifier
     * @since 1.0
     */
    public int getPacketId() {
        return this.packetId;
    }

    /**
     * Gets a {@linkplain Class class} of a packet that this writer serializes.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<P> getPacketClass() {
        return this.packetClass;
    }
}