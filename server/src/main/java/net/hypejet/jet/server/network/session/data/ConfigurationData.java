package net.hypejet.jet.server.network.session.data;

import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.world.JetWorld;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a data specified
 * in {@linkplain net.hypejet.jet.server.network.session.task.ConfigurationSessionTask a configuration session task}
 * that {@linkplain net.hypejet.jet.server.entity.player.JetPlayer a player} that is going to be associated with it
 * should have.
 *
 * @param loginData a login data of the player
 * @param world a world that the player should initially spawn in
 * @param position a position that the player should initially spawn at
 * @param enableRespawnScreen whether a respawn screen should be enabled for the player
 * @param previousGameMode a game mode that the player had before joining the server
 * @param gameMode a game mode that the player should initially have
 * @param settings a settings of a client associated with the player
 * @param clientBrand a brand name of a client associated with the player
 * @since 1.0
 * @see net.hypejet.jet.server.network.session.task.ConfigurationSessionTask
 * @see net.hypejet.jet.server.entity.player.JetPlayer
 */
public record ConfigurationData(@NonNull LoginData loginData, @NonNull JetWorld world, @NonNull Position position,
                                boolean enableRespawnScreen, Player.@Nullable GameMode previousGameMode,
                                Player.@NonNull GameMode gameMode, Player.@NonNull Settings settings,
                                @NonNull String clientBrand) {
    /**
     * Constructs the {@linkplain ConfigurationData configuration data}.
     *
     * @param loginData a login data of the player
     * @param world a world that the player should initially spawn in
     * @param position a position that the player should initially spawn at
     * @param enableRespawnScreen whether a respawn screen should be enabled for the player
     * @param previousGameMode a game mode that the player had before joining the server
     * @param gameMode a game mode that the player should initially have
     * @param settings a settings of a client associated with the player
     * @param clientBrand a brand name of a client associated with the player
     * @since 1.0
     */
    public ConfigurationData {
        NullabilityUtil.requireNonNull(loginData, "login data");
        NullabilityUtil.requireNonNull(world, "spawning world");
        NullabilityUtil.requireNonNull(position, "spawning position");
        NullabilityUtil.requireNonNull(gameMode, "game mode");
        NullabilityUtil.requireNonNull(settings, "settings");
        NullabilityUtil.requireNonNull(clientBrand, "client brand");
    }
}