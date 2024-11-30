package net.hypejet.jet.pack;

/**
 * Represents a state of resource pack loading.
 *
 * @since 1.0
 * @author Codestech
 */
public enum ResourcePackState {
    /**
     * A state used when a client states that the resource pack was loaded successfully.
     *
     * @since 1.0
     */
    SUCCESS,
    /**
     * A state used when a client states that the resource pack was loaded successfully.
     *
     * @since 1.0
     */
    DECLINED,
    /**
     * A state used when a client states that it could not download the resource pack.
     *
     * @since 1.0
     */
    FAILED_TO_DOWNLOAD,
    /**
     * A state used when a client states that the client accepted the resource pack.
     *
     * @since 1.0
     */
    ACCEPTED,
    /**
     * A state used when a client states that the resource pack was downloaded successfully.
     *
     * @since 1.0
     */
    DOWNLOADED,
    /**
     * A state used when a client states that it could not find an url resource pack.
     *
     * @since 1.0
     */
    INVALID_URL,
    /**
     * A state used when a client states that the resource pack was could not be reloaded.
     *
     * @since 1.0
     */
    FAILED_TO_RELOAD,
    /**
     * A state used when a client states that the resource pack was discarded.
     *
     * @since 1.0
     */
    DISCARDED
}
