package net.hypejet.jet.server.entity.player;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.player.PlayerChangeClientBrandEvent;
import net.hypejet.jet.event.events.player.PlayerChangeSettingsEvent;
import net.hypejet.jet.event.events.player.PlayerPluginMessageEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.entity.JetEntity;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.charset.StandardCharsets;
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
    private final PlayerConnection connection;

    private @MonotonicNonNull Settings settings;
    private @MonotonicNonNull String clientBrand;

    /**
     * Constructs a {@linkplain JetPlayer player}.
     *
     * @param uniqueId an unique identifier of the player
     * @param username a username of the player
     * @param connection a connection of the player
     * @since 1.0
     */
    public JetPlayer(@NonNull UUID uniqueId, @NonNull String username, @NonNull PlayerConnection connection) {
        super(EntityType.PLAYER, uniqueId);

        this.username = username;
        this.connection = connection;
    }

    @Override
    public @NonNull String username() {
        return this.username;
    }

    @Override
    public @NonNull PlayerConnection connection() {
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
    public @NonNull MinecraftServer server() {
        return this.connection.server();
    }

    @Override
    public void applyPointers(Pointers.@NonNull Builder pointers) {
        pointers.withStatic(Identity.NAME, this.username);
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
}