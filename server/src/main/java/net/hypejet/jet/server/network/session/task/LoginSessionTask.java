package net.hypejet.jet.server.network.session.task;

import net.hypejet.concurrency.object.WriteObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import java.util.Objects;
import net.hypejet.jet.event.events.login.LoginFinishedEvent;
import net.hypejet.jet.event.events.login.LoginStartEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.packets.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCookieRequestPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.data.LoginData;
import net.hypejet.jet.server.util.unit.Unit;
import net.hypejet.jet.session.login.LoginManager;
import net.hypejet.jet.session.login.profile.GameProfileProperty;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents {@linkplain SessionTask a session task}, which handles
 * {@linkplain ProtocolState#LOGIN a login protocol state}.
 *
 * @since 1.0
 * @see ProtocolState#LOGIN
 * @see LoginManager
 * @see SessionTask
 */
public final class LoginSessionTask implements SessionTask, LoginManager {

    private static final int TIME_OUT_TIME = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;
    private static final String VIRTUAL_THREAD_NAME = "Login session task thread";

    private final SocketPlayerConnection connection;

    private final CompletableFuture<ClientLoginRequestLoginPacket> requestFuture = new CompletableFuture<>();

    private final CompletableFuture<LoginData> pluginFuture = new CompletableFuture<>();
    private final CompletableFuture<Unit> acknowledgeFuture = new CompletableFuture<>();

    private final BooleanAcquirable finished = new BooleanAcquirable();

    private final int clientProtocolVersion;
    private final boolean transferring;

    /**
     * Constructs the {@linkplain LoginSessionTask login session task}.
     *
     * @param connection a connection that the session task should be handled for
     * @param clientProtocolVersion a protocol version of the client trying to connect
     * @param transferring whether the client is joining due to transferring from another server
     * @throws IllegalStateException if the caller thread is not an event loop thread
     * @since 1.0
     */
    public LoginSessionTask(@NonNull SocketPlayerConnection connection,
                            int clientProtocolVersion, boolean transferring) {
        this.connection = Objects.requireNonNull(connection, "connection");
        this.clientProtocolVersion = clientProtocolVersion;
        this.transferring = transferring;
    }

    @Override
    public void start() {
        if (this.transferring) {
            JetServerConfiguration configuration = this.connection.server().configuration();
            if (!configuration.transfersAllowed()) {
                this.connection.disconnect(configuration.transfersNotAllowedMessage());
                return;
            }
        }

        JetMinecraftServer server = this.connection.server();
        if (this.clientProtocolVersion != server.protocolVersion()) {
            // TODO: Placeholders?
            this.connection.disconnect(server.configuration().unsupportedVersionMessage());
            return;
        }

        Thread.ofVirtual()
                .name(VIRTUAL_THREAD_NAME)
                .uncaughtExceptionHandler(connection)
                .start(this::runVirtualThreadTask);
    }

    @Override
    public void handleDisconnection() {
        this.pluginFuture.cancel(false);
        this.acknowledgeFuture.cancel(false);
        this.requestFuture.cancel(false);
    }

    @Override
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    @Override
    public void finish(@NonNull String username, @NonNull UUID uniqueId) {
        this.finish(username, uniqueId, Set.of());
    }

    @Override
    public void finish(@NonNull String username, @NonNull UUID uniqueId,
                       @NonNull Collection<GameProfileProperty> properties) {
        try (WriteBooleanAcquisition finishedAcquisition = this.finished.acquireWrite()) {
            if (finishedAcquisition.get())
                throw new IllegalArgumentException("The session has been already finished");
            finishedAcquisition.set(true);
            this.pluginFuture.complete(new LoginData(username, uniqueId, properties));
        }
    }

    @Override
    public void requestCookie(@NonNull Key key) {
        Objects.requireNonNull(key, "key");
        this.connection.sendPacket(new ServerCookieRequestPacket(key));
    }

    /**
     * Handles a login request from a client.
     *
     * @param packet a packet of the login request
     * @throws IllegalArgumentException if the client has already sent a login request
     * @since 1.0
     */
    public void handleLoginRequest(@NonNull ClientLoginRequestLoginPacket packet) {
        if (this.requestFuture.isDone())
            throw new IllegalArgumentException("The login request has been already handled");
        this.requestFuture.complete(packet);
    }

    /**
     * Handles an acknowledgement to the login finish from a client.
     *
     * @throws IllegalArgumentException if the client has already sent an acknowledgement
     * @since 1.0
     */
    public void acknowledgeFinishLogin() {
        if (this.acknowledgeFuture.isDone())
            throw new IllegalArgumentException("The login finish has been already acknowledged");
        this.acknowledgeFuture.complete(Unit.INSTANCE);

        // Ensure that no packet from the further session is handled
        this.connection.clientPacketReader().pausePacketReading();
    }

    /**
     * Awaits for a client login request and setups a compression for {@linkplain SocketPlayerConnection a socket
     * player connection} of this {@linkplain LoginSessionTask login session task}, then awaits for when handlers are
     * updated with the new compression threshold.
     *
     * @throws InterruptedException if the current thread has been interrupted during waiting for the login request
     * @since 1.0
     */
    public void setupCompression() throws InterruptedException {
        try {
            this.requestFuture.get(TIME_OUT_TIME, TIME_OUT_UNIT);
        } catch (ExecutionException | TimeoutException | CancellationException exception) {
            // The exception has been already handled by the login session task thread
        }

        int compressionThreshold = this.connection.server().configuration().compressionThreshold();
        if (compressionThreshold < 0) return; // The compression is disabled

        try {
            CompletableFuture<SocketPlayerConnection.PacketSendResult> resultFuture = new CompletableFuture<>();
            CompletableFuture<Void> handlerUpdateFuture = resultFuture.thenAccept(result -> {
                if (result == SocketPlayerConnection.PacketSendResult.SUCCESS)
                    this.connection.updateHandlers(compressionThreshold);
            });

            this.connection.sendPacket(new ServerEnableCompressionLoginPacket(compressionThreshold), resultFuture);
            handlerUpdateFuture.get();
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during setting the compression", exception);
        }
    }

    private void runVirtualThreadTask() {
        try {
            EventNode<Object> eventNode = this.connection.server().eventNode();

            try {
                ClientLoginRequestLoginPacket requestPacket = this.requestFuture.get(TIME_OUT_TIME, TIME_OUT_UNIT);
                eventNode.call(new LoginStartEvent(
                        requestPacket.username(), requestPacket.uniqueId(), this
                ));
            } catch (TimeoutException exception) {
                throw new RuntimeException("The login request has not been sent by a client on time", exception);
            }

            LoginData loginData;

            try {
                loginData = this.pluginFuture.get(TIME_OUT_TIME, TIME_OUT_UNIT);
            } catch (TimeoutException exception) {
                throw new RuntimeException("The login session has not been finished on time", exception);
            }

            LoginFinishedEvent finishedEvent = new LoginFinishedEvent(this.connection);
            eventNode.call(finishedEvent);

            switch (finishedEvent.getResult()) {
                case LoginFinishedEvent.Result.Fail fail -> {
                    this.connection.disconnect(fail.disconnectReason());
                    return;
                }
                case LoginFinishedEvent.Result.Success ignored -> {}
            }

            try (WriteObjectAcquisition<Session> sessionAcquisition = this.connection.acquireSessionWrite()) {
                this.connection.sendPacket(new ServerLoginSuccessLoginPacket(
                        loginData.uniqueId(), loginData.username(), loginData.properties()
                ));

                this.acknowledgeFuture.get(TIME_OUT_TIME, TIME_OUT_UNIT);

                sessionAcquisition.set(new Session(
                        ProtocolState.CONFIGURATION, this.connection,
                        new ConfigurationSessionTask(this.connection, loginData)
                ));

                this.connection.clientPacketReader().resumePacketReading();
            } catch (TimeoutException exception) {
                throw new RuntimeException(
                        "The login session finish has not been acknowledged by client on time",
                        exception
                );
            }
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during a login task", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }
}