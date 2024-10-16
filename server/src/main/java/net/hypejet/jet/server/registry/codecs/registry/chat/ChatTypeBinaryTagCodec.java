package net.hypejet.jet.server.registry.codecs.registry.chat;

import net.hypejet.jet.data.model.api.registry.registries.chat.ChatType;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain ChatType chat type}.
 *
 * @since 1.0
 * @author Codestech
 * @see ChatType
 * @see BinaryTagCodec
 */
public final class ChatTypeBinaryTagCodec implements BinaryTagCodec<ChatType> {

    private static final ChatTypeBinaryTagCodec INSTANCE = new ChatTypeBinaryTagCodec();

    private static final String CHAT_FIELD = "chat";
    private static final String NARRATION_FIELD = "narration";

    private ChatTypeBinaryTagCodec() {}

    @Override
    public @NonNull ChatType read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");
        ChatDecorationBinaryTagCodec decorationCodec = ChatDecorationBinaryTagCodec.instance();
        return new ChatType(BinaryTagUtil.read(CHAT_FIELD, compound, decorationCodec),
                BinaryTagUtil.read(NARRATION_FIELD, compound, decorationCodec));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull ChatType object) {
        return CompoundBinaryTag.builder()
                .put(CHAT_FIELD, ChatDecorationBinaryTagCodec.instance().write(object.chatDecoration()))
                .put(NARRATION_FIELD, ChatDecorationBinaryTagCodec.instance().write(object.narrationDecoration()))
                .build();
    }

    /**
     * Gets an instance of the {@linkplain ChatTypeBinaryTagCodec chat type binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ChatTypeBinaryTagCodec instance() {
        return INSTANCE;
    }
}