package net.hypejet.jet.server.network.session.keepalive;

/**
 * Represents a function that handles a client response for a keep alive packet.
 *
 * @since 1.0
 * @author Codestech
 */
@FunctionalInterface
public interface KeepAliveResponseHandler {
    /**
     * Handles a client response for a keep alive.
     *
     * @param keepAliveIdentifier an identifier of the keep alive
     * @since 1.0
     */
    void handleKeepAliveResponse(long keepAliveIdentifier);
}