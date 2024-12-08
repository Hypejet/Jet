package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which indicates that client has finished a tick.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public record ClientEndTickPlayPacket() implements ClientPacket {}