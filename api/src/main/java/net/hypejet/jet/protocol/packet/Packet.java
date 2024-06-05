package net.hypejet.jet.protocol.packet;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents any Minecraft packet.
 *
 * @since 1.0
 * @author Window5
 * @author Codestech
 */
public sealed interface Packet permits ClientPacket, ServerPacket {
    /**
     * Gets a {@linkplain ProtocolState protocol state}, during which this packet can be handled.
     *
     * @return the protocol state
     * @since 1.0
     */
    @NonNull ProtocolState state();
}