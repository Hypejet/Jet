package net.hypejet.jet.server.network.protocol.packet.client.codec;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads a {@linkplain ClientPacket client packet}
 * and additionally handles it.
 *
 * @param <P> the type of the packet
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 * @see PacketCodec
 */
public abstract class ClientPacketCodec<P extends ClientPacket> extends PacketCodec<P> {
    /**
     * Constructs the {@linkplain ClientPacketCodec client paket codec}.
     *
     * @param packetId an identifier of the packet that this codec serializes
     * @param packetClass a {@linkplain Class class} of the packet that this codec serializes
     * @since 1.0
     */
    protected ClientPacketCodec(int packetId, @NonNull Class<P> packetClass) {
        super(packetId, packetClass);
    }

    /**
     * Handles a {@linkplain P packet}.
     *
     * @param packet the packet
     * @param connection a connection, from which the packet was received
     * @since 1.0
     */
    public abstract void handle(@NonNull P packet, @NonNull SocketPlayerConnection connection);
}