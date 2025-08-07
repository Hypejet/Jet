package net.hypejet.jet.server.network.session.data;

import net.hypejet.jet.data.model.api.coordinate.Position;
import java.util.Objects;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.scoreboard.JetScoreboard;
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
 * @param initialScoreboard an initial scoreboard that the player should have
 * @since 1.0
 * @see net.hypejet.jet.server.network.session.task.ConfigurationSessionTask
 * @see net.hypejet.jet.server.entity.player.JetPlayer
 */
public record ConfigurationData(@NonNull LoginData loginData, @NonNull JetWorld world, @NonNull Position position,
                                boolean enableRespawnScreen, Player.@Nullable GameMode previousGameMode,
                                Player.@NonNull GameMode gameMode, Player.@NonNull Settings settings,
                                @NonNull String clientBrand, @NonNull JetScoreboard initialScoreboard) {
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
     * @param initialScoreboard an initial scoreboard that the player should have
     * @since 1.0
     */
    public ConfigurationData {
        Objects.requireNonNull(loginData, "login data");
        Objects.requireNonNull(world, "spawning world");
        Objects.requireNonNull(position, "spawning position");
        Objects.requireNonNull(gameMode, "game mode");
        Objects.requireNonNull(settings, "settings");
        Objects.requireNonNull(clientBrand, "client brand");
        Objects.requireNonNull(initialScoreboard, "initial scoreboard");
    }
}