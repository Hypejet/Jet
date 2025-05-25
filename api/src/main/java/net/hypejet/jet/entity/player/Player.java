package net.hypejet.jet.entity.player;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.acquisition.gamemode.GameModeAcquisition;
import net.hypejet.jet.entity.acquisition.gamemode.WriteGameModeAcquisition;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.scoreboard.exception.NoSuchObjectiveException;
import net.hypejet.jet.scoreboard.position.ScoreboardPosition;
import net.hypejet.jet.util.game.audience.CommonAudience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Represents {@linkplain Entity an entity}, which is connected via {@linkplain PlayerConnection player connection}.
 *
 * @since 1.0
 * @see Entity
 * @see PlayerConnection
 */
public interface Player extends Entity, CommandSource, CommonAudience {
    /**
     * Gets a username of the player.
     *
     * @return the username
     * @since 1.0
     */
    @NonNull String username();

    /**
     * Gets {@linkplain PlayerConnection a player connection} of the player.
     *
     * @return the player connection
     * @since 1.0
     */
    @NonNull PlayerConnection connection();

    /**
     * Creates {@linkplain NotNullObjectAcquisition a not-null object acquisition} of clientside
     * {@linkplain Settings settings} of the player.
     *
     * @return the not-null object acquisition
     * @since 1.0
     */
    @NonNull NotNullObjectAcquisition<Settings> settings();

    /**
     * Creates {@linkplain NotNullObjectAcquisition a not-null object acquisition} of a brand name of a client
     * associated with this {@linkplain Player player}.
     *
     * @return the not-null object acquisition
     * @since 1.0
     */
    @NonNull NotNullObjectAcquisition<String> clientBrand();

    /**
     * Gets {@linkplain MinecraftServer a Minecraft server} that the player is connected to.
     *
     * @return the Minecraft server
     * @since 1.0
     */
    @NonNull MinecraftServer server();

    /**
     * Creates {@linkplain GameModeAcquisition a game mode acquisition} of {@linkplain GameMode a game mode}
     * of this {@linkplain Player player}.
     *
     * @return the game mode acquisition
     * @since 1.0
     */
    @NonNull GameModeAcquisition acquireGameModeRead();

    /**
     * Creates {@linkplain WriteGameModeAcquisition a write game mode acquisition}
     * of {@linkplain GameMode a game mode} of this {@linkplain Player player}.
     *
     * @return the write game mode acquisition
     * @since 1.0
     */
    @NonNull WriteGameModeAcquisition acquireGameModeWrite();

    /**
     * Creates {@linkplain BooleanAcquisition a boolean acquisition}, which holds a value defining whether a respawn
     * screen is enabled for this {@linkplain Player player}.
     *
     * @return the boolean acquisition
     * @since 1.0
     */
    @NonNull BooleanAcquisition acquireRespawnScreenEnabledRead();

    /**
     * Creates {@linkplain WriteBooleanAcquisition a write boolean acquisition}, which holds a value defining
     * whether a respawn screen is enabled for this {@linkplain Player player}.
     *
     * @return the boolean acquisition
     * @since 1.0
     */
    @NonNull WriteBooleanAcquisition acquireRespawnScreenEnabledWrite();

    /**
     * Gets {@linkplain Scoreboard a scoreboard} displayed for this {@linkplain Player player}.
     *
     * @return the scoreboard
     * @since 1.0
     */
    @NonNull Scoreboard getScoreboard();

    /**
     * Sets {@linkplain Scoreboard a scoreboard} that should be displayed for this {@linkplain Player player}.
     *
     * @param scoreboard the scoreboard
     * @return a previous scoreboard that was displayed for the player
     * @since 1.0
     */
    @NonNull Scoreboard setScoreboard(@NonNull Scoreboard scoreboard);

    /**
     * Replaces {@linkplain Scoreboard a scoreboard} displayed for this {@linkplain Player player}.
     * The replacement is done only if scoreboard displayed for the player at time of calling the method
     * is the same as a value specified.
     *
     * @param scoreboard the value
     * @param newScoreboard a new scoreboard that should be displayed for the player
     * @return {@code true} if the scoreboard was replaced, {@code false} otherwise
     * @since 1.0
     */
    boolean replaceScoreboard(@NonNull Scoreboard scoreboard, @NonNull Scoreboard newScoreboard);

    /**
     * Gets {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective}
     * that is displayed for this {@linkplain Player player} at {@linkplain ScoreboardPosition a scoreboard position}
     * specified.
     *
     * @param position the scoreboard position
     * @return a name of the scoreboard objective, {@code null} if no scoreboard objective is displayed for the player
     *         at a scoreboard position specified
     * @since 1.0
     */
    @Nullable String getDisplayedObjective(@NonNull ScoreboardPosition position);

