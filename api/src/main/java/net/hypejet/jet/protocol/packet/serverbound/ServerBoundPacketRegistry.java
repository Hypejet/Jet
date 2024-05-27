package net.hypejet.jet.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a registry of a server-bound packet readers.
 *
 * @since 1.0
 * @author Codestech
 */
public interface ServerBoundPacketRegistry {
    /**
     * Gets a {@link PacketReader packet reader}, which can read a specific packet  and reads it from
     * a {@link ByteBuf byte buffer}.
     *
     * @param packetId an id of the packet
     * @param state a current state of the protocol
     * @param buffer the byte buffer
     * @return the packet, which may be null if the packet reader is not present
     * @since 1.0
     */
    default @Nullable ServerBoundPacket read(int packetId, @NonNull ProtocolState state, @NonNull ByteBuf buffer) {
        PacketReader<?> packetReader = getReader(packetId, state);
        if (packetReader == null) return null;
        return packetReader.read(buffer);
    }

    /**
     * Gets a {@link PacketReader packet reader}, which can read a specific packet.
     *
     * @param packetId an id of the packet
     * @param state a current state of the protocol
     * @return the packet reader, which may be null if not present
     * @since 1.0
     */
    @Nullable PacketReader<?> getReader(int packetId, @NonNull ProtocolState state);
}