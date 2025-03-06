package net.hypejet.jet.server.util.game.audience;

import net.hypejet.jet.server.network.packet.packets.server.common.ServerCookieRequestPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCustomLinksPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCustomReportDetailsPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPingPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPluginMessagePacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerStoreCookiePacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerTransferPacket;
import net.hypejet.jet.server.network.session.pack.ResourcePackHandler;
import net.hypejet.jet.util.game.audience.CommonAudience;
import net.hypejet.jet.util.game.crash.CrashReportDetails;
import net.hypejet.jet.util.game.link.ServerLink;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.resource.ResourcePackRequest;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * Represents a combination of {@linkplain CommonAudience a common audience}
 * and {@linkplain PacketReceivingAudience a packet receiving audience}.
 *
 * @since 1.0
 * @see PacketReceivingAudience
 * @see CommonAudience
 */
public interface PacketReceivingCommonAudience extends CommonAudience, PacketReceivingAudience {
    @Override
    default void setCustomLinks(@NonNull Collection<ServerLink> links) {
        this.sendPacket(new ServerCustomLinksPacket(links));
    }

    @Override
    default void setCustomReportDetails(@NonNull Collection<CrashReportDetails> details) {
        this.sendPacket(new ServerCustomReportDetailsPacket(details));
    }

    @Override
    default void ping(int identifier) {
        this.sendPacket(new ServerPingPacket(identifier));
    }

    @Override
    default void sendPluginMessage(@NonNull Key key, byte @NonNull [] data) {
        this.sendPacket(new ServerPluginMessagePacket(key, data));
    }

    @Override
    default void requestCookie(@NonNull Key key) {
        this.sendPacket(new ServerCookieRequestPacket(key));
    }

    @Override
    default void storeCookie(@NonNull Key key, byte @NonNull [] data) {
        this.sendPacket(new ServerStoreCookiePacket(key, data));
    }

    @Override
    default void transfer(@NonNull String address, int port) {
        this.sendPacket(new ServerTransferPacket(address, port));
    }

    @Override
    default void sendResourcePacks(@NotNull ResourcePackRequest request) {
        this.resourcePackHandler().request(request);
    }

    @Override
    default void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
        HashSet<UUID> ids = new HashSet<>(Arrays.asList(others));
        ids.add(id);
        this.resourcePackHandler().remove(ids);
    }

    @Override
    default void clearResourcePacks() {
        this.resourcePackHandler().clear();
    }

    /**
     * Gets {@linkplain ResourcePackHandler a resource pack handler} that should be used for resource pack management
     * of this audience.
     *
     * @return the resource pack handler
     * @since 1.0
     */
    @NonNull ResourcePackHandler resourcePackHandler();
}