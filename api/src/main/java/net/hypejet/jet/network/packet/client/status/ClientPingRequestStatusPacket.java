package net.hypejet.jet.network.packet.client.status;

import net.hypejet.jet.network.packet.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client when it wants
 * to calculate a ping during a status protocol state.
 *
 * @param payload a number, may be anything, vanilla clients use a system-dependent time value counted in milliseconds
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.network.ProtocolState#STATUS
 * @see ClientPacket
 */
public record ClientPingRequestStatusPacket(long payload) implements ClientPacket {}