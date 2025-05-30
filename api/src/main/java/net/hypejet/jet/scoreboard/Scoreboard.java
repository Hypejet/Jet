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
     * @param score a value that the score should be set to, {@code null} if score of the entity should be removed
     * @return a previous score of the entity, {@code null} if the entity did not have a score
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
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
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    @Nullable Score setScore(@NonNull String owner, @NonNull String objective, @Nullable Score score);

    /**
     * Sets {@linkplain Score a score} of some {@linkplain Entity entity} only if score of the entity
     * at time of calling the method is {@linkplain Object#equals(Object) equal} to a value specified.
     *
     * @param entity the entity
     * @param objective a name of a scoreboard objective to set the score in
     * @param expectedScore the value, {@code null} if it is expected that the entity does not have a score
     *                      in the scoreboard objective at time of calling the method
     * @param newScore a value that the score should be set to, {@code null} if score of the entity should be removed
     * @return {@code true} if the score has been replaced, {@code false} otherwise
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    boolean replaceScore(@NonNull Entity entity, @NonNull String objective,
                         @Nullable Score expectedScore, @Nullable Score newScore);

    /**
     * Sets {@linkplain Score a score} of some owner only if score of the owner at time
     * of calling the method is {@linkplain Object#equals(Object) equal} to a value specified.
     *
     * @param owner a name of the owner
     * @param objective a name of a scoreboard objective to set the score in
     * @param expectedScore the value, {@code null} if it is expected that the owner does not have a score
     *                      in the scoreboard objective at time of calling the method
     * @param newScore a value that the score should be set to, {@code null} if score of the owner should be removed
     * @return {@code true} if the score has been replaced, {@code false} otherwise
     * @throws NoSuchObjectiveException if a scoreboard objective with the name specified does not exist
     * @since 1.0
     */
    boolean replaceScore(@NonNull String owner, @NonNull String objective,
                         @Nullable Score expectedScore, @Nullable Score newScore);

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

    /**
     * Gets name of {@linkplain ScoreboardObjective a scoreboard objective} displayed
     * for some {@linkplain Player player} at {@linkplain ScoreboardPosition a scoreboard position} specified.
     *
     * @param player the player
     * @param position the scoreboard position
     * @return the scoreboard objective name, {@code null} if no scoreboard objective is displayed for the player
     *         at the scoreboard position
     * @throws NotViewerException if the player specified is not a viewer of this scoreboard
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
     *             objective should be displayed at the scoreboard position
     * @return a name of a previous scoreboard objective that was displayed for the player
     *         at the same position, {@code null} if none
     * @throws NotViewerException if the player specified is not a viewer of this scoreboard
     * @throws NoSuchObjectiveException if the scoreboard objective name specified is not {@code null}
     *                                  and no scoreboard objective with the same name was registered
     *                                  in this scoreboard
     * @since 1.0
     */
    @Nullable String setDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position,
                                           @Nullable String name);

    /**
     * Replaces {@linkplain ScoreboardObjective a scoreboard objective} displayed
     * at some {@linkplain ScoreboardPosition scoreboard position} for {@linkplain Player a player} specified.
     * The replacement is done only if name of a scoreboard objective displayed for the player at the same scoreboard
     * position at time of calling the method is equal to a value specified.
     *
     * @param player the player
     * @param position the scoreboard position
     * @param name the value, {@code null} if it is expected that no scoreboard objective is displayed
     *             for the player at the scoreboard position at time of calling the method
     * @param newName a name of a new scoreboard objective that should be displayed for the player
     *                at the scoreboard position, {@code null} if no scoreboard objective should be displayed
     *                at that scoreboard position
     * @return {@code true} if the scoreboard objective displayed was replaced, {@code false} otherwise
     * @throws NotViewerException if the player specified is not a viewer of this scoreboard
     * @throws NoSuchObjectiveException if the new scoreboard objective name specified is not {@code null}
     *                                  and no scoreboard objective with the same name was registered
     *                                  in this scoreboard
     * @since 1.0
     */
    boolean replaceDisplayedObjective(@NonNull Player player, @NonNull ScoreboardPosition position,
                                      @Nullable String name, @Nullable String newName);

    /**
     * Gets copy of {@linkplain Set a set} of {@linkplain Player players}
     * that see this {@linkplain Scoreboard scoreboard}.
     *
     * @return the set
     * @since 1.0
     */
    @NonNull Set<? extends Player> viewers();
}