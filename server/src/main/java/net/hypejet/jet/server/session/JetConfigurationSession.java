package net.hypejet.jet.server.session;

import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import net.hypejet.jet.event.events.player.configuration.PlayerConfigurationStartEvent;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKeepAliveConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.util.thread.JetThreadFactory;
import net.hypejet.jet.session.handler.SessionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a {@linkplain Session session} and a {@linkplain SessionHandler session handler}, which handles
 * a {@linkplain net.hypejet.jet.protocol.ProtocolState#CONFIGURATION configuration protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.ProtocolState#CONFIGURATION
 * @see Session
 * @see SessionHandler
 */
public final class JetConfigurationSession implements Session<JetConfigurationSession>, SessionHandler {

    private static final int TIME_OUT_DURATION = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private static final int KEEP_ALIVE_INTERVAL = 10;
    private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;

    private static final Logger LOGGER = LoggerFactory.getLogger(JetConfigurationSession.class);

    private final ScheduledExecutorService keepAliveExecutor;

    private final ReentrantLock keepAliveLock = new ReentrantLock();
    private final LongObjectMap<CountDownLatch> keepAliveLatches = new LongObjectHashMap<>();

    private final JetPlayer player;

    private final CountDownLatch acknowledgeLatch = new CountDownLatch(1);
    private boolean finished;

    /**
     * Constructs the {@linkplain JetConfigurationSession configuration session}.
     *
     * @param player a player that is being configured
     * @since 1.0
     */
    public JetConfigurationSession(@NonNull JetPlayer player) {
        this.player = player;

        this.keepAliveExecutor = Executors.newSingleThreadScheduledExecutor(JetThreadFactory.builder()
                .name("Keep alive thread - " + player.username())
                .threadType(JetThreadFactory.ThreadType.VIRTUAL)
                .exceptionHandler((thread, throwable) -> this.handleThrowable(throwable))
                .build());
        this.keepAliveExecutor.scheduleAtFixedRate(this::requestKeepAlive, 0, KEEP_ALIVE_INTERVAL, KEEP_ALIVE_UNIT);

        Thread.ofVirtual()
                .uncaughtExceptionHandler((thread, throwable) -> this.handleThrowable(throwable))
                .start(() -> {
                    SocketPlayerConnection connection = this.player.connection();

                    connection.server().eventNode().call(new PlayerConfigurationStartEvent(this.player));
                    this.keepAliveExecutor.shutdown();

                    try {
                        if (!this.keepAliveExecutor.awaitTermination(TIME_OUT_DURATION, TIME_OUT_UNIT)) {
                            this.handleTimeOut();
                        }
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception);
                    }

                    this.finished = true;
                    connection.sendPacket(new ServerFinishConfigurationPacket());

                    try {
                        if (!this.acknowledgeLatch.await(TIME_OUT_DURATION, TIME_OUT_UNIT)) {
                            throw new TimeoutException("The configuration was not acknowledged on time");
                        }
                    } catch (InterruptedException | TimeoutException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }

    @Override
    public @NonNull JetConfigurationSession sessionHandler() {
        return this;
    }

    /**
     * Handles a client response for a keep alive packet.
     *
     * @param packet the response
     * @since 1.0
     */
    public void handleKeepAlive(@NonNull ClientKeepAliveConfigurationPacket packet) {
        this.keepAliveLock.lock();
        try {
            long keepAliveIdentifier = packet.keepAliveIdentifier();
            CountDownLatch latch = this.keepAliveLatches.remove(keepAliveIdentifier);

            if (latch == null)
                throw new IllegalArgumentException("Unknown keep alive with identifier of: " + keepAliveIdentifier);

            latch.countDown();
        } finally {
            this.keepAliveLock.unlock();
        }
    }

    /**
     * Handles a client response for a configuration finish.
     *
     * @since 1.0
     */
    public void handleFinishAcknowledge() {
        if (!this.finished)
            throw new IllegalArgumentException("The configuration state was not finished");
        this.acknowledgeLatch.countDown();
    }

    /**
     * Handles an error that occurred during this session.
     *
     * @param throwable the error
     * @since 1.0
     */
    private void handleThrowable(@NonNull Throwable throwable) {
        Objects.requireNonNull(throwable, "The throwable must not be null");
        this.player.disconnect(Component.text("An error occurred during the configuration", NamedTextColor.RED));
        LOGGER.error("An error occurred during the configuration of a player", throwable);
        this.keepAliveExecutor.shutdownNow();
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

        this.player.sendPacket(new ServerKeepAliveConfigurationPacket(keepAliveId));

        try {
            if (!latch.await(TIME_OUT_DURATION, TIME_OUT_UNIT)) {
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
        this.player.disconnect(Component.text("Timed out"));
    }

    /**
     * Casts a {@linkplain Session session} to the {@linkplain JetConfigurationSession configuration session}
     * or throws an exception.
     *
     * @param session the session to cast
     * @return the cast session
     * @throws IllegalStateException if session is not a configuration session
     * @since 1.0
     */
    public static @NonNull JetConfigurationSession asConfigurationSession(@Nullable Session<?> session) {
        if (session instanceof JetConfigurationSession configurationSession) return configurationSession;
        throw new IllegalStateException("The session is not a configuration session");
    }
}