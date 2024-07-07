package net.hypejet.jet.server.network.protocol.packet.server;

/**
 * Represents a registry of {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packet} identifiers.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ServerPacketIdentifiers {

    public static final int STATUS_SERVER_LIST_RESPONSE = nextStatusId();
    public static final int STATUS_PING_RESPONSE = nextStatusId();

    public static final int LOGIN_DISCONNECT = nextLoginId();
    public static final int LOGIN_ENCRYPTION_REQUEST = nextLoginId();
    public static final int LOGIN_SUCCESS = nextLoginId();
    public static final int LOGIN_ENABLE_COMPRESSION = nextLoginId();
    public static final int LOGIN_PLUGIN_MESSAGE_REQUEST = nextLoginId();
    public static final int LOGIN_COOKIE_REQUEST = nextLoginId();

    public static final int CONFIGURATION_COOKIE_REQUEST = nextConfigurationId();
    public static final int CONFIGURATION_PLUGIN_MESSAGE = nextConfigurationId();
    public static final int CONFIGURATION_DISCONNECT = nextConfigurationId();
    public static final int CONFIGURATION_FINISH_CONFIGURATION = nextConfigurationId();
    public static final int CONFIGURATION_KEEP_ALIVE = nextConfigurationId();
    public static final int CONFIGURATION_PING = nextConfigurationId();
    public static final int CONFIGURATION_RESET_CHAT = nextConfigurationId();
    public static final int CONFIGURATION_REGISTRY_DATA = nextConfigurationId();
    public static final int CONFIGURATION_REMOVE_RESOURCE_PACK = nextConfigurationId();
    public static final int CONFIGURATION_ADD_RESOURCE_PACK = nextConfigurationId();
    public static final int CONFIGURATION_STORE_COOKIE = nextConfigurationId();
    public static final int CONFIGURATION_TRANSFER = nextConfigurationId();
    public static final int CONFIGURATION_FEATURE_FLAGS = nextConfigurationId();
    public static final int CONFIGURATION_UPDATE_TAGS = nextConfigurationId();
    public static final int CONFIGURATION_KNOWN_PACKS = nextConfigurationId();
    public static final int CONFIGURATION_CUSTOM_REPORT_DETAILS = nextConfigurationId();
    public static final int CONFIGURATION_SERVER_LINKS = nextConfigurationId();

    public static final int PLAY_KEEP_ALIVE = 0x26;
    public static final int PLAY_DISCONNECT = 0x1D;

    private static int currentStatusId = 0;
    private static int currentLoginId = 0;
    private static int currentConfigurationId = 0;
    private static int currentPlayId = 0;

    private ServerPacketIdentifiers() {}

    /**
     * Gets a next available identifier for a {@linkplain net.hypejet.jet.protocol.packet.server.ServerStatusPacket
     * server status packet}.
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
     * Gets a next available identifier for
     * a {@linkplain net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket server configuration packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextConfigurationId() {
        return currentConfigurationId++;
    }

    /**
     * Gets a next available identifier for
     * a {@linkplain net.hypejet.jet.protocol.packet.server.ServerPlayPacket server play packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextPlayId() {
        return currentPlayId++;
    }
}