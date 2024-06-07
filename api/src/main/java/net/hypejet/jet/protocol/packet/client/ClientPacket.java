package net.hypejet.jet.protocol.packet.client;

import net.hypejet.jet.protocol.packet.Packet;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;

/**
 * Represents a Minecraft packet that has been sent by a client to a server.
 *
 * @since 1.0
 * @author Codestech
 */
public sealed interface ClientPacket extends Packet permits ClientLoginPacket, ClientStatusPacket,
        ClientHandshakePacket {}