package net.hypejet.jet.scoreboard;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.scoreboard.exception.NoSuchObjectiveException;
import net.hypejet.jet.scoreboard.exception.NotViewerException;
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
 * @since 1.0
 */
public interface Scoreboard {
    /**
     * Gets {@linkplain ScoreboardObjective a scoreboard objective} registered
     * in this {@linkplain Scoreboard scoreboard}.
     *
     * @param name the name of the scoreboard objective
     * @return the scoreboard objective, {@code null} if no scoreboard objective with the specified name
     *         was registered in this scoreboard
     * @since 1.0
     */
    @Nullable ScoreboardObjective getObjective(@NonNull String name);

    /**
     * Sets the specified {@linkplain ScoreboardObjective scoreboard objective} to have the specified data.
     *
     * @param name the name of the scoreboard objective
     * @param objective the data, {@code null} if the scoreboard objective should be removed along
     *                  with all scores associated with it
     * @return a previous data that the scoreboard objective had, {@code null} if the scoreboard objective
     *         did not exist and the method call initialized the scoreboard objective
     * @since 1.0
     */
    @Nullable ScoreboardObjective setObjective(@NonNull String name, @Nullable ScoreboardObjective objective);

    /**
     * Replaces data of the specified {@linkplain ScoreboardObjective scoreboard objective}.
     *
     * <p>The replacement is done only if data of the scoreboard objective
     * at time of calling the method is {@linkplain Object#equals(Object) equal} to the specified value.</p>
     *
     * <p>If the expected value is specified as {@code null} and the requirement is satisfied,
     * the scoreboard objective is initialized with the replacement value, but only if the replacement
     * is not {@code null}.</p>
     *
     * @param name the name of the scoreboard objective
     * @param expectedObjective the value, {@code null} if it is expected that the scoreboard objective
     *                          is not initialized at time of calling the method
     * @param newObjective the data that data of the scoreboard objective should be replaced with, {@code null} if
     *                     the scoreboard objective should be removed along with all scores associated with it
     * @return {@code true} if the scoreboard objective data was replaced, {@code false} otherwise
     * @since 1.0
     */
    boolean replaceObjective(@NonNull String name, @Nullable ScoreboardObjective expectedObjective,
                             @Nullable ScoreboardObjective newObjective);

    /**
     * Gets a copy of {@linkplain Map map} associating the names
     * of {@linkplain ScoreboardObjective scoreboard objectives} with these scoreboard objectives.
     *
     * @return the map copy
     * @since 1.0
     */
    @NonNull Map<String, ScoreboardObjective> objectives();

    /**
     * Gets {@linkplain Score a score} of the specified {@linkplain Entity entity}.
     *
     * @param entity the entity
     * @param objective the name of a scoreboard objective to get the score from
     * @return the score, {@code null} if the entity has no score in the specified scoreboard objective
     * @throws NoSuchObjectiveException if no scoreboard objective with the specified name was registered
     * @since 1.0
     */
    @Nullable Score getScore(@NonNull Entity entity, @NonNull String objective);

    /**
     * Gets {@linkplain Score a score} of the specified owner.
     *
     * @param owner the name of the owner
     * @param objective the name of a scoreboard objective to get the score from
     * @return the score, {@code null} if the owner has no score in the specified scoreboard objective
     * @throws NoSuchObjectiveException if no scoreboard objective with the specified name was registered
     * @since 1.0
     */
    @Nullable Score getScore(@NonNull String owner, @NonNull String objective);

    /**
     * Replaces {@linkplain Score a score} of the specified {@linkplain Entity entity}.
     *
     * @param entity the entity
     * @param objective the name of the scoreboard objective to replace the score in
     * @param score a value that the score should be replaced to, {@code null} if the score should be removed
     * @return a score the entity held prior to replacement in the specified scoreboard objective, {@code null} if
     *         the entity did not have a score in that scoreboard objective
     * @throws NoSuchObjectiveException if no scoreboard objective with the specified name was registered
     * @since 1.0
     */
    @Nullable Score setScore(@NonNull Entity entity, @NonNull String objective, @Nullable Score score);

    /**
     * Replaces {@linkplain Score a score} of the specified owner.
     *
     * @param owner the name of the owner
     * @param objective the name of the scoreboard objective to replace the score in
     * @param score a value that the score should be replaced to, {@code null} if the score should be removed
     * @return a score the owner held prior to replacement in the specified scoreboard objective, {@code null} if
     *         the owner did not have a score in that scoreboard objective
     * @throws NoSuchObjectiveException if no scoreboard objective with the specified name was registered
     * @since 1.0
     */
    @Nullable Score setScore(@NonNull String owner, @NonNull String objective, @Nullable Score score);

