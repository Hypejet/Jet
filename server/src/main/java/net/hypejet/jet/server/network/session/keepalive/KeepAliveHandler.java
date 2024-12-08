package net.hypejet.jet.server.network.session.keepalive;

import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import io.netty.util.collection.LongObjectMap.PrimitiveEntry;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerKeepAlivePacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
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
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents something that handles Minecraft "keep alive" processes.
 *
 * @since 1.0
 * @author Codestech
 */
public final class KeepAliveHandler implements NetworkDisconnectionHandler, KeepAliveResponseHandler {

    private static final long INTERVAL = 20;
    private static final TimeUnit INTERVAL_UNIT = TimeUnit.SECONDS;

    private static final Component TIMED_OUT_DISCONNECT_MESSAGE = Component.text("Timed out");

    private final JetPlayer player;

    private final LongObjectMap<CompletableFuture<Unit>> keepAliveFutureMap = new LongObjectHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    private final ScheduledExecutorService executorService;
    private boolean scheduled;

    /**
     * Constructs the {@linkplain KeepAliveHandler keep alive handler}.
     *
     * @param player a player that the keep alive handler is constructed for
     * @since 1.0
     */
    public KeepAliveHandler(@NonNull JetPlayer player) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
        this.executorService = Executors.newSingleThreadScheduledExecutor(JetThreadFactory.builder()
                .name("Keep alive thread #%s - " + player.username())
                .threadType(JetThreadFactory.ThreadType.VIRTUAL)
                .exceptionHandler(player.connection())
                .build());
    }

    @Override
    public void handleDisconnection() {
        try {
            this.lock.lock();
            this.executorService.shutdown();

            Iterator<PrimitiveEntry<CompletableFuture<Unit>>> iterator = this.keepAliveFutureMap.entries().iterator();
            while (iterator.hasNext()) {
                PrimitiveEntry<CompletableFuture<Unit>> entry = iterator.next();
                entry.value().cancel(false);
                iterator.remove();
            }
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Handles a client response for a keep alive.
     *
     * @param keepAliveIdentifier an identifier of the keep alive
     * @since 1.0
     */
    @Override
    public void handleKeepAliveResponse(long keepAliveIdentifier) {
        try {
            this.lock.lock();
            CompletableFuture<Unit> future = this.keepAliveFutureMap.remove(keepAliveIdentifier);
            if (future == null) {
                throw new IllegalArgumentException("Could not find a keep" +
                        " alive future with the identifier specified.");
            }
            future.complete(Unit.INSTANCE);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Schedules a task requesting the keep alive packets.
     *
     * @since 1.0
     * @throws IllegalStateException if the task has been already scheduled
     */
    public void schedule() {
        try {
            this.lock.lock();
            if (this.scheduled)
                throw new IllegalStateException("The task has been already scheduled");
            this.scheduled = true;
            this.executorService.scheduleAtFixedRate(this::requestKeepAlive, 0, INTERVAL, INTERVAL_UNIT);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Stops the keep alive handler and waits until all keep alive tasks finish.
     *
     * @param duration a duration, for which the await block should last
     * @param timeUnit a time unit of the duration
     * @return {@code true} if the executor terminated before the duration specified, {@code false} otherwise
     * @throws InterruptedException when the thread is interrupted during awaiting
     */
    public boolean stopAndAwaitTermination(long duration, @NonNull TimeUnit timeUnit) throws InterruptedException {
        this.executorService.shutdown();
        return this.executorService.awaitTermination(duration, timeUnit);
    }

    private void requestKeepAlive() {
        CompletableFuture<Unit> keepAliveFuture = new CompletableFuture<>();

        try {
            this.lock.lock();
            long keepAliveIdentifier;

            do keepAliveIdentifier = System.currentTimeMillis();
            while (this.keepAliveFutureMap.containsKey(keepAliveIdentifier));

            this.keepAliveFutureMap.put(keepAliveIdentifier, keepAliveFuture);
            this.player.sendPacket(new ServerKeepAlivePacket(keepAliveIdentifier));
        } finally {
            this.lock.unlock();
        }

        try {
            keepAliveFuture.get(INTERVAL, INTERVAL_UNIT);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new RuntimeException("The keep alive thread has been interrupted", exception);
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during handling of keep alive", exception);
        } catch (TimeoutException exception) {
            this.player.disconnect(TIMED_OUT_DISCONNECT_MESSAGE);
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }
}