package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents {@linkplain SessionTask a session task}, which changes {@linkplain Session a session} based
 * on {@linkplain ClientHandshakePacket.HandshakeIntent a handshake intention} specified
 * in {@linkplain ClientHandshakePacket a client handshake packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see Session
 * @see ClientHandshakePacket
 * @see SessionTask
 */
public final class HandshakeTask implements SessionTask {

    private static final int TIME_OUT_DURATION = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private static final String VIRTUAL_THREAD_NAME = "Handshake session task thread";

    private final SocketPlayerConnection connection;

    private final MutableAcquisition<Session> sessionAcquisition;
    private final CompletableFuture<ClientHandshakePacket> handshakeFuture = new CompletableFuture<>();

    /**
     * Constructs the {@linkplain HandshakeTask handshake task}.
     *
     * @param connection a socket player connection that the task is done for
     * @since 1.0
     */
    public HandshakeTask(@NonNull SocketPlayerConnection connection) {
        // We do not care about the acquisition if it is not null, it is up to user not to provide null values
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");

        connection.ensureInEventLoop(); // The session acquisition should be created in an event loop
        this.sessionAcquisition = connection.session().acquireMutable();

        Thread.ofVirtual()
                .name(VIRTUAL_THREAD_NAME)
                .uncaughtExceptionHandler(connection)
                .start(this::runVirtualThreadTask);
    }

    @Override
    public void handleDisconnection() {
        this.handshakeFuture.cancel(false);
        if (this.sessionAcquisition != null)
            this.sessionAcquisition.close();
    }

    /**
     * Handles {@linkplain ClientHandshakePacket a client handshake packet}.
     *
     * @param packet the handshake packet
     * @since 1.0
     */
    public void handleHandshakePacket(@NonNull ClientHandshakePacket packet) {
        try {
            this.connection.ensureInEventLoop();
            this.handshakeFuture.complete(packet);

            ProtocolState nextProtocolState = switch (packet.intent()) {
                case STATUS -> ProtocolState.STATUS;
                case LOGIN, TRANSFER -> ProtocolState.LOGIN;
            };

            Session session = new Session(nextProtocolState, this.connection);
            this.sessionAcquisition.set(session);

            SessionTask nextSessionTask = switch (packet.intent()) {
                case STATUS -> new StatusSessionTask(this.connection);
                case LOGIN -> new LoginTask(this.connection, packet.protocolVersion(), false);
                case TRANSFER -> new LoginTask(this.connection, packet.protocolVersion(), true);
            };

            session.startSession(nextSessionTask);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            this.sessionAcquisition.close();
        }
    }

    private void runVirtualThreadTask() {
        try {
            this.handshakeFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during a handshaking task", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new RuntimeException("The handshaking task has been interrupted", exception);
        } catch (TimeoutException exception) {
            throw new RuntimeException("The handshake packet has not been sent on time", exception);
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }
}