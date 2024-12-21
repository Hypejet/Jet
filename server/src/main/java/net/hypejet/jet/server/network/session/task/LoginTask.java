package net.hypejet.jet.server.network.session.task;

import net.hypejet.concurrency.object.WriteObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.events.login.LoginStartEvent;
import net.hypejet.jet.login.profile.GameProfileProperty;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.util.unit.Unit;
import net.hypejet.jet.login.LoginManager;
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
 * Represents {@linkplain SessionTask a session task}, which handles {@linkplain ProtocolState#LOGIN a login protocol
 * state}.
 *
 * @since 1.0
 * @author Codestech
 * @see LoginManager
 * @see SessionTask
 */
public final class LoginTask implements SessionTask, LoginManager {

    private static final int TIME_OUT_TIME = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;
    private static final String VIRTUAL_THREAD_NAME = "Login session task thread";

    private final SocketPlayerConnection connection;

    private final CompletableFuture<Unit> loginRequestFuture = new CompletableFuture<>();
    private final CompletableFuture<Unit> pluginFuture = new CompletableFuture<>();
    private final CompletableFuture<Unit> acknowledgeFuture = new CompletableFuture<>();

    private final BooleanAcquirable finished = new BooleanAcquirable();

    /**
     * Constructs the {@linkplain LoginTask login task}.
     *
     * @param connection a connection that the session task should be handled for
     * @param clientProtocolVersion a protocol version of the client trying to connect
     * @param transferring whether the client is joining due to transferring from another server
     * @throws IllegalStateException if the caller thread is not an event loop thread
     */
    public LoginTask(@NonNull SocketPlayerConnection connection, int clientProtocolVersion, boolean transferring) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");

        if (transferring) {
            JetServerConfiguration configuration = this.connection.server().configuration();
            if (!configuration.areTransfersAllowed()) {
                this.connection.disconnect(configuration.transfersNotAllowedMessage());
                return;
            }
        }

        JetMinecraftServer server = this.connection.server();
        if (clientProtocolVersion != server.protocolVersion()) {
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
        this.loginRequestFuture.cancel(false);
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
            // TODO: Handle properties
            this.connection.initializePlayer(new JetPlayer(uniqueId, username, this.connection));
            this.pluginFuture.complete(Unit.INSTANCE);
        }
    }

    /**
     * Handles a login request from a client.
     *
     * @param packet a packet of the login request
     * @since 1.0
     * @throws IllegalArgumentException if the client has already sent a login request
     */
    public void handleLoginRequest(@NonNull ClientLoginRequestLoginPacket packet) {
        if (this.loginRequestFuture.isDone())
            throw new IllegalArgumentException("The login request has been already handled");
        this.loginRequestFuture.complete(Unit.INSTANCE);

        LoginStartEvent loginStartEvent = new LoginStartEvent(packet.username(), packet.uniqueId(), this);
        this.connection.server().eventNode().call(loginStartEvent);
    }

    /**
     * Handles an acknowledgement to the login finish from a client.
     *
     * @since 1.0
     * @throws IllegalArgumentException if the client has already sent an acknowledgement
     */
    public void acknowledgeFinishLogin() {
        if (this.acknowledgeFuture.isDone())
            throw new IllegalArgumentException("The login finish has been already acknowledged");
        this.acknowledgeFuture.complete(Unit.INSTANCE);

        // Ensure that no packet from the further session is handled
        this.connection.clientPacketReader().pausePacketReading();
    }

    /**
     * Awaits for when the client sends a login request.
     *
     * @param timeout a maximum time to wait
     * @param timeUnit a unit of the maximum time to wait
     * @since 1.0
     */
    public void awaitForLoginRequest(long timeout, @NonNull TimeUnit timeUnit) {
        try {
            this.loginRequestFuture.get(timeout, timeUnit);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } catch (TimeoutException exception) {
            throw new RuntimeException("The client has not sent the login request on time", exception);
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during awaiting for the login request", exception);
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }

    private void runVirtualThreadTask() {
        try {
            try {
                this.pluginFuture.get(TIME_OUT_TIME, TIME_OUT_UNIT);
            } catch (TimeoutException exception) {
                throw new RuntimeException("The login session has been not finished on time", exception);
            }

            try (WriteObjectAcquisition<Session> sessionAcquisition = this.connection.acquireSessionWrite()) {
                JetPlayer player = this.connection.playerOrThrow();
                // TODO: Handle properties
                player.sendPacket(new ServerLoginSuccessLoginPacket(player.uniqueId(), player.username(), Set.of()));

                this.acknowledgeFuture.get(TIME_OUT_TIME, TIME_OUT_UNIT);

                Session configurationSession = new Session(ProtocolState.CONFIGURATION, this.connection);
                sessionAcquisition.set(configurationSession);
                configurationSession.startSession(new ConfigurationTask(player));

                this.connection.clientPacketReader().resumePacketReading();
            } catch (TimeoutException exception) {
                throw new RuntimeException("The login session finish has been not acknowledged on time", exception);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new RuntimeException("The login task has been interrupted", exception);
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during a login task", exception);
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }
}