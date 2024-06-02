package net.hypejet.jet.protocol.packet.server;

import net.hypejet.jet.protocol.packet.server.login.ServerLoginPacket;

/**
 * Represents a Minecraft packet to send from a server to a client.
 *
 * @since 1.0
 * @author Codestech
 */
public sealed interface ServerPacket permits ServerLoginPacket {}