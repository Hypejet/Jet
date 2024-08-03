package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.coordinate.BlockPosition;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which is sent to a {@linkplain Player player}.
 *
 * @param entityId an entity identifier of the player
 * @param hardcore whether the game has hardcore enable
 * @param dimensions identifiers of all dimensions on the server
 * @param maxPlayers a maximum amount of players of the server, unused by the client since Minecraft 1.8
 * @param viewDistance a render distance of the player
 * @param simulationDistance a distance within the player will process specific things, such as entities
 * @param reducedDebugInfo whether the reduced debug screen info should be shown for the player
 * @param enableRespawnScreen whether the respawn screen is enabled
 * @param limitedCrafting whether players can only recipes that they unlocked
 * @param dimensionType a numeric identifier from a registry of the dimension that the player is being spawned into
 * @param dimensionName an identifier of the dimension that the player is being spawned into
 * @param hashedSeed first 8 bytes of the SHA-256 hash of the seed of a world that the player is being spawned into
 * @param gameMode an initial game mode of the player
 * @param previousGameMode a previous game mode of the player, {@code null} if the game mode was not set for the player
 *                         before
 * @param debug whether the world is in the debug mode
 * @param flat whether the world is a super-flat world
 * @param deathLocation a death location of the player, {@code null} if the player did not die
 * @param portalCooldown a number of ticks until the player can use portal again
 * @param enforcesSecureChat whether the server enforces the player to use secure chat
 * @since 1.0
 * @author Codestech
 * @see ServerPlayPacket
 */
public record ServerJoinGamePlayPacket(int entityId, boolean hardcore, @NonNull Collection<Key> dimensions,
                                       int maxPlayers, int viewDistance, int simulationDistance,
                                       boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
                                       int dimensionType, @NonNull Key dimensionName, long hashedSeed,
                                       Player.@NonNull GameMode gameMode, Player.@Nullable GameMode previousGameMode,
                                       boolean debug, boolean flat, @Nullable DeathLocation deathLocation,
                                       int portalCooldown, boolean enforcesSecureChat) implements ServerPlayPacket {
    /**
     * Constructs the {@linkplain ServerJoinGamePlayPacket join game play packet}.
     *
     * @param entityId an entity identifier of the player
     * @param hardcore whether the game has hardcore enable
     * @param dimensions identifiers of all dimensions on the server
     * @param maxPlayers a maximum amount of players of the server, unused by the client since Minecraft 1.8
     * @param viewDistance a render distance of the player
     * @param simulationDistance a distance within the player will process specific things, such as entities
     * @param reducedDebugInfo whether the reduced debug screen info should be shown for the player
     * @param enableRespawnScreen whether the respawn screen is enabled
     * @param limitedCrafting whether players can only recipes that they unlocked
     * @param dimensionType a numeric identifier from a registry of the dimension that the player is being spawned into
     * @param dimensionName an identifier of the dimension that the player is being spawned into
     * @param hashedSeed first 8 bytes of the SHA-256 hash of the seed of a world that the player is being spawned into
     * @param gameMode an initial game mode of the player
     * @param previousGameMode a previous game mode of the player, {@code null} if the game mode was not set for the
     *                         player before
     * @param debug whether the world is in the debug mode
     * @param flat whether the world is a super-flat world
     * @param deathLocation a death location of the player, {@code null} if the player did not die
     * @param portalCooldown a number of ticks until the player can use portal again
     * @param enforcesSecureChat whether the server enforces the player to use secure chat
     * @since 1.0
     */
    public ServerJoinGamePlayPacket {
        dimensions = Set.copyOf(dimensions);
    }

    /**
     * Represents a death location of a {@linkplain Player player}.
     *
     * @param deathDimensionName an identifier of the dimension that the player died in
     * @param deathPosition a position where the player died at
     * @since 1.0
     * @author Codestech
     */
    public record DeathLocation(@NonNull Key deathDimensionName, @NonNull BlockPosition deathPosition) {}
}