package net.hypejet.jet.scoreboard;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.scoreboard.objective.ScoreboardObjective;
import net.hypejet.jet.scoreboard.position.ScoreboardPosition;
import net.hypejet.jet.scoreboard.score.Score;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Represents a Minecraft scoreboard.
 *
 * <p>Note that the default implementation of the scoreboard is <strong>NOT</strong> thread-safe, therefore
 * interactions with it should be done in the main ticking thread only.</p>
 *
 * @since 1.0
 */
public interface Scoreboard {
    /**
     * Gets {@linkplain ScoreboardObjective a scoreboard objective} registered
     * in this {@linkplain Scoreboard scoreboard} with some name.
     *
     * @param name the name
     * @return the scoreboard objective, {@code null} if no scoreboard objective with the name specified
     *         has been registered in this scoreboard
     * @throws IllegalArgumentException if the specified scoreboard objective name is invalid
     * @since 1.0
     */
    @Nullable ScoreboardObjective getObjective(@NonNull String name);

    /**
     * Sets some {@linkplain ScoreboardObjective scoreboard objective}
     * in this {@linkplain Scoreboard scoreboard} to have a data specified.
     *
     * @param name a name of the scoreboard objective
     * @param objective the data, {@code null} if the scoreboard objective should be removed along with all scores
     *                  associated with it
     * @return a previous data of the scoreboard objective, {@code null} if the scoreboard objective did not exist
     * @throws IllegalArgumentException if the specified scoreboard objective name is invalid
     * @since 1.0
     */
    @Nullable ScoreboardObjective setObjective(@NonNull String name, @Nullable ScoreboardObjective objective);

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
     * @throws IllegalArgumentException if a scoreboard objective with the name specified does not exist
     *                                  or if the specified scoreboard objective name is invalid
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
     * @throws IllegalArgumentException if a scoreboard objective with the name specified does not exist
     *                                  or if the specified scoreboard objective name is invalid
     * @since 1.0
     */
    @Nullable Score getScore(@NonNull String owner, @NonNull String objective);

    /**
     * Sets {@linkplain Score a score} of {@linkplain Entity an entity} specified.
     *
     * @param entity the entity
     * @param objective a name of a scoreboard objective to set the score in
     * @param score a value that the score should be set to, {@code null} if score of the entity should be removed
     * @return a previous score of the entity, {@code null} if the entity did not have a score
     * @throws IllegalArgumentException if a scoreboard objective with the name specified does not exist
     *                                  or if the specified scoreboard objective name is invalid
     * @since 1.0
     */
    @Nullable Score setScore(@NonNull Entity entity, @NonNull String objective, @Nullable Score score);

    /**
     * Sets {@linkplain Score a score} of an owner with name specified.
     *
     * @param owner the owner name
     * @param objective a name of a scoreboard objective to set the score in
     * @param score a value that the score should be set to, {@code null} if score of the owner should be removed
     * @return a previous score of the owner, {@code null} if the owner did not have a score
     * @throws IllegalArgumentException if a scoreboard objective with the name specified does not exist
     *                                  or if the specified scoreboard objective name is invalid
     * @since 1.0
     */
    @Nullable Score setScore(@NonNull String owner, @NonNull String objective, @Nullable Score score);

    /**
     * Removes all {@linkplain Score scores} associated with the specified entity
     * from all {@linkplain ScoreboardObjective scoreboard objectives} registered
     * in this {@linkplain Scoreboard scoreboard}.
     *
     * @param entity the entity
     * @return a map that associates the names of scoreboard objectives (from which scores of the entity were removed)
     *         with the scores the entity held in these objectives prior to removal
     * @since 1.0
     */
    @NonNull Map<String, Score> removeScores(@NonNull Entity entity);

    /**
     * Removes all {@linkplain Score scores} associated with the specified owner
     * from all {@linkplain ScoreboardObjective scoreboard objectives} registered
     * in this {@linkplain Scoreboard scoreboard}.
     *
     * @param owner the name of the owner
     * @return a map that associates the names of scoreboard objectives (from which scores of the owner were removed)
     *         with the scores the owner held in these objectives prior to removal
     * @since 1.0
     */
    @NonNull Map<String, Score> removeScores(@NonNull String owner);

    /**
     * Gets copy of {@linkplain Map a map} which maps owner names to {@linkplain Score scores} that the owners have
     * in {@linkplain ScoreboardObjective a scoreboard objective} with name specified.
     *
     * @param objective the scoreboard objective name
     * @return the map copy
     * @throws IllegalArgumentException if a scoreboard objective with the name specified does not exist
     *                                  or if the specified scoreboard objective name is invalid
     * @since 1.0
     */
    @NonNull Map<String, Score> scores(@NonNull String objective);

    /**
     * Gets name of {@linkplain ScoreboardObjective a scoreboard objective} displayed
     * for some {@linkplain Player player} at {@linkplain ScoreboardPosition a scoreboard position} specified.
     *
     * @param player the player
     * @param position the scoreboard position
     * @return the scoreboard objective name, {@code null} if no scoreboard objective is displayed for the player
     *         at the specified scoreboard position
     * @throws IllegalArgumentException if the player specified is not a viewer of this scoreboard
     * @since 1.0
     */
    @Nullable String getDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position);

    /**
     * Sets {@linkplain ScoreboardObjective a scoreboard objective} that should be displayed
     * at some {@linkplain ScoreboardPosition scoreboard position} for {@linkplain Player a player} specified.
     *
     * @param player the player
     * @param position the scoreboard position
     * @param name a name of the scoreboard objective that should be displayed, {@code null} if no scoreboard
     *             objective should be displayed at the specified scoreboard position
     * @return a name of a previous scoreboard objective that was displayed for the player
     *         at the same position, {@code null} if none
     * @throws IllegalArgumentException if the player specified is not a viewer of this scoreboard or if
     *                                  the scoreboard objective name specified is not {@code null} and
     *                                  either no scoreboard objective with the same name was registered
     *                                  in this scoreboard or the name is invalid
     * @since 1.0
     */
    @Nullable String setDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position,
                                           @Nullable String name);

    /**
     * Gets copy of {@linkplain Set a set} of {@linkplain Player players}
     * that are viewers of this {@linkplain Scoreboard scoreboard}.
     *
     * @return the set
     * @since 1.0
     */
    @NonNull Set<? extends Player> viewers();
}