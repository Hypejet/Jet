package net.hypejet.jet.entity.player;

import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.world.World;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

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
public interface Player extends Entity, CommandSource {
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
     * A shortcut for {@link PlayerConnection#disconnect(Component)}, which is accessed by {@link #connection()}.
     *
     * @param reason the reason of disconnection
     * @since 1.0
     * @see PlayerConnection#disconnect(Component)
     */
    void disconnect(@NonNull Component reason);

    /**
     * Creates {@linkplain NullableObjectAcquisition a nullable object acquisition} holding
     * {@linkplain Settings settings} of the player.
     *
     * @return the acquisition, whose object is {@code null} if the settings were not initialized yet
     * @since 1.0
     */
    @NonNull NullableObjectAcquisition<Settings> settings();

    /**
     * Creates {@linkplain NullableObjectAcquisition a nullable object acquisition} holding client brand of the
     * {@linkplain Player player}.
     *
     * @return the acquisition, whose object is {@code null} if the client did not send it yet
     * @since 1.0
     */
    @NonNull NullableObjectAcquisition<String> clientBrand();

    /**
     * Gets {@linkplain MinecraftServer a Minecraft server} that the player is connected to.
     *
     * @return the Minecraft server
     * @since 1.0
     */
    @NonNull MinecraftServer server();

    /**
     * Sends a plugin message to the player.
     *
     * @param identifier an identifier of the plugin message
     * @param data a data of the plugin message
     * @since 1.0
     */
    void sendPluginMessage(@NonNull Key identifier, byte @NonNull [] data);

    /**
     * Creates {@linkplain NullableObjectAcquisition a nullable object acquisition} of {@linkplain World a world}
     * that this {@linkplain Player player} is currently in.
     *
     * @return the nullable object acquisition, which holds the world or {@code null} if the world has not been set yet
     * @since 1.0
     */
    @NonNull NullableObjectAcquisition<? extends World> getWorld();

    /**
     * Changes {@linkplain World a world} that this {@linkplain Player player} is in and keeps attributes and entity
     * metadata of the player after the change.
     *
     * @param world the world
     * @param position an initial position that the player should be teleported to
     * @throws IllegalArgumentException if the player is already in the world specified
     * @since 1.0
     * @see #setWorld(World, Position, boolean, boolean)
     */
    void setWorld(@NonNull World world, @NonNull Position position);

    /**
     * Changes {@linkplain World a world} that this {@linkplain Player player} is in.
     *
     * <p>
     *     Note that the world change can be <strong>cancelled</strong> and the world
     *     the player gets finally teleported to can be <strong>changed</strong> in
     *     {@linkplain net.hypejet.jet.event.events.world.PreWorldSwitchEvent a pre-world-switch event}.
     * </p>
     *
     * <p>
     *     If a check for these changed is needed, it can be done by {@linkplain #getWorld() read-acquiring} a world
     *     that the player is in before setting the world and then getting and comparing it just after the change.
     * </p>
     *
     * @param world the world
     * @param position an initial position that the player should be teleported to
     * @param keepAttributes whether attributes of the player should be kept after the change
     * @param keepMetadata whether entity metadata of the player should be kept after the change
     * @throws IllegalArgumentException if the player is already in the world specified
     * @since 1.0
     */
    void setWorld(@NonNull World world, @NonNull Position position, boolean keepAttributes, boolean keepMetadata);

    /**
     * Creates {@linkplain NullableObjectAcquisition a nullable object acquisition}
     * of {@linkplain GameMode a game mode} of this {@linkplain Player player}.
     *
     * @return the nullable object acquisition, which holds the game mode or {@code null}, if the game mode
     *         has not been set yet
     * @since 1.0
     */
    @NonNull NullableObjectAcquisition<GameMode> getGameMode();

    /**
     * Sets {@linkplain GameMode a game mode} for this {@linkplain Player player}.
     *
     * @param gameMode a game mode that the player should have
     * @since 1.0
     */
    void setGameMode(@NonNull GameMode gameMode);

    /**
     * Creates {@linkplain BooleanAcquisition a boolean acquisition}, which holds a value defining whether a respawn
     * screen is enabled for this {@linkplain Player player}.
     *
     * @return the boolean acquisition
     * @since 1.0
     */
    @NonNull BooleanAcquisition respawnScreenEnabled();

    /**
     * Sets whether a respawn screen should be enabled for this {@linkplain Player player}.
     *
     * @param enabled {@code true} if the respawn screen should be enabled, {@code false} otherwise
     * @since 1.0
     */
    void setRespawnScreenEnabled(boolean enabled);

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