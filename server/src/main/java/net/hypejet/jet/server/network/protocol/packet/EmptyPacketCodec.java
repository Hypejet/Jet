package net.hypejet.jet.server.network.protocol.packet;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.Packet;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which does not process any packet data.
 *
 * @param <P> the type of the packet
 * @since 1.0
 * @author Codestech
 * @see PacketCodec
 */
public final class EmptyPacketCodec<P extends Packet> extends PacketCodec<P> {

    private final P packet;

    /**
     * Constructs the {@linkplain EmptyPacketCodec empty packet codec}.
     *
     * @param packetId an identifier of the packet
     * @param packetClass a class of the packet
     * @param packet an instance of the packet, which is always returned in {@link #read(ByteBuf)}
     * @since 1.0
     */
    public EmptyPacketCodec(int packetId, @NonNull Class<P> packetClass, @NonNull P packet) {
        super(packetId, packetClass);
        this.packet = packet;
    }

    @Override
    public @NonNull P read(@NonNull ByteBuf buf) {
        return this.packet;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull P object) {
        // NOOP
    }
}