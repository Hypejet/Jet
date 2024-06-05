package net.hypejet.jet.server.network.protocol.packet.server;

/**
 * Represents a registry of {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packet} identifiers.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ServerPacketIdentifiers {

    public static final int LOGIN_DISCONNECT = nextLoginId();
    public static final int LOGIN_ENCRYPTION_REQUEST = nextLoginId();
    public static final int LOGIN_SUCCESS = nextLoginId();
    public static final int LOGIN_ENABLE_COMPRESSION = nextLoginId();
    public static final int LOGIN_PLUGIN_MESSAGE_REQUEST = nextLoginId();
    public static final int LOGIN_COOKIE_REQUEST = nextLoginId();

    private static int currentStatusId = 0;
    private static int currentLoginId = 0;
    private static int currentConfigurationId = 0;
    private static int currentPlayId = 0;

    private ServerPacketIdentifiers() {}

    /**
     * Gets a next available identifier for a {@linkplain ??? server status packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextStatusId() {
        return currentStatusId++;
    }

    /**
     * Gets a next available identifier for a {@linkplain net.hypejet.jet.protocol.packet.server.ServerLoginPacket
     * server login packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextLoginId() {
        return currentLoginId++;
    }

    /**
     * Gets a next available identifier for a {@linkplain ??? server configuration packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextConfigurationId() {
        return currentConfigurationId++;
    }

    /**
     * Gets a next available identifier for a {@linkplain ??? server play packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextPlayId() {
        return currentPlayId++;
    }
}