package net.hypejet.jet.server.entity.player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.object.nullable.WriteNullableObjectAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.player.PlayerChangeSettingsEvent;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPluginMessagePacket;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacketRegistry;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an implementation of {@linkplain Player a player}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player
 * @see JetEntity
 */
public final class JetPlayer extends JetEntity implements Player {

    private static final Key ENTITY_TYPE = Key.key("player");
    private static final Key BRAND_PLUGIN_MESSAGE_IDENTIFIER = Key.key("brand");

    private final SocketPlayerConnection connection;

    private final NullableObjectAcquirable<Settings> settings = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<String> clientBrand = new NullableObjectAcquirable<>();

    /**
     * Constructs the {@linkplain JetPlayer player}.
     *
     * @param uniqueId a unique identifier of the player
     * @param username a username of the player
     * @param connection a connection of the player
     * @since 1.0
     */
    public JetPlayer(@NonNull UUID uniqueId, @NonNull String username, @NonNull SocketPlayerConnection connection) {
        super(ENTITY_TYPE, uniqueId, Pointers.builder()
                .withStatic(Identity.UUID, NullabilityUtil.requireNonNull(uniqueId, "unique identifier"))
                .withStatic(Identity.NAME, NullabilityUtil.requireNonNull(username, "username"))
                .build());
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
    }

    @Override
    public @NonNull String username() {
        return this.pointers().get(Identity.NAME).orElseThrow();
    }

    @Override
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        this.connection.disconnect(reason);
    }

    @Override
    public @NonNull NullableObjectAcquisition<Settings> settings() {
        return this.settings.acquireRead();
    }

    @Override
    public @NonNull NullableObjectAcquisition<String> clientBrand() {
        return this.clientBrand.acquireRead();
    }

    @Override
    public @NonNull JetMinecraftServer server() {
        return this.connection.server();
    }

    @Override
    public void sendPluginMessage(@NonNull Key identifier, byte @NonNull [] data) {
        NullabilityUtil.requireNonNull(identifier, "identifier");
        NullabilityUtil.requireNonNull(data, "data");

        try (NotNullObjectAcquisition<ProtocolState> acquisition = this.connection.protocolState()) {
            if (!ServerPacketRegistry.isSupported(acquisition.get(), ServerPluginMessagePacket.class))
                throw new IllegalStateException("The operation is not supported at current protocol state");
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
    public void setSettings(@NonNull Settings settings) {
        Objects.requireNonNull(settings, "The settings must not be null");
        try (WriteNullableObjectAcquisition<Settings> acquisition = this.settings.acquireWrite()) {
            PlayerChangeSettingsEvent event = new PlayerChangeSettingsEvent(this, settings);
            this.server().eventNode().call(event);
            if (event.isCancelled()) return;
            acquisition.set(settings);
        }
    }

    /**
     * Updates a brand name of the client.
     *
     * @param clientBrand the brand name
     * @since 1.0
     */
    public void setClientBrand(@NonNull String clientBrand) {
        NullabilityUtil.requireNonNull(clientBrand, "client brand");
        try (WriteNullableObjectAcquisition<String> acquisition = this.clientBrand.acquireWrite()) {
            acquisition.set(clientBrand);
        }
    }

    /**
     * Sends a plugin message containing brand name of the server to a client of this player.
     *
     * @param brand the server brand name
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
     * Sends a packet to a client backed by {@linkplain SocketPlayerConnection a socket player connection} attached
     * to this player.
     *
     * @param packet the server packet
     * @since 1.0
     * @see SocketPlayerConnection#sendPacket(ServerPacket)
     */
    public void sendPacket(@NonNull ServerPacket packet) {
        this.connection.sendPacket(packet);
    }
}