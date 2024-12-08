package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.events.serverlist.ServerListPingEvent;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientPingRequestPacket;
import net.hypejet.jet.server.network.packet.packets.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPingResponsePacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.packets.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.server.util.unit.Unit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents {@linkplain SessionTask a session task}, which handles {@linkplain ProtocolState#STATUS a status
 * protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ProtocolState#STATUS
 * @see SessionTask
 */
public final class StatusSessionTask implements SessionTask {

    private static final int TIME_OUT_DURATION = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private static final String VIRTUAL_THREAD_NAME = "Status session task thread";
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusSessionTask.class);

    private final SocketPlayerConnection connection;

    private final CompletableFuture<Unit> serverListRequestFuture = new CompletableFuture<>();
    private final CompletableFuture<Unit> pingRequestFuture = new CompletableFuture<>();

    /**
     * Constructs the {@linkplain StatusSessionTask status session task}.
     *
     * @param connection a connection that the task should be handled for
     * @since 1.0
     */
    public StatusSessionTask(@NonNull SocketPlayerConnection connection) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        Thread.ofVirtual()
                .name(VIRTUAL_THREAD_NAME)
                .uncaughtExceptionHandler(connection)
                .start(this::runVirtualThreadTask);
    }

    @Override
    public void handleDisconnection() {
        this.serverListRequestFuture.cancel(false);
        this.pingRequestFuture.cancel(false);
    }

    /**
     * Handles {@linkplain ClientServerListRequestStatusPacket a client server list request status packet}.
     *
     * @since 1.0
     */
    public void handleServerListRequest() {
        this.connection.ensureInEventLoop();

        if (this.serverListRequestFuture.isDone())
            throw new IllegalArgumentException("The server list request packet has been already received");
        this.serverListRequestFuture.complete(Unit.INSTANCE);

        JetMinecraftServer server = this.connection.server();
        ServerListPing ping = createDefaultServerListPing(server);

        try {
            ServerListPingEvent pingEvent = new ServerListPingEvent(this.connection, ping);
            server.eventNode().call(pingEvent);
            ping = pingEvent.getPing();
        } catch (Throwable throwable) {
            // A plugin might have thrown an exception, we just ignore it and use the default server list ping value
            LOGGER.error("An error occurred during handling of the server list ping event", throwable);
        }

        this.connection.sendPacket(new ServerListResponseStatusPacket(ping));
    }

    /**
     * Handles {@linkplain ClientPingRequestPacket a client ping request status packet}.
     *
     * @param packet the packet
     * @since 1.0
     */
    public void handlePingRequest(@NonNull ClientPingRequestPacket packet) {
        this.connection.ensureInEventLoop();

        if (!this.serverListRequestFuture.isDone())
            // The client can skip the server list request, it is a natural vanilla behaviour
            this.serverListRequestFuture.complete(Unit.INSTANCE);

        if (this.pingRequestFuture.isDone())
            throw new IllegalArgumentException("The ping request packet has been already received");
        this.pingRequestFuture.complete(Unit.INSTANCE);

        this.connection.sendPacket(new ServerPingResponsePacket(packet.timestamp()));
        this.connection.close(); // The status session has finished
    }

    private void runVirtualThreadTask() {
        try {
            this.serverListRequestFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);
            this.pingRequestFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during a status session task", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new RuntimeException("The status session task has been interrupted", exception);
        } catch (TimeoutException exception) {
            throw new RuntimeException("The status packets have not been sent on time", exception);
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }

    private static @NotNull ServerListPing createDefaultServerListPing(@NonNull JetMinecraftServer server) {
        JetServerConfiguration configuration = NullabilityUtil.requireNonNull(server, "server").configuration();
        return new ServerListPing(new ServerListPing.Version(server.minecraftVersion(), server.protocolVersion()),
                // TODO: An actual list of players online
                new ServerListPing.Players(configuration.maxPlayers(), 0, List.of()),
                configuration.serverListDescription(), server.serverIcon(), false, /* TODO: An actual property*/ false,
                /* TODO: An actual property*/ null);
    }
}