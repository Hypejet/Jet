package net.hypejet.jet.server.network.netty.reader;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a holder of {@linkplain ClientPacket a client packet}, which has been already read, but not handled.
 *
 * @param packet the client packet
 * @param handler the handler that the packet was read with and should be handled with
 * @param <P> a type of the client packet
 * @since 1.0
 */
public record UnhandledPacket<P extends ClientPacket>(@NonNull P packet, @NonNull ClientPacketHandler<P> handler) {
    /**
     * Constructs the {@linkplain UnhandledPacket unhandled packet}.
     *
     * @param packet the client packet
     * @param handler the handler that the packet was read with and should be handled with
     * @since 1.0
     */
    public UnhandledPacket {
        NullabilityUtil.requireNonNull(packet, "packet");
        NullabilityUtil.requireNonNull(handler, "handler");
    }
}