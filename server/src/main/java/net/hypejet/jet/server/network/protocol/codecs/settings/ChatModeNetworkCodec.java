package net.hypejet.jet.server.network.protocol.codecs.settings;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Player.ChatMode player
 * chat mode}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player.ChatMode
 * @see NetworkCodec
 */
public final class ChatModeNetworkCodec implements NetworkCodec<Player.ChatMode> {

    private static final ChatModeNetworkCodec INSTANCE = new ChatModeNetworkCodec();

    private static final IntObjectMap<Player.ChatMode> idToChatModeMap = new IntObjectHashMap<>();
    private static final EnumMap<Player.ChatMode, Integer> chatModeToIdMap = new EnumMap<>(Player.ChatMode.class);

    static {
        idToChatModeMap.put(0, Player.ChatMode.ENABLED);
        idToChatModeMap.put(1, Player.ChatMode.COMMANDS_ONLY);
        idToChatModeMap.put(2, Player.ChatMode.HIDDEN);
        idToChatModeMap.forEach((id, chatMode) -> chatModeToIdMap.put(chatMode, id));
    }

    private ChatModeNetworkCodec() {}

    @Override
    public Player.@NonNull ChatMode read(@NonNull ByteBuf buf) {
        int chatModeIdentifier = NetworkUtil.readVarInt(buf);
        Player.ChatMode chatMode = idToChatModeMap.get(chatModeIdentifier);

        if (chatMode == null) {
            throw new IllegalArgumentException("Unknown chat mode: " + chatModeIdentifier);
        }

        return chatMode;
    }

    @Override
    public void write(@NonNull ByteBuf buf, Player.@NonNull ChatMode object) {
        NetworkUtil.writeVarInt(buf, chatModeToIdMap.get(object));
    }

    /**
     * Gets an instance of the {@linkplain ChatModeNetworkCodec chat mode network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ChatModeNetworkCodec instance() {
        return INSTANCE;
    }
}