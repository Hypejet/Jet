package net.hypejet.jet.protocol.packet;

import net.hypejet.jet.protocol.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents any Minecraft packet.
 *
 * @since 1.0
 * @author Window5
 * @author Codestech
 */
public interface Packet {
    /**
     * Gets a {@linkplain ProtocolState protocol state}, during which this packet can be handled.
     *
     * @return the protocol state
     * @since 1.0
     */
    @NonNull ProtocolState state();
}