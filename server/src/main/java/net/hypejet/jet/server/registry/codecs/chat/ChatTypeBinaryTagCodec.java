package net.hypejet.jet.server.registry.codecs.chat;

import net.hypejet.jet.chat.ChatType;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain ChatType chat types}.
 *
 * @since 1.0
 * @see ChatType
 * @see BinaryTagCodec
 */
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
    public @NotNull ChatType decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new ChatType(
                    ChatTypeDecorationBinaryTagCodec.INSTANCE.decode(requiredTag(CHAT_FIELD, compound)),
                    ChatTypeDecorationBinaryTagCodec.INSTANCE.decode(requiredTag(NARRATION_FIELD, compound))
            );
        } else {
            throw new IllegalArgumentException("The encoded tag must be of compound type to decode it to a chat type");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull ChatType decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(CHAT_FIELD, ChatTypeDecorationBinaryTagCodec.INSTANCE.encode(decoded.chat()))
                .put(NARRATION_FIELD, ChatTypeDecorationBinaryTagCodec.INSTANCE.encode(decoded.narration()))
                .build();
    }
}