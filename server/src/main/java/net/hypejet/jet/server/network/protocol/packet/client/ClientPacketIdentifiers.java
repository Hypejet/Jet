package net.hypejet.jet.server.network.protocol.packet.client;

/**
 * Represents a registry of identifiers of {@linkplain net.hypejet.jet.protocol.packet.client.ClientPacket client
 * packets}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ClientPacketIdentifiers {

    public static final int HANDSHAKE = 0;

    public static final int STATUS_SERVER_LIST_REQUEST = nextStatusId();
    public static final int STATUS_PING_REQUEST = nextStatusId();

    public static final int LOGIN_REQUEST = nextLoginId();
    public static final int LOGIN_ENCRYPTION_RESPONSE = nextLoginId();
    public static final int LOGIN_PLUGIN_MESSAGE_RESPONSE = nextLoginId();
    public static final int LOGIN_ACKNOWLEDGE = nextLoginId();
    public static final int LOGIN_COOKIE_RESPONSE = nextLoginId();

    public static final int CONFIGURATION_CLIENT_INFORMATION = nextConfigurationId();
    public static final int CONFIGURATION_COOKIE_RESPONSE = nextConfigurationId();
    public static final int CONFIGURATION_PLUGIN_MESSAGE = nextConfigurationId();
    public static final int CONFIGURATION_ACKNOWLEDGE_FINISH_CONFIGURATION = nextConfigurationId();
    public static final int CONFIGURATION_KEEP_ALIVE = nextConfigurationId();
    public static final int CONFIGURATION_PONG = nextConfigurationId();
    public static final int CONFIGURATION_RESOURCE_PACK_RESPONSE = nextConfigurationId();
    public static final int CONFIGURATION_KNOWN_PACKS = nextConfigurationId();

    private static int currentStatusId = 0;
    private static int currentLoginId = 0;
    private static int currentConfigurationId = 0;
    private static int currentPlayId = 0;

    private ClientPacketIdentifiers() {}

    /**
     * Gets a next available identifier for a {@linkplain ??? client status packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextStatusId() {
        return currentStatusId++;
    }

    /**
     * Gets a next available identifier for a {@linkplain net.hypejet.jet.protocol.packet.client.ClientLoginPacket
     * client login packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextLoginId() {
        return currentLoginId++;
    }

    /**
     * Gets a next available identifier for a {@linkplain ??? client configuration packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextConfigurationId() {
        return currentConfigurationId++;
    }

    /**
     * Gets a next available identifier for a {@linkplain ??? client play packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextPlayId() {
        return currentPlayId++;
    }
}