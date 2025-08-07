package net.hypejet.jet.server.network.session.task;

import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import java.util.Objects;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.network.session.common.CommonSessionPacketHandler;
import net.hypejet.jet.server.network.session.data.ConfigurationData;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveHandler;
import net.hypejet.jet.server.registry.function.RegistryTagUpdateFunction;
import net.kyori.adventure.resource.ResourcePackStatus;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents {@linkplain SessionTask a session task}, which handles
 * {@linkplain net.hypejet.jet.server.network.ProtocolState#PLAY a play protocol state}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#PLAY
 * @see SessionTask
 */
public final class PlaySessionTask implements SessionTask, RegistryTagUpdateFunction, CommonSessionPacketHandler {

    private final SocketPlayerConnection connection;
    private final ConfigurationData configurationData;

    private final KeepAliveHandler keepAliveHandler;
    private final BooleanAcquirable commandsSent = new BooleanAcquirable();

    private final CompletableFuture<JetPlayer> playerFuture = new CompletableFuture<>();

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

        try (WriteBooleanAcquisition commandsSentAcquisition = this.commandsSent.acquireWrite()) {
            this.connection.server().commandManager().sendDeclarationPacket(this.connection);
            commandsSentAcquisition.set(true);
        }

        this.playerFuture.complete(JetPlayer.create(this.connection, this.configurationData));
    }

    @Override
    public void handleDisconnection() {
        this.keepAliveHandler.handleDisconnection();
    }

    @Override
    public void updateTags(@NonNull ServerUpdateTagsPacket packet) {
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
     * Updates commands for {@linkplain JetPlayer a player} that the task is handled for.
     *
     * @param packet a packet representing the command update
     * @since 1.0
     */
    public void updateCommands(@NonNull ServerDeclareCommandsPlayPacket packet) {
        try (BooleanAcquisition commandsSentAcquisition = this.commandsSent.acquireRead()) {
            // Commands have not been sent yet, so the update will be taken into account when commands are sent
            if (!commandsSentAcquisition.get()) return;
            this.connection.sendPacket(packet);
        }
    }
}