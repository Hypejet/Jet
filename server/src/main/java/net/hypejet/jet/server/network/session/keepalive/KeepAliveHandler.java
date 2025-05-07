package net.hypejet.jet.server.network.session.keepalive;

import io.netty.util.collection.LongObjectMap;
import io.netty.util.collection.LongObjectMap.PrimitiveEntry;
import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerKeepAlivePacket;
import net.hypejet.jet.server.util.acquirable.map.longs.LongObjectHashMapAcquirable;
import net.hypejet.jet.server.util.thread.JetThreadFactory;
import net.hypejet.jet.server.util.unit.Unit;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents something that handles Minecraft "keep alive" processes.
 *
 * @since 1.0
 */
public final class KeepAliveHandler implements NetworkDisconnectionHandler {

    private static final long INTERVAL = 20;
    private static final TimeUnit INTERVAL_UNIT = TimeUnit.SECONDS;

    private static final Component TIMED_OUT_DISCONNECT_MESSAGE = Component.text("Timed out");

    private final SocketPlayerConnection connection;
    private final LongObjectHashMapAcquirable<CompletableFuture<Unit>> futures = new LongObjectHashMapAcquirable<>();

    private final ScheduledExecutorService executorService;
    private final BooleanAcquirable scheduled = new BooleanAcquirable();

    /**
     * Constructs the {@linkplain KeepAliveHandler keep alive handler}.
     *
     * @param connection a player connection that the keep alive processes should be handled for
     * @param username a username of a player that the keep alive processes should be handled for
     * @since 1.0
     */
    public KeepAliveHandler(@NonNull SocketPlayerConnection connection, @NonNull String username) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        this.executorService = Executors.newSingleThreadScheduledExecutor(JetThreadFactory.builder()
                .name("Keep alive thread #%s - " + username)
                .threadType(JetThreadFactory.ThreadType.VIRTUAL)
                .exceptionHandler(connection)
                .build());
    }

    @Override
    public void handleDisconnection() {
        try (MapAcquisition<?, ?, LongObjectMap<CompletableFuture<Unit>>> acquisition = this.futures.acquireWrite()) {
            this.executorService.shutdown();

            Iterator<PrimitiveEntry<CompletableFuture<Unit>>> iterator = acquisition.map().entries().iterator();
            while (iterator.hasNext()) {
                PrimitiveEntry<CompletableFuture<Unit>> entry = iterator.next();
                entry.value().cancel(false);
                iterator.remove();
            }
        }
    }

    /**
     * Handles a client response for a keep alive.
     *
     * @param keepAliveIdentifier an identifier of the keep alive that the client responds to
     * @since 1.0
     */
    public void handleKeepAliveResponse(long keepAliveIdentifier) {
        try (MapAcquisition<?, ?, LongObjectMap<CompletableFuture<Unit>>> acquisition = this.futures.acquireWrite()) {
            CompletableFuture<Unit> future = acquisition.map().remove(keepAliveIdentifier);
            if (future == null) {
                throw new IllegalArgumentException("Could not find a keep" +
                        " alive future with the identifier specified.");
            }
            future.complete(Unit.INSTANCE);
        }
    }

    /**
     * Schedules a task requesting the keep alive packets.
     *
     * @throws IllegalStateException if the task has been already scheduled
     * @since 1.0
     */
    public void schedule() {
        try (WriteBooleanAcquisition acquisition = this.scheduled.acquireWrite()) {
            if (acquisition.get())
                throw new IllegalStateException("The keep alive task has been already scheduled");
            acquisition.set(true);
            this.executorService.scheduleAtFixedRate(this::requestKeepAlive, 0, INTERVAL, INTERVAL_UNIT);
        }
    }

    /**
     * Stops the keep alive handler and waits until all keep alive tasks finish.
     *
     * @param duration a duration, for which the await block should last
     * @param timeUnit a time unit of the duration
     * @return {@code true} if the executor terminated before the duration specified, {@code false} otherwise
     * @throws InterruptedException when the thread is interrupted during awaiting
     * @since 1.0
     */
    public boolean stopAndAwaitTermination(long duration, @NonNull TimeUnit timeUnit) throws InterruptedException {
        this.executorService.shutdown();
        return this.executorService.awaitTermination(duration, timeUnit);
    }

    private void requestKeepAlive() {
        CompletableFuture<Unit> keepAliveFuture = new CompletableFuture<>();
        try (MapAcquisition<?, ?, LongObjectMap<CompletableFuture<Unit>>> acquisition = this.futures.acquireWrite()) {
            LongObjectMap<CompletableFuture<Unit>> map = acquisition.map();

            long keepAliveIdentifier = System.currentTimeMillis();
            while (map.containsKey(keepAliveIdentifier))
                keepAliveIdentifier++;

            map.put(keepAliveIdentifier, keepAliveFuture);
            this.connection.sendPacket(new ServerKeepAlivePacket(keepAliveIdentifier));
        }

        try {
            keepAliveFuture.get(INTERVAL, INTERVAL_UNIT);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new RuntimeException("The keep alive thread has been interrupted", exception);
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during handling the keep alive process", exception);
        } catch (TimeoutException exception) {
            this.connection.disconnect(TIMED_OUT_DISCONNECT_MESSAGE);
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }
}