package net.hypejet.jet.server.entity.player.spawn;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.world.data.WorldData;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a spawn info of {@linkplain Player a player}.
 *
 * @param dimensionTypeIdentifier an identifier of a dimension type of world where the player should spawn
 * @param dimensionTypeKey a key of a dimension type of world where the player should spawn
 * @param worldData an additional data of a world where the player should spawn
 * @param gameMode an initial game mode that the player should have
 * @param previousGameMode a previous game mode that the player had before spawning, {@code null} if the player
 *                         is spawning first time
 * @param lastDeathLocation a location where the player died before spawning, {@code null} if the player did not die
 * @param portalCooldown a remaining number of ticks of cooldown for using portals of the player
 * @since 1.0
 */
public record PlayerSpawnInfo(int dimensionTypeIdentifier, @NonNull Key dimensionTypeKey, @NonNull WorldData worldData,
                              Player.@NonNull GameMode gameMode, Player.@Nullable GameMode previousGameMode,
                              @Nullable DeathLocation lastDeathLocation, int portalCooldown) {
    /**
     * Constructs the {@linkplain PlayerSpawnInfo player spawn info}.
     *
     * @param dimensionTypeIdentifier an identifier of a dimension type of world where the player should spawn
     * @param dimensionTypeKey a key of a dimension type of world where the player should spawn
     * @param worldData an additional data of a world where the player should spawn
     * @param gameMode an initial game mode that the player should have
     * @param previousGameMode a previous game mode that the player had before spawning, {@code null} if the player
     *                         is spawning first time
     * @param lastDeathLocation a location where the player died before spawning, {@code null} if the player did not die
     * @param portalCooldown a remaining number of ticks of cooldown for using portals of the player
     * @since 1.0
     */
    public PlayerSpawnInfo {
        NullabilityUtil.requireNonNull(dimensionTypeKey, "dimension type key");
        NullabilityUtil.requireNonNull(worldData, "world data");
        NullabilityUtil.requireNonNull(gameMode, "game mode");
    }
}