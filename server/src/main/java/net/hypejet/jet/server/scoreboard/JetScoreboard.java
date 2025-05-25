package net.hypejet.jet.server.scoreboard;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.scoreboard.exception.NoSuchObjectiveException;
import net.hypejet.jet.scoreboard.exception.NotViewerException;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public @Nullable ScoreboardObjective getObjective(@NonNull String name) {
        NullabilityUtil.requireNonNull(name, "name");
        try {
            this.lock.readLock().lock();
            return this.objectives.get(name);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public @NonNull ScoreboardObjective registerObjective(@NonNull String name,
                                                          @NonNull ScoreboardObjective objective) {
        NullabilityUtil.requireNonNull(name, "name");
        NullabilityUtil.requireNonNull(objective, "objective");

        try {
            this.lock.writeLock().lock();
            if (this.objectives.containsKey(name))
                return this.objectives.get(name);

            this.objectives.put(name, objective);
            this.scoreMaps.put(name, new HashMap<>());
            this.sendObjectiveUpdate(name, new Action.Create(objective));

            return objective;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public boolean replaceObjective(@NonNull String name, @NonNull ScoreboardObjective expectedObjective,
                                    @NonNull ScoreboardObjective newObjective) {
        NullabilityUtil.requireNonNull(name, "name");
        NullabilityUtil.requireNonNull(expectedObjective, "expected objective");
        NullabilityUtil.requireNonNull(newObjective, "new objective");

        try {
            this.lock.writeLock().lock();
            boolean result = this.objectives.replace(name, expectedObjective, newObjective);
            if (result) this.sendObjectiveUpdate(name, new Action.Update(newObjective));
            return result;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public @Nullable ScoreboardObjective removeObjective(@NonNull String name) {
        NullabilityUtil.requireNonNull(name, "name");

        try {
            this.lock.writeLock().lock();
            this.scoreMaps.remove(name);

            ScoreboardObjective objectiveRemoved = this.objectives.remove(name);
            if (objectiveRemoved != null)
                this.handleObjectiveRemoval(name);
            return objectiveRemoved;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeObjective(@NonNull String name, @NonNull ScoreboardObjective expectedObjective) {
        NullabilityUtil.requireNonNull(name, "name");
        NullabilityUtil.requireNonNull(expectedObjective, "expected objective");

        try {
            this.lock.writeLock().lock();
            this.scoreMaps.remove(name);

            boolean result = this.objectives.remove(name, expectedObjective);
            if (result) this.handleObjectiveRemoval(name);
            return result;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public @NonNull Map<String, ScoreboardObjective> objectives() {
        try {
            this.lock.readLock().lock();
            return Map.copyOf(this.objectives);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public @Nullable Score getScore(@NonNull Entity entity, @NonNull String objective) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.getScore(ownerName(entity), objective);
    }

    @Override
    public @Nullable Score getScore(@NonNull String owner, @NonNull String objective) {
        NullabilityUtil.requireNonNull(owner, "owner");
        NullabilityUtil.requireNonNull(objective, "objective");

        try {
            this.lock.readLock().lock();
            return this.scoreMap(objective).get(owner);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public @Nullable Score setScore(@NonNull Entity entity, @NonNull String objective, @NonNull Score score) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.setScore(ownerName(entity), objective, score);
    }

    @Override
    public @Nullable Score setScore(@NonNull String owner, @NonNull String objective, @NonNull Score score) {
        NullabilityUtil.requireNonNull(owner, "owner");
        NullabilityUtil.requireNonNull(objective, "objective");
        NullabilityUtil.requireNonNull(score, "score");

        try {
            this.lock.writeLock().lock();
            this.sendScoreUpdate(owner, objective, score);
            return this.scoreMap(objective).put(owner, score);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public boolean replaceScore(@NonNull Entity entity, @NonNull String objective,
                                @NonNull Score expectedScore, @NonNull Score newScore) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.replaceScore(ownerName(entity), objective, expectedScore, newScore);
    }

    @Override
    public boolean replaceScore(@NonNull String owner, @NonNull String objective,
                                @NonNull Score expectedScore, @NonNull Score newScore) {
        NullabilityUtil.requireNonNull(owner, "owner");
        NullabilityUtil.requireNonNull(objective, "objective");
        NullabilityUtil.requireNonNull(expectedScore, "expected score");
        NullabilityUtil.requireNonNull(newScore, "new score");

        try {
            this.lock.writeLock().lock();
            boolean result = this.scoreMap(objective).replace(owner, expectedScore, newScore);
            if (result) this.sendScoreUpdate(owner, objective, newScore);
            return result;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public @Nullable Score removeScore(@NonNull Entity entity, @NonNull String objective) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.removeScore(ownerName(entity), objective);
    }

    @Override
    public @Nullable Score removeScore(@NonNull String owner, @NonNull String objective) {
        NullabilityUtil.requireNonNull(owner, "owner");
        NullabilityUtil.requireNonNull(objective, "objective");

        try {
            this.lock.writeLock().lock();
            this.sendScoreReset(owner, objective);
            return this.scoreMap(objective).remove(owner);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeScore(@NonNull Entity entity, @NonNull String objective, @NonNull Score score) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.removeScore(ownerName(entity), objective, score);
    }

    @Override
    public boolean removeScore(@NonNull String owner, @NonNull String objective, @NonNull Score score) {
        NullabilityUtil.requireNonNull(owner, "owner");
        NullabilityUtil.requireNonNull(objective, "objective");
        NullabilityUtil.requireNonNull(score, "score");

        try {
            this.lock.writeLock().lock();
            boolean result = this.scoreMap(objective).remove(owner, score);
            if (result) this.sendScoreReset(owner, objective);
            return result;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public @NonNull Set<String> removeScores(@NonNull Entity entity) {
        NullabilityUtil.requireNonNull(entity, "entity");
        return this.removeScores(ownerName(entity));
    }

    @Override
    public @NonNull Set<String> removeScores(@NonNull String owner) {
        NullabilityUtil.requireNonNull(owner, "owner");
        try {
            this.lock.writeLock().lock();

            Set<String> objectiveNames = new HashSet<>();
            for (Map.Entry<String, Map<String, Score>> entry : this.scoreMaps.entrySet()) {
                Map<String, Score> scoreMap = entry.getValue();
                if (!scoreMap.containsKey(owner)) continue;
                scoreMap.remove(owner);
                objectiveNames.add(entry.getKey());
            }

            this.sendScoreReset(owner, null);
            return Set.copyOf(objectiveNames);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public @NonNull Map<String, Score> scores(@NonNull String objective) {
        try {
            this.lock.readLock().lock();
            return Map.copyOf(this.scoreMap(objective));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public @Nullable String getDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position) {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(position, "position");

        JetPlayer castPlayer = JetPlayer.cast(player);
        try {
            this.lock.readLock().lock();
            return this.displayedObjectivesHandler(castPlayer).getDisplayedObjective(position);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public @Nullable String setDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position,
                                                  @Nullable String name) {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(position, "position");

        JetPlayer castPlayer = JetPlayer.cast(player);
        try {
            this.lock.readLock().lock();
            if (name != null && !this.objectives.containsKey(name))
                throw createNoSuchObjectiveException(name);
            return this.displayedObjectivesHandler(castPlayer).setDisplayedObjective(position, name);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public boolean replaceDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position,
                                             @Nullable String name, @Nullable String newName) {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(position, "position");

        JetPlayer castPlayer = JetPlayer.cast(player);
        try {
            this.lock.readLock().lock();
            if (newName != null && !this.objectives.containsKey(newName))
                throw createNoSuchObjectiveException(newName);
            return this.displayedObjectivesHandler(castPlayer).replaceDisplayedObjective(position, name, newName);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public @NonNull Set<JetPlayer> viewers() {
        try {
            this.lock.readLock().lock();
            return Set.copyOf(this.viewers.keySet());
        } finally {
            this.lock.readLock().unlock();
        }
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
        try {
            this.lock.writeLock().lock();
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
        } finally {
            this.lock.writeLock().unlock();
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
        try {
            this.lock.writeLock().lock();
            if (!this.viewers.containsKey(player))
                throw new IllegalStateException("The player specified is not a viewer of this scoreboard");

            this.viewers.remove(player);
            for (String name : this.objectives.keySet())
                player.sendPacket(new ServerObjectiveActionPlayPacket(name, Action.Remove.INSTANCE));
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private @NonNull Map<String, Score> scoreMap(@NonNull String objective) {
        Map<String, Score> scoreMap = this.scoreMaps.get(objective);
        if (scoreMap == null)
            throw createNoSuchObjectiveException(objective);
        return scoreMap;
    }

    private @NonNull DisplayedObjectivesHandler displayedObjectivesHandler(@NonNull JetPlayer player) {
        DisplayedObjectivesHandler handler = this.viewers.get(player);
        if (handler == null)
            throw new NotViewerException("The player specified is not a viewer of this scoreboard");
        return handler;
    }

    private void handleObjectiveRemoval(@NonNull String name) {
        this.sendObjectiveUpdate(name, Action.Remove.INSTANCE);
        this.viewers.values().forEach(handler -> handler.handleObjectiveRemoval(name));
    }

    private void sendObjectiveUpdate(@NonNull String name, @NonNull Action action) {
        this.sendPacketToViewers(new ServerObjectiveActionPlayPacket(name, action));
    }

    private void sendScoreUpdate(@NonNull String entityName, @NonNull String objectiveName, @NonNull Score newScore) {
        this.sendPacketToViewers(new ServerUpdateScorePlayPacket(entityName, objectiveName, newScore));
    }

    private void sendScoreReset(@NonNull String entityName, @Nullable String objectiveName) {
        this.sendPacketToViewers(new ServerResetScorePlayPacket(entityName, objectiveName));
    }

    private void sendPacketToViewers(@NonNull ServerPacket packet) {
        this.viewers.keySet().forEach(player -> player.sendPacket(packet)); // TODO: FRAME
    }

    private static @NonNull String ownerName(@NonNull Entity entity) {
        // TODO: Check entity type instead of the entity being an instance of player
        return entity instanceof JetPlayer player ? player.username() : entity.uniqueId().toString();
    }

    private static @NonNull NoSuchObjectiveException createNoSuchObjectiveException(@NonNull String name) {
        return new NoSuchObjectiveException(String.format(
                "Objective with name of %s was not registered in this scoreboard",
                name
        ));
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
        private final ReentrantReadWriteLock displayedObjectivesLock = new ReentrantReadWriteLock();

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
         *         at the scoreboard position
         * @since 1.0
         */
        private @Nullable String getDisplayedObjective(@NonNull ScoreboardPosition position) {
            try {
                this.displayedObjectivesLock.readLock().lock();
                return this.displayedObjectives.get(position);
            } finally {
                this.displayedObjectivesLock.readLock().unlock();
            }
        }

        /**
         * Sets {@linkplain ScoreboardObjective a scoreboard objective} that should be displayed
         * at some {@linkplain ScoreboardPosition scoreboard position} for {@linkplain JetPlayer a player}
         * associated with this handler.
         *
         * @param position the scoreboard position
         * @param name a name of the scoreboard objective that should be displayed, {@code null} if no scoreboard
         *             objective should be displayed at the scoreboard position
         * @return a name of a previous scoreboard objective that was displayed for the player
         *         at the same position, {@code null} if none
         * @since 1.0
         */
        private @Nullable String setDisplayedObjective(@NonNull ScoreboardPosition position, @Nullable String name) {
            try {
                this.displayedObjectivesLock.writeLock().lock();
                return this.updateObjective(position, name);
            } finally {
                this.displayedObjectivesLock.writeLock().unlock();
            }
        }

        /**
         * Replaces {@linkplain ScoreboardObjective a scoreboard objective} displayed
         * at some {@linkplain ScoreboardPosition scoreboard position} for {@linkplain JetPlayer a player}
         * associated with this handler. The replacement is done only if name of a scoreboard objective
         * displayed for the player at the same scoreboard position at time of calling the method is equal
         * to a value specified.
         *
         * @param position the scoreboard position
         * @param name the value, {@code null} if it is expected that no scoreboard objective is displayed
         *             for the player at the scoreboard position at time of calling the method
         * @param newName a name of a new scoreboard objective that should be displayed for the player
         *                at the scoreboard position, {@code null} if no scoreboard objective should be displayed
         *                at that scoreboard position
         * @return {@code true} if the scoreboard objective displayed was replaced, {@code false} otherwise
         * @since 1.0
         */
        private boolean replaceDisplayedObjective(@NonNull ScoreboardPosition position,
                                                  @Nullable String name, @Nullable String newName) {
            try {
                this.displayedObjectivesLock.writeLock().lock();

                String previousName = this.displayedObjectives.get(position);
                if (!Objects.equals(previousName, name)) return false;

                this.updateObjective(position, newName);
                return true;
            } finally {
                this.displayedObjectivesLock.writeLock().unlock();
            }
        }

        /**
         * Handles removal of {@linkplain ScoreboardObjective a scoreboard objective} with name specified.
         *
         * @param name the scoreboard objective name
         * @since 1.0
         */
        private void handleObjectiveRemoval(@NonNull String name) {
            try {
                this.displayedObjectivesLock.writeLock().lock();
                this.displayedObjectives.entrySet().removeIf(entry -> entry.getValue().equals(name));
            } finally {
                this.displayedObjectivesLock.writeLock().unlock();
            }
        }

        private @Nullable String updateObjective(@NonNull ScoreboardPosition position, @Nullable String name) {
            if (Objects.equals(name, this.displayedObjectives.get(position))) return name;
            this.player.sendPacket(new ServerSetObjectiveDisplayedPlayPacket(position, name));
            if (name == null) return this.displayedObjectives.remove(position);
            else return this.displayedObjectives.put(position, name);
        }
    }
}