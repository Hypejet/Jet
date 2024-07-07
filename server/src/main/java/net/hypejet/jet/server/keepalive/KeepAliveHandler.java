package net.hypejet.jet.server.keepalive;

import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.session.Session;
import net.hypejet.jet.server.util.thread.JetThreadFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents something that handles keep alive packets and timing out for a {@linkplain Session session}.
 *
 * @since 1.0
 * @author Codestech
 * @see Session
 */
public final class KeepAliveHandler {

    private static final int KEEP_ALIVE_INTERVAL = 10;
    private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;

    private final ScheduledExecutorService keepAliveExecutor;

    private final ReentrantLock keepAliveLock = new ReentrantLock();
    private final LongObjectMap<CountDownLatch> keepAliveLatches = new LongObjectHashMap<>();

    private final JetPlayer player;

    private final KeepAlivePacketSupplier keepAlivePacketSupplier;

    /**
     * Constructs the {@linkplain KeepAliveHandler keep alive handler}.
     *
     * @param player a player that keep alive packets are handler for
     * @param exceptionHandler an exception handler that handles errors occurring during keep alive handling
     * @param keepAlivePacketSupplier a supplier creating keep alive packets
     * @since 1.0
     */
    public KeepAliveHandler(@NonNull JetPlayer player, Thread.@NonNull UncaughtExceptionHandler exceptionHandler,
                            @NonNull KeepAlivePacketSupplier keepAlivePacketSupplier) {
        this.keepAliveExecutor = Executors.newSingleThreadScheduledExecutor(JetThreadFactory.builder()
                .name("Keep alive thread - " + player.username())
                .threadType(JetThreadFactory.ThreadType.VIRTUAL)
                .exceptionHandler(exceptionHandler)
                .build());

        this.player = player;
        this.keepAlivePacketSupplier = keepAlivePacketSupplier;

        this.keepAliveExecutor.scheduleAtFixedRate(this::requestKeepAlive, 0, KEEP_ALIVE_INTERVAL, KEEP_ALIVE_UNIT);
    }

    /**
     * Handles a client response for a keep alive packet.
     *
     * @param keepAliveId an identifier of the keep alive
     * @since 1.0
     */
    public void handleKeepAlive(long keepAliveId) {
        this.keepAliveLock.lock();
        try {
            CountDownLatch latch = this.keepAliveLatches.remove(keepAliveId);
            if (latch == null)
                throw new IllegalArgumentException("Unknown keep alive with identifier of: " + keepAliveId);
            latch.countDown();
        } finally {
            this.keepAliveLock.unlock();
        }
    }

    /**
     * Gets whether an executor service of this keep alive handler is not shut down.
     *
     * @return {@code true} if the executor service is not shut down, false otherwise
     * @since 1.0
     * @see ExecutorService#isShutdown()
     */
    public boolean isAlive() {
        return !this.keepAliveExecutor.isShutdown();
    }

    /**
     * Shuts down an executor service of this keep alive handler.
     *
     * @since 1.0
     * @see ExecutorService#shutdown()
     */
    public void shutdown() {
        this.keepAliveExecutor.shutdown();
    }

    /**
     * Shuts down an executor service of this keep alive handler and tries to stop all running threads scheduled
     * by it.
     *
     * @since 1.0
     * @see ExecutorService#shutdownNow()
     */
    public void shutdownNow() {
        this.keepAliveExecutor.shutdownNow();
    }

    /**
     * Waits to termination of an executor service of this keep alive handler with a timeout specified by
     * {@link Session#TIME_OUT_DURATION} and {@link Session#TIME_OUT_UNIT}.
     *
     * @throws InterruptedException when interrupted while waiting
     * @since 1.0
     */
    public void awaitTermination() throws InterruptedException {
        if (!this.keepAliveExecutor.awaitTermination(Session.TIME_OUT_DURATION, Session.TIME_OUT_UNIT)) {
            this.handleTimeOut();
        }
    }

    /**
     * Requests a keep-alive response from a client.
     *
     * @since 1.0
     */
    private void requestKeepAlive() {
        CountDownLatch latch = new CountDownLatch(1);
        long keepAliveId;

        this.keepAliveLock.lock();

        try {
            do keepAliveId = ThreadLocalRandom.current().nextLong();
            while (this.keepAliveLatches.containsKey(keepAliveId));
            this.keepAliveLatches.put(keepAliveId, latch);
        } finally {
            this.keepAliveLock.unlock();
        }

        this.player.sendPacket(this.keepAlivePacketSupplier.createKeepAlivePacket(keepAliveId));

        try {
            if (!latch.await(Session.TIME_OUT_DURATION, Session.TIME_OUT_UNIT)) {
                this.keepAliveExecutor.shutdownNow();
                this.handleTimeOut();
            }
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Handles a timeout of an action in the session.
     *
     * @since 1.0
     */
    private void handleTimeOut() {
        this.player.disconnect(Session.TIME_OUT_DISCONNECTION_MESSAGE);
    }
}