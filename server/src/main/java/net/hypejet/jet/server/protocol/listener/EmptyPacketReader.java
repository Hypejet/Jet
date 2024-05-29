package net.hypejet.jet.server.protocol.listener;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Supplier;

/**
 * Represents a {@link PacketReader packet reader}, which reads all Minecraft packets without fields specified.
 *
 * @param <P> the type of packet, which this reader reads
 * @since 1.0
 * @author Codestech
 */
public abstract class EmptyPacketReader<P extends ClientPacket> extends PacketReader<P> {

    private final Supplier<P> packetSupplier;

    /**
     * Constructs a {@link EmptyPacketReader empty packet reader}.
     *
     * @param packetId an identifier of {@link P a packet}, which this reader reads
     * @param protocolState a protocol state of {@link P a packet}, which this reader reads
     * @param packetSupplier a supplier that creates a packet instance
     * @since 1.0
     */
    public EmptyPacketReader(int packetId, @NonNull ProtocolState protocolState, @NonNull Supplier<P> packetSupplier) {
        super(packetId, protocolState);
        this.packetSupplier = packetSupplier;
    }

    @Override
    public @NonNull P read(@NonNull NetworkBuffer buffer) {
        return this.packetSupplier.get();
    }
}