package net.hypejet.jet.entity.player;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.client.configuration.ClientInformationConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Represents an {@linkplain Entity entity}, which is connected via {@linkplain PlayerConnection player connection}.
 *
 * @since 1.0
 * @author Codestech
 * @see Entity
 * @see PlayerConnection
 * @see CommandSource
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
     * Gets a {@linkplain PlayerConnection player connection} of the player.
     *
     * @return the player connection
     * @since 1.0-
     */
    @NonNull PlayerConnection connection();

    /**
     * A shortcut for {@link PlayerConnection#sendPacket(ServerPacket)}, which is accessed by {@link #connection()}.
     *
     * @param packet the packet
     * @since 1.0
     * @see PlayerConnection#sendPacket(ServerPacket)
     */
    void sendPacket(@NonNull ServerPacket packet);

    /**
     * A shortcut for {@link PlayerConnection#disconnect(Component)}, which is accessed by {@link #connection()}.
     *
     * @param reason the reason of disconnection
     * @since 1.0
     * @see PlayerConnection#disconnect(Component)
     */
    void disconnect(@NonNull Component reason);

    /**
     * Gets a {@linkplain Settings settings} of the player.
     *
     * @return the settings, {@code null} if not initialized yet
     * @since 1.0
     */
    @Nullable Settings settings();

    /**
     * Gets a client brand of the {@linkplain Player player}.
     *
     * @return the client brand, {@code null} if the client did not send it yet
     * @since 1.0
     */
    @Nullable String clientBrand();

    /**
     * Gets a {@linkplain MinecraftServer Minecraft server} that the player is connected to.
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
     * Represents a Minecraft chat mode setting of a {@linkplain Player player}.
     *
     * @since 1.0
     * @author Codestech
     * @see Player
     */
    enum ChatMode {
        /**
         * A chat mode used when the player allows all chat messages to be sent.
         *
         * @since 1.0
         */
        ENABLED,
        /**
         * A chat mode used when the player allows only chat messages from commands to be sent.
         *
         * @since 1.0
         */
        COMMANDS_ONLY,
        /**
         * A chat mode used when the does not allow any chat messages to be sent.
         *
         * @since 1.0
         */
        HIDDEN
    }

    /**
     * Represents a skin part of a {@linkplain Player player}.
     *
     * @since 1.0
     * @author Codestech
     * @see Player
     */
    enum SkinPart {
        /**
         * A skin part representing a cape.
         *
         * @since 1.0
         */
        CAPE,
        /**
         * A skin part representing a jacket.
         *
         * @since 1.0
         */
        JACKET,
        /**
         * A skin part representing a left sleeve.
         *
         * @since 1.0
         */
        LEFT_SLEEVE,
        /**
         * A skin part representing a right sleeve.
         *
         * @since 1.0
         */
        RIGHT_SLEEVE,
        /**
         * A skin part representing left pants.
         *
         * @since 1.0
         */
        LEFT_PANTS,
        /**
         * A skin part representing right pants.
         *
         * @since 1.0
         */
        RIGHT_PANTS,
        /**
         * A skin part representing a hat.
         *
         * @since 1.0
         */
        HAT
    }

    /**
     * Represents a Minecraft game mode of a {@linkplain Player player}.
     *
     * @since 1.0
     * @author Codestech
     */
    enum GameMode {
        /**
         * A survival game mode.
         *
         * @since 1.0
         */
        SURVIVAL,
        /**
         * A creative game mode.
         *
         * @since 1.0
         */
        CREATIVE,
        /**
         * An adventure game mode.
         *
         * @since 1.0
         */
        ADVENTURE,
        /**
         * A spectator game mode.
         *
         * @since 1.0
         */
        SPECTATOR
    }

    /**
     * Represents a settings of a {@linkplain Player player}.
     *
     * @param locale a locale of the player
     * @param viewDistance a view distance of the player
     * @param chatMode a chat mode of the player
     * @param chatColorsEnabled whether the chat colors are enabled by the player
     * @param enabledSkinParts parts of a skin of the player, which should be enabled
     * @param mainHand a main hand of the player
     * @param textFilteringEnabled whether the text filtering on signs and written books is enabled
     * @param allowServerListings whether the player should be listed on players lists
     * @since 1.0
     */
    record Settings(@NonNull Locale locale, byte viewDistance, Player.@NonNull ChatMode chatMode,
                    boolean chatColorsEnabled, @NonNull Collection<Player.SkinPart> enabledSkinParts,
                    Entity.@NonNull Hand mainHand, boolean textFilteringEnabled, boolean allowServerListings) {
        /**
         * Constructs the {@linkplain ClientInformationConfigurationPacket information configuration packet}.
         *
         * @param locale a locale of the player
         * @param viewDistance a view distance of the player
         * @param chatMode a chat mode of the player
         * @param chatColorsEnabled whether the chat colors are enabled by the player
         * @param enabledSkinParts parts of a skin of the player, which should be enabled
         * @param mainHand a main hand of the player
         * @param textFilteringEnabled whether the text filtering on signs and written books is enabled
         * @param allowServerListings whether the player should be listed on players lists
         * @since 1.0
         */
        public Settings {
            enabledSkinParts = Set.copyOf(enabledSkinParts);
        }
    }
}