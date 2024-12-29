package net.hypejet.jet.server.registry.writers.registry.chat;

import net.hypejet.jet.data.model.api.registries.chat.ChatType;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain ChatType a chat type}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see ChatType
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class ChatTypeBinaryTagWriter implements Writer<ChatType, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain ChatTypeBinaryTagWriter chat type binary tag writer}.
     *
     * @since 1.0
     */
    public static final ChatTypeBinaryTagWriter INSTANCE = new ChatTypeBinaryTagWriter();

    private static final String CHAT_FIELD = "chat";
    private static final String NARRATION_FIELD = "narration";

    private ChatTypeBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull ChatType object) {
        return CompoundBinaryTag.builder()
                .put(CHAT_FIELD, ChatDecorationBinaryTagWriter.INSTANCE.write(object.chatDecoration()))
                .put(NARRATION_FIELD, ChatDecorationBinaryTagWriter.INSTANCE.write(object.narrationDecoration()))
                .build();
    }
}