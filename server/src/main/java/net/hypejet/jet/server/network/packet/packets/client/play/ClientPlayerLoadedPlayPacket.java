package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.server.world.JetWorld;

/**
 * A {@linkplain ClientPacket client packet} sent by the client when the world loading screen closes
 * after initial {@linkplain JetWorld world} join or {@linkplain JetWorld world} switch.
 *
 * @since 1.0
 * @see JetWorld
 * @see ClientPacket
 */
public record ClientPlayerLoadedPlayPacket() implements ClientPacket {}