    /**
     * Replaces {@linkplain Score a score} of the specified {@linkplain Entity entity}.
     *
     * <p>The replacement is done only if score of the entity at time of calling the method
     * is {@linkplain Object#equals(Object) equal} to the specified value.</p>
     *
     * <p>If the expected value is specified as {@code null} and the requirement is satisfied,
     * the score is initialized with the replacement value, but only if the replacement is not {@code null}.</p>
     *
     * @param entity the entity
     * @param objective the name of the scoreboard objective to replace the score in
     * @param expectedScore the value, {@code null} if it is expected that the entity does not have a score
     *                      in the scoreboard objective at time of calling the method
     * @param newScore a value that the score should be set to, {@code null} if score of the entity should be removed
     * @return {@code true} if the score has been replaced, {@code false} otherwise
     * @throws NoSuchObjectiveException if no scoreboard objective with the specified name was registered
     * @since 1.0
     */
    boolean replaceScore(@NonNull Entity entity, @NonNull String objective,
                         @Nullable Score expectedScore, @Nullable Score newScore);

    /**
     * Replaces {@linkplain Score a score} of the specified owner.
     *
     * <p>The replacement is done only if score of the owner at time of calling the method
     * is {@linkplain Object#equals(Object) equal} to the specified value.</p>
     *
     * <p>If the expected value is specified as {@code null} and the requirement is satisfied,
     * the score is initialized with the replacement value, but only if the replacement is not {@code null}.</p>
     *
     * @param owner the name of the owner
     * @param objective the name of the scoreboard objective to replace the score in
     * @param expectedScore the value, {@code null} if it is expected that the owner does not have a score
     *                      in the scoreboard objective at time of calling the method
     * @param newScore a value that the score should be set to, {@code null} if score of the owner should be removed
     * @return {@code true} if the score has been replaced, {@code false} otherwise
     * @throws NoSuchObjectiveException if no scoreboard objective with the specified name was registered
     * @since 1.0
     */
    boolean replaceScore(@NonNull String owner, @NonNull String objective,
                         @Nullable Score expectedScore, @Nullable Score newScore);

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
     * Gets copy of {@linkplain Map a map} associating owner names with {@linkplain Score scores} that the owners have
     * in the specified {@linkplain ScoreboardObjective scoreboard objective}.
     *
     * @param objective the name of the scoreboard objective
     * @return the map copy
     * @throws NoSuchObjectiveException if no scoreboard objective with the specified name was registered
     * @since 1.0
     */
    @NonNull Map<String, Score> scores(@NonNull String objective);

    /**
     * Gets the name of {@linkplain ScoreboardObjective a scoreboard objective} displayed
     * for the given {@linkplain Player player} at the specified {@linkplain ScoreboardPosition scoreboard position}.
     *
     * @param player the player
     * @param position the scoreboard position
     * @return the scoreboard objective name, {@code null} if no scoreboard objective is displayed for the player
     *         at the specified scoreboard position
     * @throws NotViewerException if the specified player is not a viewer of this scoreboard
     * @since 1.0
     */
    @Nullable String getDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position);

    /**
     * Replaces {@linkplain ScoreboardObjective a scoreboard objective} displayed
     * for the given {@linkplain Player player} at the specified {@linkplain ScoreboardPosition scoreboard position}.
     *
     * @param player the player
     * @param position the scoreboard position
     * @param name the name of the scoreboard objective that the displayed scoreboard objective should be replaced
     *             to, {@code null} if no scoreboard objective should be displayed at the scoreboard position
     * @return the name of a previous scoreboard objective that was displayed for the player
     *         at the specified scoreboard position, {@code null} if none
     * @throws NotViewerException if the specified player is not a viewer of this scoreboard
     * @throws NoSuchObjectiveException if the specified scoreboard objective name is not {@code null}
     *                                  and no scoreboard objective with the same name was registered
     * @since 1.0
     */
    @Nullable String setDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position,
                                           @Nullable String name);

    /**
     * Replaces {@linkplain ScoreboardObjective a scoreboard objective} displayed
     * for the given {@linkplain Player player} at the specified {@linkplain ScoreboardPosition scoreboard position}.
     *
     * <p>The replacement is done only if the name of a scoreboard objective displayed for the player
     * at the same scoreboard position at time of calling the method is {@linkplain Object#equals(Object) equal}
     * to the specified value.</p>
     *
     * @param player the player
     * @param position the scoreboard position
     * @param name the value, {@code null} if it is expected that no scoreboard objective is displayed
     *             for the player at the scoreboard position at time of calling the method
     * @param newName a name of the scoreboard objective that the scoreboard objective should be replaced to,
     *                {@code null} if no scoreboard objective should be displayed the specified scoreboard position
     * @return {@code true} if the scoreboard objective displayed was replaced, {@code false} otherwise
     * @throws NotViewerException if the specified player is not a viewer of this scoreboard
     * @throws NoSuchObjectiveException if the specified name of the scoreboard objective that the displayed scoreboard
     *                                  objective should be replaced to is not {@code null} and no scoreboard objective
     *                                  with the same name was registered
     * @since 1.0
     */
    boolean replaceDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position,
                                      @Nullable String name, @Nullable String newName);

    /**
     * Gets a copy of {@linkplain Set a set} of {@linkplain Player players}
     * that see this {@linkplain Scoreboard scoreboard}.
     *
     * @return the set copy
     * @since 1.0
     */
    @NonNull Set<? extends Player> viewers();
}