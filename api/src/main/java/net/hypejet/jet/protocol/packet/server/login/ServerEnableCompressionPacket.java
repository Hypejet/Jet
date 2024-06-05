package net.hypejet.jet.protocol.packet.server.login;

import net.hypejet.jet.protocol.packet.server.ServerLoginPacket;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet}, which is sent to enable a packet compression.
 *
 * @param threshold a minimum size of a packet, from which it gets compressed
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public record ServerEnableCompressionPacket(int threshold) implements ServerLoginPacket {}