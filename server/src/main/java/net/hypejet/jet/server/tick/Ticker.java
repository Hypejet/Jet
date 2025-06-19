package net.hypejet.jet.server.tick;

import net.hypejet.jet.server.JetMinecraftServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

/**
 * Represents a handler of a game logic loop of {@linkplain JetMinecraftServer a Minecraft server}.
 *
 * @since 1.0
 * @see JetMinecraftServer
 */
public final class Ticker {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ticker.class);

    private final Thread thread;
    private final long tickNanos;

    private volatile boolean running = true;

    /**
     * Constructs the {@linkplain Ticker ticker} and runs it immediately.
     *
     * @param server a server that the game logic loop should be handled for
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
                .start(this::loop);
    }

    /**
     * Ensures that {@linkplain Thread#currentThread() the current thread} is {@linkplain Thread a thread}
     * that runs the game logic loop.
     *
     * @throws IllegalStateException if the current thread is not the thread that runs the game logic loop
     * @since 1.0
     */
    public void ensureRunsInTickLoop() {
        if (Thread.currentThread() != this.thread)
            throw new IllegalStateException("The current thread is not the main tick loop thread");
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
            // TODO: Run the tick logic
            nextTickNanos += this.tickNanos;
            LockSupport.parkNanos(nextTickNanos - System.nanoTime());
        }
    }
}