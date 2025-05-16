package net.hypejet.jet.scoreboard;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.scoreboard.exception.NoSuchObjectiveException;
import net.hypejet.jet.scoreboard.objective.ScoreboardObjective;
import net.hypejet.jet.scoreboard.score.Score;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Represents a Minecraft scoreboard.
 *
 * @since 1.0
 */
public interface Scoreboard {
    /**
     * Gets {@linkplain ScoreboardObjective a scoreboard objective} registered
     * in this {@linkplain Scoreboard scoreboard} by a name specified.
     *
     * @param name the name
     * @return the scoreboard objective, {@code null} if no scoreboard objective with the name specified
     *         has been registered in this scoreboard
     * @since 1.0
     */
    @Nullable ScoreboardObjective getObjective(@NonNull String name);

    /**
     * Registers {@linkplain ScoreboardObjective a scoreboard objective} specified with a name specified
     * if no other scoreboard objective with the name specified has been registered.
     *
     * @param name the name
     * @return the scoreboard objective specified, or another scoreboard objective if it was already registered
     *         with the name specified
     * @since 1.0
     */
    @NonNull ScoreboardObjective registerObjective(@NonNull String name, @NonNull ScoreboardObjective objective);

    /**
     * Replaces {@linkplain ScoreboardObjective a scoreboard objective} with a name specified from this scoreboard
     * only if it is equal to an expected scoreboard objective specified.
     *
     * @param name the name
     * @param expectedObjective the expected scoreboard objective
     * @param newObjective a scoreboard objective that the existing objective should be replaced with
     * @return {@code true} if the scoreboard objective was replaced, {@code false} otherwise
     * @since 1.0
     */
    boolean replaceObjective(@NonNull String name, @NonNull ScoreboardObjective expectedObjective,
                             @NonNull ScoreboardObjective newObjective);

    /**
     * Removes {@linkplain ScoreboardObjective a scoreboard objective} registered in this scoreboard.
     *
     * @param name a name that was given to the scoreboard objective when it was registered
     * @return the scoreboard objective that was removed, {@code null} if no scoreboard objective
     *         was registered with the name specified
     * @since 1.0
     */
    @Nullable ScoreboardObjective removeObjective(@NonNull String name);

    /**
     * Removes {@linkplain ScoreboardObjective a scoreboard objective} registered in this scoreboard only if it is
     * equal to the expected scoreboard objective specified.
     *
     * @param name a name that was given to the scoreboard objective when it was registered
     * @param expectedObjective the expected scoreboard objective
     * @return {@code true} if the scoreboard objective was removed, {@code false} otherwise
     * @since 1.0
     */
    boolean removeObjective(@NonNull String name, @NonNull ScoreboardObjective expectedObjective);

    /**
     * Gets a copy of {@linkplain Map a map} which maps names to {@linkplain ScoreboardObjective scoreboard objectives}
     * that the names were given to during registration in this scoreboard.
     *
     * @return the map copy
     * @since 1.0
     */
    @NonNull Map<String, ScoreboardObjective> objectives();

