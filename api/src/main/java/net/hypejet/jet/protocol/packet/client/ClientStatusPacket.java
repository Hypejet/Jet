package net.hypejet.jet.protocol.packet.client;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet}, which can be received during
 * a {@linkplain net.hypejet.jet.protocol.ProtocolState#STATUS status protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public sealed interface ClientStatusPacket extends ClientPacket permits ClientPingRequestStatusPacket,
        ClientServerListRequestStatusPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.STATUS;
    }
}