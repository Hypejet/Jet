package net.hypejet.jet.pack;

import net.hypejet.jet.protocol.packet.client.configuration.ClientResourcePackResponseConfigurationPacket;

/**
 * Represents a result of the resource pack load.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientResourcePackResponseConfigurationPacket
 */
public enum ResourcePackResult {
    /**
     * A result used when a client states that the resource pack was loaded successfully.
     *
     * @since 1.0
     */
    SUCCESS,
    /**
     * A result used when a client states that the resource pack was loaded successfully.
     *
     * @since 1.0
     */
    DECLINED,
    /**
     * A result used when a client states that it could not download the resource pack.
     *
     * @since 1.0
     */
    FAILED_TO_DOWNLOAD,
    /**
     * A result used when a client states that the client accepted the resource pack.
     *
     * @since 1.0
     */
    ACCEPTED,
    /**
     * A result used when a client states that the resource pack was downloaded successfully.
     *
     * @since 1.0
     */
    DOWNLOADED,
    /**
     * A result used when a client states that it could not find an url resource pack.
     *
     * @since 1.0
     */
    INVALID_URL,
    /**
     * A result used when a client states that the resource pack was could not be reloaded.
     *
     * @since 1.0
     */
    FAILED_TO_RELOAD,
    /**
     * A result used when a client states that the resource pack was discarded.
     *
     * @since 1.0
     */
    DISCARDED
}
