package net.hypejet.jet.server.entity.player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.player.PlayerChangeSettingsEvent;
import net.hypejet.jet.event.events.player.PlayerPongEvent;
import net.hypejet.jet.event.events.player.PlayerResourcePackResponseEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.network.packet.server.common.ServerPluginMessagePacket;
import net.hypejet.jet.pack.ResourcePackState;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.network.packet.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.network.packet.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.network.packet.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

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

    private static final Key ENTITY_TYPE = Key.key("player");
    private static final Key BRAND_PLUGIN_MESSAGE_IDENTIFIER = Key.key("brand");

    private final String username;
    private final SocketPlayerConnection connection;

    private @MonotonicNonNull Settings settings; // TODO: Thread safety
    private @MonotonicNonNull String clientBrand; // TODO: Thread safety

    /**
     * Constructs a {@linkplain JetPlayer player}.
     *
     * @param uniqueId a unique identifier of the player
     * @param username a username of the player
     * @param connection a connection of the player
     * @since 1.0
     */
    public JetPlayer(@NonNull UUID uniqueId, @NonNull String username, @NonNull SocketPlayerConnection connection) {
        super(ENTITY_TYPE, uniqueId, Pointers.builder()
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
    public @NonNull JetMinecraftServer server() {
        return this.connection.server();
    }

    @Override
    public void sendPluginMessage(@NonNull Key identifier, byte @NonNull [] data) {
        NullabilityUtil.requireNonNull(identifier, "identifier");
        NullabilityUtil.requireNonNull(data, "data");

        try (Acquisition<Session> ignoredAcquisition = this.connection.createOrReuseSessionAcquisition()) {
            // TODO: Do checks? Check whether the acquisition is actually necessary?
            this.sendPacket(new ServerPluginMessagePacket(identifier, data));
        }
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
        ServerPacket packet = switch (type) {
            case CHAT -> throw new IllegalArgumentException("Non-system messages are not supported yet");
            case SYSTEM -> new ServerSystemMessagePlayPacket(message, false);
        };
        this.sendPacket(packet);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        this.sendPacket(new ServerActionBarPlayPacket(message));
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        this.sendPacket(new ServerPlayerListHeaderAndFooterPlayPacket(header, footer));
    }

    /**
     * Updates {@linkplain Settings settings} of the player.
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
     * Sends a plugin message to a client containing a server brand.
     *
     * @param brand the server brand
     * @since 1.0
     */
    public void sendServerBrand(@NonNull String brand) {
        ByteBuf buf = Unpooled.buffer();
        StringNetworkCodec.INSTANCE.write(buf, brand);

        byte[] messageData = NetworkUtil.readRemainingBytes(buf);
        this.sendPluginMessage(BRAND_PLUGIN_MESSAGE_IDENTIFIER, messageData);

        buf.release();
    }

    /**
     * Sets a brand name of the client.
     *
     * @param clientBrand the brand name
     * @since 1.0
     */
    public void setClientBrand(@NonNull String clientBrand) {
        this.clientBrand = NullabilityUtil.requireNonNull(clientBrand, "client brand");
    }
}