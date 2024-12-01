package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.events.login.LoginSessionInitializeEvent;
import net.hypejet.jet.network.ProtocolState;
import net.hypejet.jet.network.packet.client.ClientPacket;
import net.hypejet.jet.network.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.network.packet.server.login.ServerLoginSuccessLoginPacket.Property;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.util.unit.Unit;
import net.hypejet.jet.session.LoginSession;
import net.hypejet.jet.session.handler.LoginSessionHandler;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
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
 * @see LoginSession
 * @see SessionTask
 */
public final class LoginTask implements SessionTask, LoginSession {

    private static final int TIME_OUT_TIME = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;
    private static final String VIRTUAL_THREAD_NAME = "Login session task thread";

    private final SocketPlayerConnection connection;

    private final CompletableFuture<Unit> handlerFuture = new CompletableFuture<>();
    private final CompletableFuture<Unit> acknowledgeFuture = new CompletableFuture<>();

    private @MonotonicNonNull MutableAcquisition<Session> sessionAcquisition;
    private @MonotonicNonNull LoginSessionHandler sessionHandler;

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

        LoginSessionInitializeEvent sessionInitializeEvent = new LoginSessionInitializeEvent(this.connection);
        server.eventNode().call(sessionInitializeEvent);

        LoginSessionHandler sessionHandler = sessionInitializeEvent.getSessionHandler();
        if (sessionHandler == null)
            throw new IllegalArgumentException("The login session handler has not been set");
        this.sessionHandler = sessionHandler;

        Thread.ofVirtual()
                .name(VIRTUAL_THREAD_NAME)
                .uncaughtExceptionHandler(connection)
                .start(this::runVirtualThreadTask);
    }

    @Override
    public void handleDisconnection() {
        this.handlerFuture.cancel(false);
        this.acknowledgeFuture.cancel(false);

        if (this.sessionAcquisition != null)
            this.sessionAcquisition.close();
    }

    @Override
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    @Override
    public void finish(@NonNull String username, @NonNull UUID uniqueId, @NonNull Collection<Property> properties) {
        if (this.handlerFuture.isDone())
            throw new IllegalArgumentException("The session has been already finished");
        // TODO: Handle properties
        this.connection.initializePlayer(new JetPlayer(uniqueId, username, this.connection));
        this.handlerFuture.complete(Unit.INSTANCE);
    }

    /**
     * Handles an acknowledgement to the login finish from the client.
     *
     * @since 1.0
     */
    public void acknowledgeFinishLogin() {
        this.connection.ensureInEventLoop();

        if (this.acknowledgeFuture.isDone())
            throw new IllegalArgumentException("The login finish has been already acknowledged");
        this.acknowledgeFuture.complete(Unit.INSTANCE);

        MutableAcquisition<Session> sessionAcquisition = this.sessionAcquisition;
        if (sessionAcquisition == null)
            throw new IllegalArgumentException("The session acquirable has been not acquired");

        try (sessionAcquisition) {
            sessionAcquisition.set(new Session(
                    ProtocolState.CONFIGURATION, this.connection,
                    () -> new ConfigurationTask(this.connection.playerOrThrow())
            ));
        }
    }

    /**
     * Handles {@linkplain ClientPacket a client packet} received during this session task.
     *
     * @param packet the packet
     * @since 1.0
     */
    public void handlePacket(@NonNull ClientPacket packet) {
        this.connection.ensureInEventLoop();
        if (this.sessionHandler == null)
            throw new IllegalArgumentException("The session handler has been not initialized");
        this.sessionHandler.handlePacket(packet, this);
    }

    private void runVirtualThreadTask() {
        try {
            try {
                this.handlerFuture.get(TIME_OUT_TIME, TIME_OUT_UNIT);
            } catch (TimeoutException exception) {
                this.sessionHandler.handleTimeOut(this);
                throw new RuntimeException("The login session handler has timed out", exception);
            }

            this.connection.submitToEventLoop(this::finishSession).get();

            try {
                this.acknowledgeFuture.get(TIME_OUT_TIME, TIME_OUT_UNIT);
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

    private void finishSession() {
        JetPlayer player = this.connection.playerOrThrow();
        MutableAcquisition<Session> sessionAcquisition = this.connection.createMutableSessionAcquisition();

        try {
            /* Set the compression threshold here to ensure that there will be no race conditions. Technically,
               it is possible anyway, but login protocol state by design is a state where no packet that were
               not requested by a server should come, except of "login request". Modded clients are obliged
               to keep this approach. */
            this.connection.setCompressionThreshold(this.connection.server()
                    .configuration()
                    .compressionThreshold());

            this.sessionAcquisition = sessionAcquisition;
            // TODO: Handle properties
            this.connection.sendPacket(new ServerLoginSuccessLoginPacket(
                    player.uniqueId(), player.username(), Set.of()
            ));
        } catch (Throwable throwable) {
            sessionAcquisition.close();
            throw throwable; // Re-throw the throwable, since it has been not completely handled
        }
    }
}