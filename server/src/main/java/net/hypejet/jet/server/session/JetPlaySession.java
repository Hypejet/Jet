package net.hypejet.jet.server.session;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.server.play.ServerGameEventPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerKeepAlivePlayPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.keepalive.KeepAliveHandler;
import net.hypejet.jet.session.handler.SessionHandler;
import net.hypejet.jet.world.GameEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;

/**
 * Represents a {@linkplain Session session}, which handles a {@linkplain net.hypejet.jet.protocol.ProtocolState#PLAY
 * play protocol state} of a {@linkplain JetPlayer player}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.ProtocolState#PLAY
 * @see JetPlayer
 * @see Session
 */
public final class JetPlaySession implements Session<JetPlaySession>, SessionHandler, Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JetPlaySession.class);

    private final JetPlayer player;
    private final KeepAliveHandler keepAliveHandler;

    /**
     * Constructs the {@linkplain JetPlaySession play session}.
     *
     * @param player a player that the session should be handled for
     * @since 1.0
     */
    public JetPlaySession(@NonNull JetPlayer player) {
        this.player = player;
        this.keepAliveHandler = new KeepAliveHandler(player, this, ServerKeepAlivePlayPacket::new);

        Key overworld = Key.key("overworld");

        player.sendPacket(new ServerJoinGamePlayPacket(player.entityId(), false, Collections.singleton(overworld),
                20, 5, 5, false, true, false, 0, overworld, 0, Player.GameMode.SURVIVAL, null, false, true,
                null, 2, false));

        player.sendPacket(new ServerGameEventPlayPacket(GameEvent.waitForWorldChunks()));
    }

    @Override
    public @NonNull JetPlaySession sessionHandler() {
        return this;
    }

    @Override
    public void onConnectionClose(@Nullable Throwable cause) {
        if (this.keepAliveHandler.isAlive()) {
            this.keepAliveHandler.shutdownNow();
        }
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Objects.requireNonNull(e, "The throwable must not be null");
        this.keepAliveHandler.shutdownNow();
        this.player.disconnect(Component.text("An error occurred during the play session", NamedTextColor.RED));
        LOGGER.error("An error occurred during the play session", e);
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
     * Casts a {@linkplain Session session} to the {@linkplain JetPlaySession play session}
     * or throws an exception.
     *
     * @param session the session to cast
     * @return the cast session
     * @throws IllegalStateException if session is not a play session
     * @since 1.0
     */
    public static @NonNull JetPlaySession asPlaySession(@Nullable Session<?> session) {
        if (session instanceof JetPlaySession playSession) return playSession;
        throw new IllegalStateException("The session is not a play session");
    }
}