    /**
     * Sets {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective} that should be
     * displayed for this {@linkplain Player player} at {@linkplain ScoreboardPosition a scoreboard position}
     * specified.
     *
     * @param position the scoreboard position
     * @param name a name of the scoreboard objective
     * @return a name of a previous scoreboard objective that was displayed for the player at the scoreboard position
     *         specified, {@code null} if none
     * @throws NoSuchObjectiveException if no scoreboard objective with name specified was registered in a scoreboard
     *                                  visible for this player
     * @since 1.0
     */
    @Nullable String setDisplayedObjective(@NonNull ScoreboardPosition position, @NonNull String name);

    /**
     * Removes {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective}
     * from being displayed for this {@linkplain Player player}
     * at {@linkplain ScoreboardPosition a scoreboard position} specified.
     *
     * @param position the scoreboard position
     * @return a name of a scoreboard objective removed, {@code null} if no scoreboard objective was displayed
     *         at the scoreboard position specified
     * @since 1.0
     */
    @Nullable String removeDisplayedObjective(@NonNull ScoreboardPosition position);

    /**
     * Removes {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective}
     * from being displayed for this {@linkplain Player player}
     * at {@linkplain ScoreboardPosition a scoreboard position} specified.
     * The removal is done only if name of the scoreboard objective is equal to a value specified.
     *
     * @param position the scoreboard position
     * @param name the value
     * @return {@code true} if the scoreboard objective was removed from being displayed, {@code false otherwise}
     * @since 1.0
     */
    boolean removeDisplayedObjective(@NonNull ScoreboardPosition position, @NonNull String name);

    /**
     * Replaces {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective}
     * displayed for this {@linkplain Player player} at {@linkplain ScoreboardPosition a scoreboard position}
     * specified. The replacement is done only if name of a scoreboard objective displayed at the scoreboard position
     * at time of calling the method is equal to a value specified.
     *
     * @param position the scoreboard position
     * @param name the value
     * @param newName a name of a new scoreboard objective that should be displayed
     * @return {@code true} if the scoreboard objective displayed was replaced, {@code false} otherwise
     * @throws NoSuchObjectiveException if no scoreboard objective with the new name specified was registered
     *                                  in a scoreboard visible for this player
     * @since 1.0
     */
    boolean replaceDisplayedObjective(@NonNull ScoreboardPosition position,
                                      @NonNull String name, @NonNull String newName);

    /**
     * Represents a Minecraft chat mode setting of {@linkplain Player a player}.
     *
     * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
     * example.</p>
     *
     * @since 1.0
     * @see Player
     */
    final class ChatMode {
        /**
         * A chat mode used when a player allows all chat messages to be sent.
         *
         * @since 1.0
         */
        public static final ChatMode ENABLED = new ChatMode("enabled");

        /**
         * A chat mode used when a player allows only chat messages from commands to be sent.
         *
         * @since 1.0
         */
        public static final ChatMode COMMANDS_ONLY = new ChatMode("commands only");

        /**
         * A chat mode used when a player does not allow any chat messages to be sent.
         *
         * @since 1.0
         */
        public static final ChatMode HIDDEN = new ChatMode("hidden");

        private final String name;

        private ChatMode(@NonNull String name) {
            this.name = NullabilityUtil.requireNonNull(name, "name");
        }

        /**
         * Gets a readable lower-case name of this chat mode.
         *
         * @return the name
         * @since 1.0
         */
        public @NonNull String name() {
            return this.name;
        }

        /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
           since all instances are defined in constants of this class. */

        @Override
        public String toString() {
            return "ChatMode{" +
                    "name='" + this.name + '\'' +
                    '}';
        }
    }

    /**
     * Represents skin part of {@linkplain Player a player}.
     *
     * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
     * example.</p>
     *
     * @since 1.0
     * @see Player
     */
    final class SkinPart {
        /**
         * A skin part representing a cape.
         *
         * @since 1.0
         */
        public static final SkinPart CAPE = new SkinPart("cape");

        /**
         * A skin part representing a jacket.
         *
         * @since 1.0
         */
        public static final SkinPart JACKET = new SkinPart("jacket");

        /**
         * A skin part representing a left sleeve.
         *
         * @since 1.0
         */
        public static final SkinPart LEFT_SLEEVE = new SkinPart("left sleeve");

        /**
         * A skin part representing a right sleeve.
         *
         * @since 1.0
         */
        public static final SkinPart RIGHT_SLEEVE = new SkinPart("right sleeve");

        /**
         * A skin part representing left pants.
         *
         * @since 1.0
         */
        public static final SkinPart LEFT_PANTS = new SkinPart("left pants");

        /**
         * A skin part representing right pants.
         *
         * @since 1.0
         */
        public static final SkinPart RIGHT_PANTS = new SkinPart("right pants");

