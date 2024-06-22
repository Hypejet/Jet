package net.hypejet.jet.server.session;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.session.handler.SessionHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

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

    private final JetPlayer player;

    /**
     * Constructs the {@linkplain JetConfigurationSession configuration session}.
     *
     * @param player a player that is being configured
     * @since 1.0
     */
    public JetConfigurationSession(@NonNull JetPlayer player) {
        this.player = player;
    }

    @Override
    public @NonNull JetConfigurationSession sessionHandler() {
        return this;
    }
}