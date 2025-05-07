package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.entity.player.spawn.PlayerSpawnInfo;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ServerPacket a server packet}, which provides information about the server and the initial
 * spawn.
 *
 * @param entityId an entity identifier of the player joining
 * @param hardcore whether the game has hardcore enabled
 * @param worldKeys keys of worlds that are available on the server
 * @param maximumPlayers a maximum amount of players of the server
 * @param maximumViewDistance a maximum distance within players can see world chunks
 * @param simulationDistance a chunk distance within entities are be precessed
 * @param reducedDebugInfo whether information displayed on the debug screen of client should be reduced
 * @param enableRespawnScreen whether the respawn screen is enabled
 * @param showUnlockedRecipesOnly whether players can only see recipes that they unlocked
 * @param spawnInfo an information about the initial spawn
 * @param enforcesSecureChat whether the server enforces players to use secure chat
 * @since 1.0
 * @see ServerPacket
 */
public record ServerJoinGamePlayPacket(
        int entityId, boolean hardcore, @NonNull Collection<Key> worldKeys, int maximumPlayers,
        int maximumViewDistance, int simulationDistance, boolean reducedDebugInfo,
        boolean enableRespawnScreen, boolean showUnlockedRecipesOnly, @NonNull PlayerSpawnInfo spawnInfo,
        boolean enforcesSecureChat
) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerJoinGamePlayPacket server join game play packet}.
     *
     * @param entityId an entity identifier of the player joining
     * @param hardcore whether the game has hardcore enabled
     * @param worldKeys keys of worlds that are available on the server
     * @param maximumPlayers a maximum amount of players of the server
     * @param maximumViewDistance a maximum distance within players can see world chunks
     * @param simulationDistance a chunk distance within entities are be precessed
     * @param reducedDebugInfo whether information displayed on the debug screen of client should be reduced
     * @param enableRespawnScreen whether the respawn screen is enabled
     * @param showUnlockedRecipesOnly whether players can only see recipes that they unlocked
     * @param spawnInfo an information about the initial spawn
     * @param enforcesSecureChat whether the server enforces players to use secure chat
     * @since 1.0
     */
    public ServerJoinGamePlayPacket {
        worldKeys = Set.copyOf(NullabilityUtil.requireNonNull(worldKeys, "world keys"));
        NullabilityUtil.requireNonNull(spawnInfo, "spawn info");
    }
}