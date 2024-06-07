package net.hypejet.jet.protocol.packet.server;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.protocol.packet.server.status.ServerPingResponseStatusPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet}, which can be sent during a {@linkplain ProtocolState#STATUS
 * status protocol state} only.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerStatusPacket extends ServerPacket permits ServerListResponseStatusPacket,
        ServerPingResponseStatusPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.STATUS;
    }
}