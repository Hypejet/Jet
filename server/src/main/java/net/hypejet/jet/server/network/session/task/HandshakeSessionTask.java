package net.hypejet.jet.server.network.session.task;

import net.hypejet.concurrency.object.WriteObjectAcquisition;
import java.util.Objects;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.packets.client.handshake.ClientHandshakePacket;
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
 * @see Session
 * @see ClientHandshakePacket
 * @see SessionTask
 */
public final class HandshakeSessionTask implements SessionTask {

    private static final int TIME_OUT_DURATION = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private final SocketPlayerConnection connection;
    private final CompletableFuture<ClientHandshakePacket> handshakeFuture = new CompletableFuture<>();

    /**
     * Constructs the {@linkplain HandshakeSessionTask handshake session task}.
     *
     * @param connection a socket player connection that the session task should be handled for
     * @since 1.0
     */
    public HandshakeSessionTask(@NonNull SocketPlayerConnection connection) {
        // We do not care about the acquisition if it is not null, it is up to user not to provide null values
        this.connection = Objects.requireNonNull(connection, "connection");
    }

    @Override
    public void handleDisconnection() {
        this.handshakeFuture.cancel(false);
    }

    @Override
    public void start() {
        Thread.ofVirtual()
                .name("Handshake session task thread")
                .uncaughtExceptionHandler(this.connection)
                .start(this::runVirtualThreadTask);
    }

    /**
     * Handles {@linkplain ClientHandshakePacket a client handshake packet}.
     *
     * @param packet the handshake packet
     * @since 1.0
     */
    public void handleHandshakePacket(@NonNull ClientHandshakePacket packet) {
        this.handshakeFuture.complete(packet);
        // Ensure that no packet from the further session is handled
        this.connection.clientPacketReader().pausePacketReading();
    }

    private void runVirtualThreadTask() {
        try (WriteObjectAcquisition<Session> sessionAcquisition = this.connection.acquireSessionWrite()) {
            ClientHandshakePacket packet = this.handshakeFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);

            ProtocolState nextProtocolState = switch (packet.intent()) {
                case STATUS -> ProtocolState.STATUS;
                case LOGIN, TRANSFER -> ProtocolState.LOGIN;
            };

            SessionTask nextSessionTask = switch (packet.intent()) {
                case STATUS -> new StatusSessionTask(this.connection);
                case LOGIN -> new LoginSessionTask(this.connection, packet.protocolVersion(), false);
                case TRANSFER -> new LoginSessionTask(this.connection, packet.protocolVersion(), true);
            };

            sessionAcquisition.set(new Session(nextProtocolState, this.connection, nextSessionTask));
            this.connection.clientPacketReader().resumePacketReading();

            /* Set the compression threshold here to ensure that there will be no race conditions. Technically,
               it is possible anyway, but login protocol state by design is a state where no packet that was
               not requested by a server should come, except of the "login request". Modded clients are obliged
               to keep this approach. Since plugins are allowed to send any request packet during login without
               waiting for a response, the most safe place to enable the compression is here, because the session
               acquisition is still locked since handshake, no login packet was sent by any plugin, and we are going
               to await for the login request as well as for when handlers are updated with the new compression
               threshold. */
            if (nextSessionTask instanceof LoginSessionTask loginTask)
                loginTask.setupCompression();
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during a handshaking task", exception);
        } catch (TimeoutException exception) {
            throw new RuntimeException("The handshake packet has not been sent on time", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }
}