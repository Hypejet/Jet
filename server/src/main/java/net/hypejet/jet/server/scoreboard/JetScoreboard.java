package net.hypejet.jet.server.scoreboard;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.scoreboard.objective.ScoreboardObjective;
import net.hypejet.jet.scoreboard.position.ScoreboardPosition;
import net.hypejet.jet.scoreboard.score.Score;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerObjectiveActionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerObjectiveActionPlayPacket.Action;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerResetScorePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSetObjectiveDisplayedPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateScorePlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain Scoreboard a scoreboard}.
 *
 * @since 1.0
 * @see Scoreboard
 */
public final class JetScoreboard implements Scoreboard {

    private final Map<String, ScoreboardObjective> objectives = new HashMap<>();
    private final Map<String, Map<String, Score>> scoreMaps = new HashMap<>();
    private final Map<JetPlayer, DisplayedObjectivesHandler> viewers = new HashMap<>();

    @Override
    public @Nullable ScoreboardObjective getObjective(@NonNull String name) {
        validateObjectiveName(name);
        return this.objectives.get(name);
    }

    @Override
    public @Nullable ScoreboardObjective setObjective(@NonNull String name, @Nullable ScoreboardObjective objective) {
        validateObjectiveName(name);

        ScoreboardObjective currentObjective = this.objectives.get(name);
        if (Objects.equals(currentObjective, objective)) return objective;

        Action action;
        ScoreboardObjective result;

        if (objective == null) {
            this.viewers.values().forEach(handler -> handler.handleObjectiveRemoval(name));
            this.scoreMaps.remove(name);
            action = Action.Remove.INSTANCE;
            result = this.objectives.remove(name);
        } else {
            if (currentObjective == null) {
                this.scoreMaps.put(name, new HashMap<>());
                action = new Action.Create(objective);
            } else {
                action = new Action.Update(objective);
            }
            result = this.objectives.put(name, objective);
        }

        this.sendPacketToViewers(new ServerObjectiveActionPlayPacket(name, action));
        return result;
    }

    @Override
    public @NonNull Map<String, ScoreboardObjective> objectives() {
        return Map.copyOf(this.objectives);
    }

