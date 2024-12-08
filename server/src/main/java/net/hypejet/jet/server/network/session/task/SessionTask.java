package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;

/**
 * Represents a task of {@linkplain net.hypejet.jet.server.network.session.Session a session}.
 *
 * @since 1.0
 * @author Codestech
 */
public interface SessionTask extends NetworkDisconnectionHandler {}