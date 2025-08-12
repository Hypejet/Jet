package net.hypejet.jet.server.network.packet.packets.server.status;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.game.ping.ServerListPing;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet}, which sends a server list response data requested by a client.
 *
 * @param ping the response data
 * @since 1.0
 * @see ServerPacket
 */
public record ServerListResponseStatusPacket(@NonNull ServerListPing ping) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerListResponseStatusPacket server list response status packet}.
     *
     * @param ping the response data
     * @since 1.0
     */
    public ServerListResponseStatusPacket {
        Objects.requireNonNull(ping, "ping");
    }
}