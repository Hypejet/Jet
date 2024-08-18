package net.hypejet.jet.server.session;

import net.hypejet.jet.event.events.player.configuration.PlayerConfigurationStartEvent;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.registry.JetRegistry;
import net.hypejet.jet.server.session.keepalive.KeepAliveHandler;
import net.hypejet.jet.session.handler.SessionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

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
public final class JetConfigurationSession implements Session<JetConfigurationSession>, SessionHandler,
        Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JetConfigurationSession.class);

    private final JetPlayer player;
    private final KeepAliveHandler keepAliveHandler;

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
        this.keepAliveHandler = new KeepAliveHandler(player, this, ServerKeepAliveConfigurationPacket::new);

        Thread.ofVirtual()
                .uncaughtExceptionHandler(this)
                .start(() -> {
                    SocketPlayerConnection connection = this.player.connection();
                    if (connection.isClosed()) return;

                    JetMinecraftServer server = connection.server();

                    server.eventNode().call(new PlayerConfigurationStartEvent(this.player));
                    this.keepAliveHandler.shutdown();

                    try {
                        this.keepAliveHandler.awaitTermination();
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception);
                    }

                    for (JetRegistry<?> registry : server.registryManager().getRegistries().values())
                        this.player.sendPacket(registry.getPacket());

                    this.player.sendServerBrand(server.brandName());

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

    @Override
    public void onConnectionClose(@Nullable Throwable cause) {
        if (this.keepAliveHandler.isAlive()) {
            this.keepAliveHandler.shutdownNow();

            try {
                this.keepAliveHandler.awaitTermination();
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }

        if (this.acknowledgeLatch.getCount() > 0) {
            this.acknowledgeLatch.countDown();
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Objects.requireNonNull(e, "The throwable must not be null");
        this.player.disconnect(Component.text("An error occurred during the configuration", NamedTextColor.RED));
        LOGGER.error("An error occurred during the configuration of a player", e);
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
     * Gets a {@linkplain KeepAliveHandler keep alive handler} of this session.
     *
     * @return the keep alive handler
     * @since 1.0
     */
    public @NonNull KeepAliveHandler keepAliveHandler() {
        return this.keepAliveHandler;
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