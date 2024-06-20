package net.hypejet.jet.session.handler;

import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponseLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.session.LoginSession;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain SessionHandler session handler}, which handles a {@linkplain LoginSession login session}.
 *
 * @since 1.0
 * @author Codestech
 * @see LoginSession
 * @see SessionHandler
 */
public interface LoginSessionHandler extends SessionHandler {
    /**
     * Called when a {@linkplain ClientLoginRequestLoginPacket login request packet} is received.
     *
     * @param packet the packet
     * @param session a session manager, which uses this handler
     * @since 1.0
     */
    default void onLoginRequest(@NonNull ClientLoginRequestLoginPacket packet, @NonNull LoginSession session) {}

    /**
     * Called when a {@linkplain ClientCookieResponseLoginPacket cookie response packet} is received.
     *
     * @param packet the packet
     * @param session a session manager, which uses this handler
     * @since 1.0
     */
    default void onCookieResponse(@NonNull ClientCookieResponseLoginPacket packet, @NonNull LoginSession session) {}

    /**
     * Called when a {@linkplain ClientEncryptionResponseLoginPacket encryption response packet} is received.
     *
     * @param packet the packet
     * @param session a session manager, which uses this handler
     * @since 1.0
     */
    default void onEncryptionResponse(@NonNull ClientEncryptionResponseLoginPacket packet,
                                      @NonNull LoginSession session) {}

    /**
     * Called when a {@linkplain ClientLoginAcknowledgeLoginPacket login acknowledge packet} is received.
     *
     * @param packet the packet
     * @param session a session manager, which uses this handler
     * @since 1.0
     */
    default void onLoginAcknowledge(@NonNull ClientLoginAcknowledgeLoginPacket packet,
                                    @NonNull LoginSession session) {}

    /**
     * Called when a {@linkplain ClientPluginMessageResponseLoginPacket login message response packet} is received.
     *
     * @param packet the packet
     * @param session a session manager, which uses this handler
     * @since 1.0
     */
    default void onPluginMessage(@NonNull ClientPluginMessageResponseLoginPacket packet,
                                 @NonNull LoginSession session) {}

    /**
     * Called when a {@linkplain LoginSession login session} waits for something too long.
     *
     * @param session the login session
     * @param type a type of the time-out
     * @since 1.0
     */
    default void onTimeOut(@NonNull LoginSession session, @NonNull TimeOutType type) {}

    /**
     * Represents a type of {@linkplain LoginSession login session} time-out.
     *
     * @since 1.0
     * @author Codestech
     * @see LoginSession
     */
    enum TimeOutType {
        /**
         * A time-out type used when a {@linkplain LoginSessionHandler login session handler} handles the login session
         * for too long.
         *
         * @since 1.0
         * @see LoginSessionHandler
         */
        HANDLER,
        /**
         * A time-out type used when a client does not respond for a {@linkplain ServerLoginSuccessLoginPacket login
         * success packet} for too long.
         *
         * @since 1.0
         * @see ServerLoginSuccessLoginPacket
         */
        ACKNOWLEDGE
    }
}