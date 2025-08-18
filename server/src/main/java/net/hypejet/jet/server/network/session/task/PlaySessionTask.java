package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.network.session.common.CommonSessionPacketHandler;
import net.hypejet.jet.server.network.session.data.ConfigurationData;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveHandler;
import net.hypejet.jet.server.registry.function.SynchronizeRegistryTagsFunction;
import net.kyori.adventure.resource.ResourcePackStatus;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents {@linkplain SessionTask a session task}, which handles
 * {@linkplain net.hypejet.jet.server.network.ProtocolState#PLAY a play protocol state}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#PLAY
 * @see SessionTask
 */
public final class PlaySessionTask implements SessionTask, SynchronizeRegistryTagsFunction, CommonSessionPacketHandler {

    private final SocketPlayerConnection connection;
    private final ConfigurationData configurationData;

    private final KeepAliveHandler keepAliveHandler;
    private final CompletableFuture<JetPlayer> playerFuture = new CompletableFuture<>();

    private final ReentrantReadWriteLock commandsInitializedLock = new ReentrantReadWriteLock();
    private boolean commandsInitialized;

    /**
     * Constructs the {@linkplain PlaySessionTask play session task}.
     *
     * @param connection a connection that the task should be handled for
     * @param configurationData a configuration data that should be used for player creation
     * @since 1.0
     */
    public PlaySessionTask(@NonNull SocketPlayerConnection connection, @NonNull ConfigurationData configurationData) {
        this.connection = Objects.requireNonNull(connection, "connection");
        this.configurationData = Objects.requireNonNull(configurationData, "configuration data");
        this.keepAliveHandler = new KeepAliveHandler(connection, configurationData.loginData().username());
    }

    @Override
    public void start() {
        this.keepAliveHandler.schedule();
        this.connection.server().commandManager().initializeCommands(this);
        JetPlayer.create(this.connection, this.configurationData, this.playerFuture);
    }

    @Override
    public void handleDisconnection() {
        this.keepAliveHandler.handleDisconnection();
    }

    @Override
    public void synchronizeTags(@NonNull ServerUpdateTagsPacket packet) {
        this.connection.sendPacket(packet);
    }

    @Override
    public void handleClientBrand(@NonNull String name) {
        this.playerFuture.thenAccept(player -> player.setClientBrand(name));
    }

    @Override
    public void handleClientInformation(Player.@NonNull Settings settings) {
        this.playerFuture.thenAccept(player -> player.setSettings(settings));
    }

    @Override
    public void handleKeepAliveResponse(long keepAliveIdentifier) {
        this.keepAliveHandler.handleKeepAliveResponse(keepAliveIdentifier);
    }

    @Override
    public void handleResourcePackStatus(@NonNull UUID uniqueId, @NonNull ResourcePackStatus status) {
        this.playerFuture.thenAccept(player -> player.resourcePackHandler().handleState(uniqueId, status, player));
    }

    /**
     * Sends the specified {@linkplain ServerDeclareCommandsPlayPacket server declare commands play packet}
     * to the {@linkplain SocketPlayerConnection player connection} associated
     * with this {@linkplain PlaySessionTask play session task}.
     *
     * @param packet the packet to send
     * @param initializing whether this is command initialization rather than an update
     * @since 1.0
     */
    public void sendCommands(@NonNull ServerDeclareCommandsPlayPacket packet, boolean initializing) {
        Lock lock = initializing ? this.commandsInitializedLock.writeLock() : this.commandsInitializedLock.readLock();
        try {
            lock.lock();
            if (initializing) {
                if (this.commandsInitialized)
                    throw new IllegalStateException("The commands have already been initialized");
                this.commandsInitialized = true;
            } else if (!this.commandsInitialized) {
                return;
            }
            this.connection.sendPacket(packet);
        } finally {
            lock.unlock();
        }
    }
}