package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.server.network.handler.NetworkDisconnectionHandler;

/**
 * Represents a task of {@linkplain net.hypejet.jet.server.network.session.Session a session}.
 *
 * @since 1.0
 * @author Codestech
 */
public sealed interface SessionTask extends NetworkDisconnectionHandler {
    /**
     * Represents {@linkplain SessionTask a session task}, which is executed in an event loop thread and after
     * when the session has been.
     *
     * @since 1.0
     * @see SessionTask
     */
    non-sealed interface EventLoopTask extends SessionTask {
        /**
         * Runs the task.
         *
         * @since 1.0
         */
        void runEventLoopTask();
    }

    /**
     * Represents {@linkplain SessionTask a session task}, which is executed in an external virtual thread, which
     * is created specifically to execute the task.
     *
     * @since 1.0
     * @see SessionTask
     */
    non-sealed interface VirtualThreadTask extends SessionTask {
        /**
         * Runs the task.
         *
         * @since 1.0
         */
        void runVirtualThreadTask();
    }
}