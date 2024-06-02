package net.hypejet.jet.protocol.packet;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.protocol.packet.server.ServerPacket;

/**
 * Represents any Minecraft packet.
 *
 * @since 1.0
 * @author Window5
 * @author Codestech
 */
public sealed interface Packet permits ClientPacket, ServerPacket {}