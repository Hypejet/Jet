package net.hypejet.jet.server.scoreboard;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.scoreboard.exception.NoSuchObjectiveException;
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
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
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

    private final Set<JetPlayer> viewers = new HashSet<>();
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
    public @NonNull Set<JetPlayer> viewers() {
        try {
            this.lock.readLock().lock();
            return Set.copyOf(this.viewers);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Displays {@linkplain ScoreboardObjective a scoreboard objective} for {@linkplain JetPlayer a player} specified.
     *
     * @param player the player
     * @param position a scoreboard position where the scoreboard objective should be displayed at
     * @param name a name of the scoreboard objective that should be displayed
     * @param displayedObjectives a mutable map which maps scoreboard positions to names of scoreboard objectives
     *                            that are displayed at these positions for the player
     * @param displayedObjectivesLock a read-write lock that guards multithreaded access to the map containing
     *                                displayed objectives
     * @return a name of a previous scoreboard objective that was displayed to the player, {@code null} if none
     * @throws NoSuchObjectiveException if no scoreboard objective with the name specified was registered
     *                                  in this scoreboard
     * @since 1.0
     */
    public @Nullable String setDisplayedObjective(
            @NonNull JetPlayer player, @NonNull ScoreboardPosition position, @NonNull String name,
            @NonNull Map<ScoreboardPosition, String> displayedObjectives,
            @NonNull ReadWriteLock displayedObjectivesLock
    ) {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(name, "name");
        NullabilityUtil.requireNonNull(displayedObjectives, "displayed objectives");
        NullabilityUtil.requireNonNull(displayedObjectivesLock, "displayed objectives lock");

        try {
            this.lock.readLock().lock();
            displayedObjectivesLock.writeLock().lock();

            if (!this.objectives.containsKey(name)) {
                throw new NoSuchObjectiveException(String.format(
                        "A scoreboard objective with name of %s does not exist",
                        name
                ));
            }

            player.sendPacket(new ServerSetObjectiveDisplayedPlayPacket(position, name));
            return displayedObjectives.put(position, name);
        } finally {
            this.lock.readLock().unlock();
            displayedObjectivesLock.writeLock().unlock();
        }
    }

    /**
     * Replaces {@linkplain ScoreboardObjective a scoreboard objective} displayed
     * at some {@linkplain ScoreboardPosition scoreboard position} for {@linkplain JetPlayer a player} specified.
     * The replacement is done only if name of a scoreboard objective displayed at the scoreboard position
     * at time of calling the method is equal to a value specified.
     *
     * @param player the player
     * @param position the scoreboard position
     * @param name the value
     * @param newName a name of a new scoreboard objective that should be displayed
     * @param displayedObjectives a mutable map which maps scoreboard positions to names of scoreboard objectives
     *                            that are displayed at these positions for the player
     * @param displayedObjectivesLock a read-write lock that guards multithreaded access to the map containing
     *                                displayed objectives
     * @return {@code true} if the scoreboard objective displayed was replaced, {@code false} otherwise
     * @throws NoSuchObjectiveException if no scoreboard objective with the new name specified was registered
     *                                  in this scoreboard
     * @since 1.0
     */
    public boolean replaceDisplayedObjective(
            @NonNull JetPlayer player, @NonNull ScoreboardPosition position,
            @NonNull String name, @NonNull String newName,
            @NonNull Map<ScoreboardPosition, String> displayedObjectives,
            @NonNull ReadWriteLock displayedObjectivesLock
    ) {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(name, "name");
        NullabilityUtil.requireNonNull(newName, "new name");
        NullabilityUtil.requireNonNull(displayedObjectives, "displayed objectives");
        NullabilityUtil.requireNonNull(displayedObjectivesLock, "displayed objectives lock");

        try {
            this.lock.readLock().lock();
            displayedObjectivesLock.writeLock().lock();

            if (!this.objectives.containsKey(name)) {
                throw new NoSuchObjectiveException(String.format(
                        "A scoreboard objective with name of %s does not exist",
                        name
                ));
            }

            boolean result = displayedObjectives.replace(position, name, newName);
            if (result) player.sendPacket(new ServerSetObjectiveDisplayedPlayPacket(position, newName));
            return result;
        } finally {
            this.lock.readLock().unlock();
            displayedObjectivesLock.writeLock().unlock();
        }
    }

    /**
     * Adds {@linkplain JetPlayer a player} specified to {@linkplain Set a set} of viewers
     * of this {@linkplain JetScoreboard scoreboard}. Moreover, {@linkplain ServerPacket server packets} initializing
     * the scoreboard are sent to the same player.
     *
     * @param player the player
     * @since 1.0
     */
    public void addViewer(@NonNull JetPlayer player) {
        try {
            this.lock.writeLock().lock();
            this.viewers.add(player);

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
     * @since 1.0
     */
    public void removeViewer(@NonNull JetPlayer player) {
        try {
            this.lock.writeLock().lock();
            this.viewers.remove(player);
            for (String name : this.objectives.keySet())
                player.sendPacket(new ServerObjectiveActionPlayPacket(name, Action.Remove.INSTANCE));
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private @NonNull Map<String, Score> scoreMap(@NonNull String objective) {
        Map<String, Score> scoreMap = this.scoreMaps.get(objective);
        if (scoreMap == null) {
            throw new NoSuchObjectiveException(String.format(
                    "Objective with name of %s has not been registered",
                    objective
            ));
        }
        return scoreMap;
    }

    private void handleObjectiveRemoval(@NonNull String name) {
        this.sendObjectiveUpdate(name, Action.Remove.INSTANCE);
        this.viewers.forEach(player -> player.handleScoreboardObjectiveRemoval(name));
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
        this.viewers.forEach(player -> player.sendPacket(packet)); // TODO: FRAME
    }

    private static @NonNull String ownerName(@NonNull Entity entity) {
        // TODO: Check entity type instead of the entity being an instance of player
        return entity instanceof JetPlayer player ? player.username() : entity.uniqueId().toString();
    }
}