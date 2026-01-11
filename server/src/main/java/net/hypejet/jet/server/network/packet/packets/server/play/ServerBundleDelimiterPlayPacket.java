package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * A {@linkplain ServerPacket server packet} indicating that the following packets are sent in one bundle
 * and should be handled by the client in the same tick. This packet also indicates and end of a packet bundle.
 *
 * @since 1.0
 * @see ServerPacket
 */
public record ServerBundleDelimiterPlayPacket() implements ServerPacket {}