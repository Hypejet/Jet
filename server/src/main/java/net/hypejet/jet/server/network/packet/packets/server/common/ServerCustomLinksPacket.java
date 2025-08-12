package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.game.link.ServerLink;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet}, which changes server links displayed on a client in a pause
 * menu.
 *
 * @param serverLinks the server links
 * @since 1.0
 * @see ServerLink
 * @see ServerPacket
 */
public record ServerCustomLinksPacket(@NonNull Collection<ServerLink> serverLinks) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerCustomLinksPacket server custom links packet}.
     *
     * @param serverLinks the server links
     * @since 1.0
     */
    public ServerCustomLinksPacket {
        serverLinks = List.copyOf(Objects.requireNonNull(serverLinks, "server links"));
    }
}