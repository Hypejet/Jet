package net.hypejet.jet.server.entity.player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.object.nullable.WriteNullableObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.settings.ChangeSettingsEvent;
import net.hypejet.jet.event.events.world.InitialSpawnEvent;
import net.hypejet.jet.event.events.world.PreWorldSwitchEvent;
import net.hypejet.jet.event.events.world.WorldSwitchEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPluginMessagePacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.JetWorldManager;
import net.hypejet.jet.server.world.handler.ChunkBatchHandler;
import net.hypejet.jet.world.World;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an implementation of {@linkplain Player a player}.
 *
 * @since 1.0
 * @see Player
 * @see JetEntity
 */
public final class JetPlayer extends JetEntity implements Player, NetworkDisconnectionHandler {

    private static final Key ENTITY_TYPE = Key.key("player");
    private static final Key BRAND_PLUGIN_MESSAGE_IDENTIFIER = Key.key("brand");

    private static final Logger LOGGER = LoggerFactory.getLogger(JetPlayer.class);

    private final SocketPlayerConnection connection;

    private final NullableObjectAcquirable<Settings> settings = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<String> clientBrand = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<JetWorld> world = new NullableObjectAcquirable<>();

    private final ChunkBatchHandler chunkBatchHandler = new ChunkBatchHandler(this);

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
    public @NonNull NullableObjectAcquisition<? extends World> getWorld() {
        return this.world.acquireRead();
    }

    @Override
    public void setWorld(@NonNull World world, @NonNull Position position) {
        if (!(world instanceof JetWorld validatedWorld))
            throw new IllegalArgumentException("The world specified is not a valid world");
        this.setWorld(validatedWorld, position);
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

    @Override
    public void handleDisconnection() {
        this.chunkBatchHandler.handleDisconnection();
    }

    /**
     * Gets {@linkplain ChunkBatchHandler a chunk batch handler}, which sends
     * {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunks} to this {@linkplain JetPlayer player}.
     *
     * @return the chunk batch handler
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull ChunkBatchHandler chunkBatchHandler() {
        return this.chunkBatchHandler;
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
            Settings previousSettings = acquisition.get();
            acquisition.set(settings);

            ChangeSettingsEvent event = new ChangeSettingsEvent(this, settings);
            this.server().eventNode().call(event);

            byte viewDistance = settings.viewDistance();
            if (previousSettings == null || previousSettings.viewDistance() != viewDistance)
                this.chunkBatchHandler.handleViewDistanceUpdate(viewDistance);
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

    private void setWorld(@NonNull JetWorld world, @NonNull Position position) {
        JetWorldManager worldManager = this.server().worldManager();
        BooleanAcquisition newWorldRegisteredAcquisition = null;

        try (
                BooleanAcquisition worldRegisteredAcquisition = worldManager.isRegistered(world);
                BooleanAcquisition chunkBatchTaskRunningAcquisition = this.chunkBatchHandler.isTaskRunning();
                WriteNullableObjectAcquisition<JetWorld> worldAcquisition = this.world.acquireWrite()
        ) {
            if (!worldRegisteredAcquisition.get())
                throw new IllegalArgumentException("The world specified has not been registered in the world manager");

            if (chunkBatchTaskRunningAcquisition.get())
                this.chunkBatchHandler.cancelTask();

            JetWorld previousWorld = worldAcquisition.get();
            EventNode<Object> eventNode = this.server().eventNode();

            if (previousWorld != null) {
                previousWorld.removePlayer(this);

                PreWorldSwitchEvent event = new PreWorldSwitchEvent(this, previousWorld, world, position);
                eventNode.call(event);

                if (event.isCancelled())
                    return;

                World newWorld = event.getNewWorld();
                if (newWorld != world) {
                    if (newWorld instanceof JetWorld validatedNewWorld) {
                        newWorldRegisteredAcquisition = worldManager.isRegistered(newWorld);

                        if (newWorldRegisteredAcquisition.get()) {
                            world = validatedNewWorld;
                        } else {
                            LOGGER.warn("The new world specified in the pre-world-switch event has not been" +
                                    " registered in the world manager, switching back to the world specified" +
                                    " as an argument in the method");
                        }
                    } else {
                        LOGGER.warn("The new world specified in the pre-world-switch event is not a valid world," +
                                " switching back to the world specified as an argument in the method");
                    }
                }

                position = event.getStartingPosition();
            }

            worldAcquisition.set(world);
            world.addPlayer(this);

            // TODO: Set position

            this.chunkBatchHandler.scheduleTask(world, position);

            Object postEvent = previousWorld == null
                    ? new InitialSpawnEvent(this, world, position)
                    : new WorldSwitchEvent(this, previousWorld, world, position);
            eventNode.call(postEvent);
        } finally {
            if (newWorldRegisteredAcquisition != null)
                newWorldRegisteredAcquisition.close();
        }
    }
}