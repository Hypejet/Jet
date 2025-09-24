package net.hypejet.jet.server.registry.codecs.chat;

import net.hypejet.jet.chat.ChatType;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain ChatType chat types}.
 *
 * @since 1.0
 * @see ChatType
 * @see BinaryTagCodec
 */
@NullMarked
public final class ChatTypeBinaryTagCodec implements BinaryTagCodec<ChatType> {

    private static final String CHAT_FIELD = "chat";
    private static final String NARRATION_FIELD = "narration";

    /**
     * An instance of the {@linkplain ChatTypeBinaryTagCodec chat-type binary tag codec}.
     *
     * @since 1.0
     */
    public static final ChatTypeBinaryTagCodec INSTANCE = new ChatTypeBinaryTagCodec();

    private ChatTypeBinaryTagCodec() {}

    @Override
    public ChatType decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return new ChatType(
                    ChatTypeDecorationBinaryTagCodec.INSTANCE.decode(requiredTag(CHAT_FIELD, compound), server),
                    ChatTypeDecorationBinaryTagCodec.INSTANCE.decode(requiredTag(NARRATION_FIELD, compound), server)
            );
        } else {
            throw new IllegalArgumentException("The encoded tag must be of compound type to decode it to a chat type");
        }
    }

    @Override
    public BinaryTag encode(ChatType value, JetMinecraftServer server) {
        return CompoundBinaryTag.builder()
                .put(CHAT_FIELD, ChatTypeDecorationBinaryTagCodec.INSTANCE.encode(value.chat(), server))
                .put(NARRATION_FIELD, ChatTypeDecorationBinaryTagCodec.INSTANCE.encode(value.narration(), server))
                .build();
    }
}