package net.hypejet.jet.server.entity.player;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.data.entity.type.BuiltInEntityTypes;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.player.PlayerChangeClientBrandEvent;
import net.hypejet.jet.event.events.player.PlayerChangeSettingsEvent;
import net.hypejet.jet.event.events.player.PlayerCookieResponseEvent;
import net.hypejet.jet.event.events.player.PlayerPluginMessageEvent;
import net.hypejet.jet.event.events.player.PlayerPongEvent;
import net.hypejet.jet.event.events.player.PlayerResourcePackResponseEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.pack.DataPack;
import net.hypejet.jet.pack.ResourcePackResult;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an implementation of {@linkplain Player player}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player
 * @see JetEntity
 */
public final class JetPlayer extends JetEntity implements Player {

    private static final Key BRAND_PLUGIN_MESSAGE_IDENTIFIER = Key.key("brand");

    private final String username;
    private final SocketPlayerConnection connection;

    private @MonotonicNonNull Settings settings;
    private @MonotonicNonNull String clientBrand;

    private Collection<DataPack> knownPacks = Collections.emptySet();

    /**
     * Constructs a {@linkplain JetPlayer player}.
     *
     * @param uniqueId an unique identifier of the player
     * @param username a username of the player
     * @param connection a connection of the player
     * @since 1.0
     */
    public JetPlayer(@NonNull UUID uniqueId, @NonNull String username, @NonNull SocketPlayerConnection connection) {
        super(BuiltInEntityTypes.PLAYER, uniqueId, Pointers.builder()
                .withStatic(Identity.UUID, uniqueId)
                .withStatic(Identity.NAME, username)
                .build());

        this.username = username;
        this.connection = connection;
    }

    @Override
    public @NonNull String username() {
        return this.username;
    }

    @Override
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    @Override
    public void sendPacket(@NonNull ServerPacket packet) {
        this.connection.sendPacket(packet);
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        this.connection.disconnect(reason);
    }

    @Override
    public @Nullable Settings settings() {
        return this.settings;
    }

    @Override
    public @Nullable String clientBrand() {
        return this.clientBrand;
    }

    @Override
    public @NonNull Collection<DataPack> knownDataPacks() {
        return this.knownPacks;
    }

    @Override
    public @NonNull MinecraftServer server() {
        return this.connection.server();
    }

    /**
     * Updates {@linkplain Player.Settings settings} of the player.
     *
     * @param settings the new settings
     * @since 1.0
     */
    public void settings(@NonNull Settings settings) {
        Objects.requireNonNull(settings, "The settings must not be null");

        PlayerChangeSettingsEvent event = new PlayerChangeSettingsEvent(this, settings);
        this.server().eventNode().call(event);
        if (event.isCancelled()) return;

        this.settings = settings;
    }

    /**
     * Updates known {@linkplain DataPack data packs} of the player.
     *
     * @param knownPacks the data packs
     * @since 1.0
     * @see DataPack
     */
    public void knownDataPacks(@NonNull Collection<DataPack> knownPacks) {
        Objects.requireNonNull(knownPacks, "The known packs must not be null");
        this.knownPacks = knownPacks;
    }

    /**
     * Handles a plugin message sent by the player.
     *
     * @param identifier an identifier of the plugin message
     * @param data a data of the plugin message
     * @since 1.0
     */
    public void handlePluginMessage(@NonNull Key identifier, byte @NonNull [] data) {
        Objects.requireNonNull(identifier, "The identifier must not be null");
        Objects.requireNonNull(data, "The data must not be null");

        EventNode<Object> eventNode = this.server().eventNode();
        eventNode.call(new PlayerPluginMessageEvent(this, identifier, data));

        if (identifier.equals(BRAND_PLUGIN_MESSAGE_IDENTIFIER)) {
            String clientBrand = new String(data, StandardCharsets.UTF_8);
            eventNode.call(new PlayerChangeClientBrandEvent(this, clientBrand));
            this.clientBrand = clientBrand;
        }
    }

    /**
     * Handles a cookie response sent by the player.
     *
     * @param identifier an identifier of the cookie
     * @param data a data of the cookie
     * @since 1.0
     */
    public void handleCookieResponse(@NonNull Key identifier, byte @Nullable [] data) {
        Objects.requireNonNull(identifier, "The identifier must not be null");

        EventNode<Object> eventNode = this.server().eventNode();
        eventNode.call(new PlayerCookieResponseEvent(this, identifier, data));
    }

    /**
     * Handles a resource pack response.
     *
     * @param uniqueId an unique identifier of the resource pack
     * @param result a result of the resource pack
     * @since 1.0
     */
    public void handleResourcePackResponse(@NonNull UUID uniqueId, @NonNull ResourcePackResult result) {
        Objects.requireNonNull(uniqueId, "The unique identifier must not be null");
        Objects.requireNonNull(result, "The result must not be null");

        EventNode<Object> eventNode = this.server().eventNode();
        eventNode.call(new PlayerResourcePackResponseEvent(this, uniqueId, result));
    }

    /**
     * Handles a response for a ping packet.
     *
     * @param pingIdentifier an identifier of the ping
     * @since 1.0
     */
    public void handlePong(int pingIdentifier) {
        EventNode<Object> eventNode = this.server().eventNode();
        eventNode.call(new PlayerPongEvent(this, pingIdentifier));
    }
}