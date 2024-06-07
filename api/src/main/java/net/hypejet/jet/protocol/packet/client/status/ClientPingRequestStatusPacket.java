package net.hypejet.jet.protocol.packet.client.status;

import net.hypejet.jet.protocol.packet.client.ClientStatusPacket;

/**
 * Represents a {@linkplain ClientStatusPacket client status packet}, which is sent by a client when it wants
 * to calculate a ping during a {@linkplain net.hypejet.jet.protocol.ProtocolState#STATUS status protocol state}.
 *
 * @param payload a number, may be anything, vanilla clients use a system-dependent time value counted in milliseconds
 * @since 1.0
 * @author Codestech
 * @see ClientStatusPacket
 */
public record ClientPingRequestStatusPacket(long payload) implements ClientStatusPacket {}