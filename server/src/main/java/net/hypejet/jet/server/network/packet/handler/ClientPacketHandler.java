package net.hypejet.jet.server.network.packet.handler;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that handles {@linkplain ClientPacket a client packet}.
 *
 * @param <P> a type of the client packet
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public abstract class ClientPacketHandler<P extends ClientPacket> {

    private final Class<P> packetClass;

    /**
     * Constructs the {@linkplain ClientPacketHandler client packet handler}.
     *
     * @param packetClass a class of the client packet
     * @since 1.0
     */
    protected ClientPacketHandler(@NonNull Class<P> packetClass) {
        this.packetClass = NullabilityUtil.requireNonNull(packetClass, "packet class");
    }

    /**
     * Gets a class of the client packet that this handler handles.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<P> packetClass() {
        return this.packetClass;
    }

    /**
     * Handles the packet.
     *
     * @param packet the packet
     * @param session a session, during which the packet is handled
     * @since 1.0
     */
    public abstract void handle(@NonNull P packet, @NonNull Session session);
}