    /**
     * Gets {@linkplain Score a score} of {@linkplain Entity an entity} specified.
     *
     * @param entity the entity
     * @param objective a name of a scoreboard objective to get the score from
     * @return the score, {@code null} if the entity specified has no score
     *         in the scoreboard objective with name specified
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    @Nullable Score getScore(@NonNull Entity entity, @NonNull String objective);

    /**
     * Gets {@linkplain Score a score} of an owner with name specified.
     *
     * @param owner the owner name
     * @param objective a name of a scoreboard objective to get the score from
     * @return the score, {@code null} if an owner with name specified
     *         has no score in the scoreboard objective with name specified
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    @Nullable Score getScore(@NonNull String owner, @NonNull String objective);

    /**
     * Sets {@linkplain Score a score} of {@linkplain Entity an entity} specified.
     *
     * @param entity the entity
     * @param objective a name of a scoreboard objective to set the score in
     * @param score a value that the score should be set to
     * @return a previous score of the entity, {@code null} if the entity did not have a score
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    @Nullable Score setScore(@NonNull Entity entity, @NonNull String objective, @NonNull Score score);

    /**
     * Sets {@linkplain Score a score} of an owner with name specified.
     *
     * @param owner the owner name
     * @param objective a name of a scoreboard objective to set the score in
     * @param score a value that the score should be set to
     * @return a previous score of the owner, {@code null} if the owner did not have a score
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    @Nullable Score setScore(@NonNull String owner, @NonNull String objective, @NonNull Score score);

    /**
     * Sets {@linkplain Score a score} of {@linkplain Entity an entity} specified only if score of the entity
     * at time of calling the method is equal to a value specified.
     *
     * @param entity the entity
     * @param objective a name of a scoreboard objective to set the score in
     * @param expectedScore a value that the score should be equal to
     * @param newScore a value that the score should be set to
     * @return {@code true} if the score has been replaced, {@code false} otherwise
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    boolean replaceScore(@NonNull Entity entity, @NonNull String objective,
                         @NonNull Score expectedScore, @NonNull Score newScore);

    /**
     * Sets {@linkplain Score a score} of an owner with name specified only if score of the owner at time
     * of calling the method is equal to a value specified.
     *
     * @param owner the owner name
     * @param objective a name of a scoreboard objective to set the score in
     * @param expectedScore a value that the score should be equal to
     * @param newScore a value that the score should be set to
     * @return {@code true} if the score has been replaced, {@code false} otherwise
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    boolean replaceScore(@NonNull String owner, @NonNull String objective,
                         @NonNull Score expectedScore, @NonNull Score newScore);

    /**
     * Removes {@linkplain Score a score} of {@linkplain Entity an entity} specified.
     *
     * @param entity the entity
     * @param objective a name of a scoreboard objective to remove the score from
     * @return a score that the entity had, {@code null} if none
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    @Nullable Score removeScore(@NonNull Entity entity, @NonNull String objective);

    /**
     * Removes {@linkplain Score a score} of an owner with name specified.
     *
     * @param owner the owner name
     * @param objective a name of a scoreboard objective to remove the score from
     * @return a score that the entity had, {@code null} if none
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    @Nullable Score removeScore(@NonNull String owner, @NonNull String objective);

    /**
     * Removes {@linkplain Score a score} of {@linkplain Entity an entity} specified only if their score at time
     * of calling the method is equal to a value specified.
     *
     * @param entity the entity
     * @param objective a name of a scoreboard objective to remove the score from
     * @param score the value
     * @return {@code true} if the score was removed, {@code false} otherwise
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    boolean removeScore(@NonNull Entity entity, @NonNull String objective, @NonNull Score score);

    /**
     * Removes {@linkplain Score a score} of an owner with name specified only if their score at time of calling
     * the method is equal to a value specified.
     *
     * @param owner the owner name
     * @param objective a name of a scoreboard objective to remove the score from
     * @param score the value
     * @return {@code true} if the score was removed, {@code false} otherwise
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    boolean removeScore(@NonNull String owner, @NonNull String objective, @NonNull Score score);

    /**
     * Removes all {@linkplain Score scores} of {@linkplain Entity an entity} specified
     * from all {@linkplain ScoreboardObjective scoreboard objectives} registered
     * in this {@linkplain Scoreboard scoreboard}
     *
     * @param entity the entity
     * @return a set of names of all scoreboard objectives where scores of the entity were removed,
     *         therefore it does not include names of scoreboard objectives where the entity did not have a score
     * @since 1.0
     */
    @NonNull Set<String> removeScores(@NonNull Entity entity);

    /**
     * Removes all {@linkplain Score scores} of an owner with name specified
     * from all {@linkplain ScoreboardObjective scoreboard objectives} registered
     * in this {@linkplain Scoreboard scoreboard}
     *
     * @param owner the owner name
     * @return a set of names of all scoreboard objectives where scores of the owner were removed,
     *         therefore it does not include names of scoreboard objectives where the owner did not have a score
     * @since 1.0
     */
    @NonNull Set<String> removeScores(@NonNull String owner);

    /**
     * Gets copy of {@linkplain Map a map} which maps owner names to {@linkplain Score scores} that the owners have
     * in {@linkplain ScoreboardObjective a scoreboard objective} with name specified.
     *
     * @param objective the scoreboard objective name
     * @return the map copy
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    @NonNull Map<String, Score> scores(@NonNull String objective);
}