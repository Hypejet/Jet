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

    public static final int PLAY_CONFIRM_TELEPORTATION = nextPlayId();
    public static final int PLAY_QUERY_BLOCK_ENTITY_TAG = nextPlayId();
    public static final int PLAY_CHANGE_DIFFICULTY = nextPlayId();
    public static final int PLAY_ACKNOWLEDGE_MESSAGE = nextPlayId();
    public static final int PLAY_CHAT_COMMAND = nextPlayId();
    public static final int PLAY_SIGNED_CHAT_COMMAND = nextPlayId();
    public static final int PLAY_CHAT_MESSAGE = nextPlayId();
    public static final int PLAY_PLAYER_SESSION = nextPlayId();
    public static final int PLAY_CHUNK_BATCH_RECEIVED = nextPlayId();
    public static final int PLAY_CLIENT_REQUEST_ACTION = nextPlayId();
    public static final int PLAY_CLIENT_INFORMATION = nextPlayId();
    public static final int PLAY_SUGGESTIONS_REQUEST = nextPlayId();
    public static final int PLAY_ACKNOWLEDGE_CONFIGURATION = nextPlayId();
    public static final int PLAY_CLICK_CONTAINER_BUTTON = nextPlayId();
    public static final int PLAY_CLICK_CONTAINER = nextPlayId();
    public static final int PLAY_CLOSE_CONTAINER = nextPlayId();
    public static final int PLAY_CHANGE_CONTAINER_SLOT_STATE = nextPlayId();
    public static final int PLAY_COOKIE_RESPONSE = nextPlayId();
    public static final int PLAY_PLUGIN_MESSAGE = nextPlayId();
    public static final int PLAY_DEBUG_SAMPLE_SUBSCRIPTION = nextPlayId();
    public static final int PLAY_EDIT_BOOK = nextPlayId();
    public static final int PLAY_QUERY_ENTITY_TAG = nextPlayId();
    public static final int PLAY_INTERACT = nextPlayId();
    public static final int PLAY_JIGSAW_GENERATE = nextPlayId();
    public static final int PLAY_KEEP_ALIVE = nextPlayId();
    public static final int PLAY_LOCK_DIFFICULTY = nextPlayId();
    public static final int PLAY_POSITION = nextPlayId();
    public static final int PLAY_ROTATION_AND_POSITION = nextPlayId();
    public static final int PLAY_ROTATION = nextPlayId();
    public static final int PLAY_ON_GROUND = nextPlayId();
    public static final int PLAY_MOVE_VEHICLE = nextPlayId();
    public static final int PLAY_PADDLE_BOAT = nextPlayId();
    public static final int PLAY_PICK_ITEM = nextPlayId();
    public static final int PLAY_PING_REQUEST = nextPlayId();
    public static final int PLAY_PLACE_RECIPE = nextPlayId();
    public static final int PLAY_ABILITIES = nextPlayId();
    public static final int PLAY_DIG_BLOCK = nextPlayId();
    public static final int PLAY_ACTION = nextPlayId();
    public static final int PLAY_STEER_VEHICLE = nextPlayId();
    public static final int PLAY_PONG = nextPlayId();
    public static final int PLAY_RECIPE_BOOK_STATE = nextPlayId();
    public static final int PLAY_SET_SEEN_RECIPE = nextPlayId();
    public static final int PLAY_RENAME_ITEM = nextPlayId();
    public static final int PLAY_RESOURCE_PACK_RESPONSE = nextPlayId();
    public static final int PLAY_SEEN_ADVANCEMENTS = nextPlayId();
    public static final int PLAY_SELECT_TRADE = nextPlayId();
    public static final int PLAY_SET_BEACON_EFFECT = nextPlayId();
    public static final int PLAY_SET_HELD_ITEM = nextPlayId();
    public static final int PLAY_PROGRAM_COMMAND_BLOCK = nextPlayId();
    public static final int PLAY_PROGRAM_MINECART_COMMAND_BLOCK = nextPlayId();
    public static final int PLAY_SET_CREATIVE_MODE_SLOT = nextPlayId();
    public static final int PLAY_PROGRAM_JIGSAW_BLOCK = nextPlayId();
    public static final int PLAY_PROGRAM_STRUCTURE_BLOCK = nextPlayId();
    public static final int PLAY_UPDATE_SIGN = nextPlayId();
    public static final int PLAY_SWING_HAND = nextPlayId();
    public static final int PLAY_SPECTATE = nextPlayId();
    public static final int PLAY_USE_ITEM_ON = nextPlayId();
    public static final int PLAY_USE_ITEM = nextPlayId();

    private static int currentStatusId = 0;
    private static int currentLoginId = 0;
    private static int currentConfigurationId = 0;
    private static int currentPlayId = 0;

    private ClientPacketIdentifiers() {}

    /**
     * Gets a next available identifier for a {@linkplain net.hypejet.jet.protocol.packet.client.ClientStatusPacket
     * client status packet}.
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
     * Gets a next available identifier for
     * a {@linkplain net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket client configuration packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextConfigurationId() {
        return currentConfigurationId++;
    }

    /**
     * Gets a next available identifier for
     * a {@linkplain net.hypejet.jet.protocol.packet.client.ClientPlayPacket client play packet}.
     *
     * @return the identifier
     * @since 1.0
     */
    private static int nextPlayId() {
        return currentPlayId++;
    }
}