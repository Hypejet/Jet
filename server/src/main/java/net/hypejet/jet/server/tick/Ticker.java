package net.hypejet.jet.server.tick;

import net.hypejet.jet.server.JetMinecraftServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * A handler of a game logic loop of a {@linkplain JetMinecraftServer Minecraft server}.
 *
 * @since 1.0
 * @see JetMinecraftServer
 */
public final class Ticker {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ticker.class);

    private final Thread thread;
    private final long tickNanos;

    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    private volatile boolean running = true;

    /**
     * Constructs the {@linkplain Ticker ticker} and runs it immediately.
     *
     * @param server the server that the game logic loop should be handled for
     * @since 1.0
     */
    public Ticker(@NonNull JetMinecraftServer server) {
        this.tickNanos = server.configuration().tickDuration() * 1_000_000;
        this.thread = Thread.ofPlatform()
                .name("Main ticking thread")
                .uncaughtExceptionHandler((thread, throwable) -> {
                    LOGGER.error("An error occurred in the main ticking thread", throwable);
                    server.shutdown();
                })
                .unstarted(this::loop);
    }

    /**
     * Schedules the specified {@linkplain Runnable runnable} to be run at an end of a tick.
     *
     * <p>If the {@linkplain Thread#currentThread() current thread} is the {@linkplain Thread thread}
     * that runs the game logic loop, the specified runnable is run immediately.</p>
     *
     * @param runnable the runnable to be run
     * @since 1.0
     */
    public void scheduleTask(@NonNull Runnable runnable) {
        if (Thread.currentThread() == this.thread) {
            runnable.run();
        } else {
            this.tasks.offer(runnable);
        }
    }

    /**
     * Ensures that the {@linkplain Thread#currentThread() current thread}
     * is the {@linkplain Thread thread} that runs the game logic loop.
     *
     * @throws IllegalStateException if the current thread is not the thread that runs the game logic loop
     * @since 1.0
     */
    public void ensureRunsInTickLoop() {
        if (Thread.currentThread() != this.thread)
            throw new IllegalStateException("The current thread is not the main tick loop thread");
    }

    /**
     * Schedules the game logic loop to start.
     *
     * @throws IllegalStateException if the ticker was already started
     * @since 1.0
     */
    public void start() {
        if (this.thread.isAlive())
            throw new IllegalStateException("The ticker was already started");
        this.thread.start();
    }

    /**
     * Schedules the game logic loop to finish after the current loop cycle, or finishes it immediately if no cycle
     * is currently running. Nothing happens if the loop has already been finished.
     *
     * @since 1.0
     */
    public void shutdown() {
        this.running = false;
    }

    private void loop() {
        long nextTickNanos = System.nanoTime();
        while (this.running) {
            // TODO: Run the other tick logic

            while (true) {
                Runnable task = this.tasks.poll();
                if (task == null) break;
                task.run();
            }

            nextTickNanos += this.tickNanos;
            LockSupport.parkNanos(nextTickNanos - System.nanoTime());
        }
    }
}