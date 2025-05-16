package net.hypejet.jet.server.scoreboard;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.scoreboard.exception.NoSuchObjectiveException;
import net.hypejet.jet.scoreboard.objective.ScoreboardObjective;
import net.hypejet.jet.scoreboard.score.Score;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerObjectiveActionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerObjectiveActionPlayPacket.Action;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerResetScorePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateScorePlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of {@linkplain Scoreboard a scoreboard}.
 *
 * @since 1.0
 * @see Scoreboard
 */
public final class JetScoreboard implements Scoreboard {

    private final JetMinecraftServer server;

    private final Map<String, ScoreboardObjective> objectives = new HashMap<>();
    private final Map<String, Map<String, Score>> scoreMaps = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Constructs the {@linkplain JetScoreboard scoreboard implementation}.
     *
     * @param server a server that the scoreboard is being constructed for
     * @since 1.0
     */
    public JetScoreboard(@NonNull JetMinecraftServer server) {
        this.server = NullabilityUtil.requireNonNull(server, "server");
    }

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
                this.sendObjectiveUpdate(name, Action.Remove.INSTANCE);

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
            if (result) this.sendObjectiveUpdate(name, Action.Remove.INSTANCE);
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

    private void sendObjectiveUpdate(@NonNull String name, @NonNull Action action) {
        try (CollectionAcquisition<JetPlayer, ?> acquisition = this.server.players()) {
            ServerObjectiveActionPlayPacket packet = new ServerObjectiveActionPlayPacket(name, action); // TODO: FRAME
            acquisition.collection().forEach(player -> player.sendPacket(packet));
        }
    }

    private void sendScoreUpdate(@NonNull String entityName, @NonNull String objectiveName, @NonNull Score newScore) {
        try (CollectionAcquisition<JetPlayer, ?> acquisition = this.server.players()) {
            ServerUpdateScorePlayPacket packet = new ServerUpdateScorePlayPacket(entityName, objectiveName, newScore); // TODO: FRAME
            acquisition.collection().forEach(player -> player.sendPacket(packet));
        }
    }

    private void sendScoreReset(@NonNull String entityName, @Nullable String objectiveName) {
        try (CollectionAcquisition<JetPlayer, ?> acquisition = this.server.players()) {
            ServerResetScorePlayPacket packet = new ServerResetScorePlayPacket(entityName, objectiveName); // TODO: FRAME
            acquisition.collection().forEach(player -> player.sendPacket(packet));
        }
    }

    private static @NonNull String ownerName(@NonNull Entity entity) {
        return entity instanceof JetPlayer player ? player.username() : entity.uniqueId().toString();
    }
}