    @Override
    public @Nullable Score getScore(@NonNull Entity entity, @NonNull String objective) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.getScore(ownerName(entity), objective);
    }

    @Override
    public @Nullable Score getScore(@NonNull String owner, @NonNull String objective) {
        NullabilityUtil.requireNonNull(owner, "owner");
        return this.scoreMap(objective).get(owner);
    }

    @Override
    public @Nullable Score setScore(@NonNull Entity entity, @NonNull String objective, @Nullable Score score) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.setScore(ownerName(entity), objective, score);
    }

    @Override
    public @Nullable Score setScore(@NonNull String owner, @NonNull String objective, @Nullable Score score) {
        NullabilityUtil.requireNonNull(owner, "owner");

        Map<String, Score> scoreMap = this.scoreMap(objective);
        if (Objects.equals(scoreMap.get(owner), score)) return score;

        if (score == null) {
            this.sendPacketToViewers(new ServerResetScorePlayPacket(owner, objective));
            return scoreMap.remove(owner);
        }

        this.sendPacketToViewers(new ServerUpdateScorePlayPacket(owner, objective, score));
        return scoreMap.put(owner, score);
    }

    @Override
    public @NonNull Map<String, Score> removeScores(@NonNull Entity entity) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.removeScores(ownerName(entity));
    }

    @Override
    public @NonNull Map<String, Score> removeScores(@NonNull String owner) {
        NullabilityUtil.requireNonNull(owner, "owner");

        Map<String, Score> objectiveToScoreMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Score>> entry : this.scoreMaps.entrySet()) {
            Map<String, Score> scoreMap = entry.getValue();
            if (!scoreMap.containsKey(owner)) continue;
            objectiveToScoreMap.put(entry.getKey(), scoreMap.remove(owner));
        }

        this.sendPacketToViewers(new ServerResetScorePlayPacket(owner, null));
        return Map.copyOf(objectiveToScoreMap);
    }

    @Override
    public @NonNull Map<String, Score> scores(@NonNull String objective) {
        return Map.copyOf(this.scoreMap(objective));
    }

    @Override
    public @Nullable String getDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position) {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(position, "position");
        return this.displayedObjectivesHandler(JetPlayer.cast(player)).getDisplayedObjective(position);
    }

    @Override
    public @Nullable String setDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position,
                                                  @Nullable String name) {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(position, "position");

        JetPlayer castPlayer = JetPlayer.cast(player);
        if (name != null) {
            validateObjectiveName(name);
            if (!this.objectives.containsKey(name))
                throw createNoSuchObjectiveException(name);
        }

        return this.displayedObjectivesHandler(castPlayer).setDisplayedObjective(position, name);
    }

    @Override
    public @NonNull Set<JetPlayer> viewers() {
        return Set.copyOf(this.viewers.keySet());
    }

    /**
     * Adds {@linkplain JetPlayer a player} specified to {@linkplain Set a set} of viewers
     * of this {@linkplain JetScoreboard scoreboard}. Moreover, {@linkplain ServerPacket server packets} initializing
     * the scoreboard are sent to the same player.
     *
     * @param player the player
     * @throws IllegalStateException if the player is already a viewer of this scoreboard
     * @since 1.0
     */
    public void addViewer(@NonNull JetPlayer player) {
        if (this.viewers.containsKey(player))
            throw new IllegalStateException("The player specified is already a viewer of this scoreboard");
        this.viewers.put(player, new DisplayedObjectivesHandler(player));

        for (Map.Entry<String, ScoreboardObjective> entry : this.objectives.entrySet()) {
            String name = entry.getKey();
            ScoreboardObjective objective = entry.getValue();
            player.sendPacket(new ServerObjectiveActionPlayPacket(name, new Action.Create(objective)));
        }

        for (Map.Entry<String, Map<String, Score>> entry : this.scoreMaps.entrySet()) {
            String objectiveName = entry.getKey();
            for (Map.Entry<String, Score> scoreEntry : entry.getValue().entrySet()) {
                String entityName = scoreEntry.getKey();
                Score score = scoreEntry.getValue();
                player.sendPacket(new ServerUpdateScorePlayPacket(entityName, objectiveName, score));
            }
        }
    }

    /**
     * Removes {@linkplain JetPlayer a player} specified from {@linkplain Set a set} of viewers
     * of this {@linkplain JetScoreboard scoreboard}. Moreover, {@linkplain ServerPacket server packets} removing
     * the scoreboard are sent to the same player.
     *
     * @param player the player
     * @throws IllegalStateException if the player is not a viewer of this scoreboard
     * @since 1.0
     */
    public void removeViewer(@NonNull JetPlayer player) {
        if (!this.viewers.containsKey(player))
            throw new IllegalStateException("The player specified is not a viewer of this scoreboard");

        this.viewers.remove(player);
        for (String name : this.objectives.keySet())
            player.sendPacket(new ServerObjectiveActionPlayPacket(name, Action.Remove.INSTANCE));
    }

    private @NonNull Map<String, Score> scoreMap(@NonNull String objective) {
        validateObjectiveName(objective);
        Map<String, Score> scoreMap = this.scoreMaps.get(objective);
        if (scoreMap == null) throw createNoSuchObjectiveException(objective);
        return scoreMap;
    }

    private @NonNull DisplayedObjectivesHandler displayedObjectivesHandler(@NonNull JetPlayer player) {
        DisplayedObjectivesHandler handler = this.viewers.get(player);
        if (handler == null)
            throw new IllegalArgumentException("The player specified is not a viewer of this scoreboard");
        return handler;
    }

    private void sendPacketToViewers(@NonNull ServerPacket packet) {
        this.viewers.keySet().forEach(player -> player.sendPacket(packet)); // TODO: FRAME
    }

    private static @NonNull String ownerName(@NonNull Entity entity) {
        // TODO: Check entity type instead of the entity being an instance of player
        return entity instanceof JetPlayer player ? player.username() : entity.uniqueId().toString();
    }

    private static @NonNull IllegalArgumentException createNoSuchObjectiveException(@NonNull String name) {
        return new IllegalArgumentException(String.format(
                "Objective with name of %s has not been registered in this scoreboard",
                name
        ));
    }

    private static void validateObjectiveName(@NonNull String objectiveName) {
        NullabilityUtil.requireNonNull(objectiveName, "objective name");
        if (objectiveName.isEmpty())
            throw new IllegalArgumentException("A scoreboard objective name must not be empty");
    }

    /**
     * Represents something that handles displaying of {@linkplain ScoreboardObjective scoreboard objectives}
     * for {@linkplain JetPlayer a player}.
     *
     * @since 1.0
     * @see ScoreboardObjective
     * @see JetPlayer
     */
    private static final class DisplayedObjectivesHandler {

        private final JetPlayer player;
        private final Map<ScoreboardPosition, String> displayedObjectives = new HashMap<>();

        /**
         * Constructs the {@linkplain DisplayedObjectivesHandler displayed objectives handler}.
         *
         * @param player the player that the handling should be done for
         * @since 1.0
         */
        private DisplayedObjectivesHandler(@NonNull JetPlayer player) {
            this.player = NullabilityUtil.requireNonNull(player, "player");
        }

        /**
         * Gets name of {@linkplain ScoreboardObjective a scoreboard objective} displayed
         * for {@linkplain JetPlayer a player} associated with this handler
         * at {@linkplain ScoreboardPosition a scoreboard position} specified.
         *
         * @param position the scoreboard position
         * @return the scoreboard objective name, {@code null} if no scoreboard objective is displayed for the player
         *         at the specified scoreboard position
         * @since 1.0
         */
        private @Nullable String getDisplayedObjective(@NonNull ScoreboardPosition position) {
            return this.displayedObjectives.get(position);
        }

        /**
         * Sets {@linkplain ScoreboardObjective a scoreboard objective} that should be displayed
         * at some {@linkplain ScoreboardPosition scoreboard position} for {@linkplain JetPlayer a player}
         * associated with this handler.
         *
         * @param position the scoreboard position
         * @param name a name of the scoreboard objective that should be displayed, {@code null} if no scoreboard
         *             objective should be displayed at the specified scoreboard position
         * @return a name of a previous scoreboard objective that was displayed for the player
         *         at the same position, {@code null} if none
         * @since 1.0
         */
        private @Nullable String setDisplayedObjective(@NonNull ScoreboardPosition position, @Nullable String name) {
            if (Objects.equals(name, this.displayedObjectives.get(position))) return name;
            this.player.sendPacket(new ServerSetObjectiveDisplayedPlayPacket(position, name));
            if (name == null) return this.displayedObjectives.remove(position);
            else return this.displayedObjectives.put(position, name);
        }

        /**
         * Handles removal of {@linkplain ScoreboardObjective a scoreboard objective} with name specified.
         *
         * @param name the scoreboard objective name
         * @since 1.0
         */
        private void handleObjectiveRemoval(@NonNull String name) {
            this.displayedObjectives.entrySet().removeIf(entry -> entry.getValue().equals(name));
        }
    }
}