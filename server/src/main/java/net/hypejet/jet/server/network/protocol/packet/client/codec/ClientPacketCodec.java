package net.hypejet.jet.server.network.protocol.packet.client.codec;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain ClientPacket client
 * packet}.
 *
 * @param <P> a type of the packet that this codec serializes
 * @since 1.0
 * @author Codestech
 */
public abstract class ClientPacketCodec<P extends ClientPacket> implements NetworkCodec<P> {

    private final int packetId;
    private final Class<P> packetClass;

    /**
     * Constructs a {@linkplain ClientPacketCodec client packet codec}.
     *
     * @param packetId an id of the {@linkplain P packet} that the codec should read
     * @param packetClass a class of the {@linkplain P packet} that the codec should read
     * @since 1.0
     */
    public ClientPacketCodec(int packetId, @NonNull Class<P> packetClass) {
        this.packetId = packetId;
        this.packetClass = packetClass;
    }

    /**
     * Gets an identifier of the {@linkplain P packet} that this codec serializes.
     *
     * @return the identifier
     * @since 1.0
     */
    public int getPacketId() {
        return this.packetId;
    }

    /**
     * Gets a {@linkplain Class class} of the {@linkplain P packet} that this codec serializes.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<P> getPacketClass() {
        return this.packetClass;
    }
}