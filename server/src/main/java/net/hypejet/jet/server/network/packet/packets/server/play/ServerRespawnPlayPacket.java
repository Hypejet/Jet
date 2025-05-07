package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.entity.player.spawn.PlayerSpawnInfo;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which is sent to a client to spawn
 * {@linkplain net.hypejet.jet.entity.player.Player a player} again on a client after death or change their dimension.
 *
 * @param spawnInfo an information of the spawn that should occur
 * @param keepAttributes whether attributes of the player should be kept clientside
 * @param keepMetadata whether metadata of the player should be kept clientside
 * @since 1.0
 * @see ServerPacket
 */
public record ServerRespawnPlayPacket(@NonNull PlayerSpawnInfo spawnInfo, boolean keepAttributes,
                                      boolean keepMetadata) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerRespawnPlayPacket server respawn play packet}.
     *
     * @param spawnInfo an information of the spawn that should occur
     * @param keepAttributes whether attributes of the player should be kept clientside
     * @param keepMetadata whether metadata of the player should be kept clientside
     * @since 1.0
     */
    public ServerRespawnPlayPacket {
        NullabilityUtil.requireNonNull(spawnInfo, "spawn info");
    }
}