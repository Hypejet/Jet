package net.hypejet.jet.network.packet.server.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.link.ServerLink;
import net.hypejet.jet.network.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Represents {@linkplain ServerPacket a server packet}, which changes server links displayed on a client
 * in a pause menu.
 *
 * @param serverLinks the server links
 * @since 1.0
 * @author Codestech
 * @see ServerLink
 * @see ServerPacket
 */
public record ServerCustomLinksPacket(@NonNull Collection<ServerLink> serverLinks) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerCustomLinksPacket server cuistom links packet}.
     *
     * @param serverLinks the server links
     * @since 1.0
     */
    public ServerCustomLinksPacket {
        serverLinks = List.copyOf(NullabilityUtil.requireNonNull(serverLinks, "server links"));
    }
}