        /**
         * A skin part representing a hat.
         *
         * @since 1.0
         */
        public static final SkinPart HAT = new SkinPart("hat");

        private final String name;

        private SkinPart(@NonNull String name) {
            this.name = NullabilityUtil.requireNonNull(name, "name");
        }

        /**
         * Gets a readable lower-case name of this skin part.
         *
         * @return the name
         * @since 1.0
         */
        public @NonNull String name() {
            return this.name;
        }

        /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
           since all instances are defined in constants of this class. */

        @Override
        public String toString() {
            return "SkinPart{" +
                    "name='" + this.name + '\'' +
                    '}';
        }
    }

    /**
     * Represents a Minecraft game mode of {@linkplain Player a player}.
     *
     * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
     * example.</p>
     *
     * @since 1.0
     * @see Player
     */
    final class GameMode {
        /**
         * A survival game mode.
         *
         * @since 1.0
         */
        public static final GameMode SURVIVAL = new GameMode("survival");

        /**
         * A creative game mode.
         *
         * @since 1.0
         */
        public static final GameMode CREATIVE = new GameMode("creative");

        /**
         * An adventure game mode.
         *
         * @since 1.0
         */
        public static final GameMode ADVENTURE = new GameMode("adventure");

        /**
         * A spectator game mode.
         *
         * @since 1.0
         */
        public static final GameMode SPECTATOR = new GameMode("spectator");

        private final String name;

        private GameMode(@NonNull String name) {
            this.name = NullabilityUtil.requireNonNull(name, "name");
        }

        /**
         * Gets a readable lower-case name of this game mode.
         *
         * @return the name
         * @since 1.0
         */
        public @NonNull String name() {
            return this.name;
        }

        /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
           since all instances are defined in constants of this class. */

        @Override
        public String toString() {
            return "GameMode{" +
                    "name='" + this.name + '\'' +
                    '}';
        }
    }

    /**
     * Represents a particle settings of {@linkplain Player a player}.
     *
     * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
     * example.</p>
     *
     * @since 1.0
     * @see Player
     */
    final class ParticleStatus {
        /**
         * A particle status indicating that all particles should be shown.
         *
         * @since 1.0
         */
        public static final ParticleStatus ALL = new ParticleStatus("all");

        /**
         * A particle status indicating that decreased amount of particles should be shown.
         *
         * @since 1.0
         */
        public static final ParticleStatus DECREASED = new ParticleStatus("decreased");

        /**
         * A particle status indicating that minimal amount of particles should be shown.
         *
         * @since 1.0
         */
        public static final ParticleStatus MINIMAL = new ParticleStatus("minimal");


        private final String name;

        private ParticleStatus(@NonNull String name) {
            this.name = NullabilityUtil.requireNonNull(name, "name");
        }

        /**
         * Gets a readable lower-case name of this particle status.
         *
         * @return the name
         * @since 1.0
         */
        public @NonNull String name() {
            return this.name;
        }

        /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
           since all instances are defined in constants of this class. */

        @Override
        public String toString() {
            return "ParticleStatus{" +
                    "name='" + this.name + '\'' +
                    '}';
        }
    }

    /**
     * Represents a settings of {@linkplain Player a player}.
     *
     * @param locale a locale of the player
     * @param viewDistance a view distance of the player
     * @param chatMode a chat mode of the player
     * @param chatColorsEnabled whether the chat colors are enabled by the player
     * @param enabledSkinParts parts of a skin of the player, which should be enabled
     * @param mainHand a main hand of the player
     * @param textFilteringEnabled whether the text filtering on signs and written books is enabled
     * @param allowServerListings whether the player should be listed on players lists
     * @param particleStatus a particle settings of the player
     * @since 1.0
     */
    record Settings(@NonNull Locale locale, byte viewDistance, Player.@NonNull ChatMode chatMode,
                    boolean chatColorsEnabled, @NonNull Collection<Player.SkinPart> enabledSkinParts,
                    Entity.@NonNull Hand mainHand, boolean textFilteringEnabled, boolean allowServerListings,
                    @NonNull ParticleStatus particleStatus) {
        /**
         * Constructs the {@linkplain Settings settings}.
         *
         * @param locale a locale of the player
         * @param viewDistance a view distance of the player
         * @param chatMode a chat mode of the player
         * @param chatColorsEnabled whether the chat colors are enabled by the player
         * @param enabledSkinParts parts of a skin of the player, which should be enabled
         * @param mainHand a main hand of the player
         * @param textFilteringEnabled whether the text filtering on signs and written books is enabled
         * @param allowServerListings whether the player should be listed on players lists
         * @param particleStatus a particle settings of the player
         * @since 1.0
         */
        public Settings {
            enabledSkinParts = Set.copyOf(enabledSkinParts);
        }
    }
}