package net.hypejet.jet.server.registry.codecs.registry.chat;

import net.hypejet.jet.data.model.registry.registries.chat.ChatType;
import net.hypejet.jet.data.model.registry.registries.chat.decoration.ChatDecoration;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.component.StyleBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ChatTypeBinaryTagCodec implements BinaryTagCodec<ChatType> {

    private static final String CHAT_FIELD = "chat";
    private static final String NARRATION_FIELD = "narration";

    private ChatTypeBinaryTagCodec() {}

    @Override
    public @NonNull ChatType read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");
        StyleBinaryTagCodec styleCodec = StyleBinaryTagCodec.instance();
        ChatDecoration
        return new ChatType(BinaryTagUtil.read(CHAT_FIELD, compound, styleCodec), BinaryTagUtil.read());
    }

    @Override
    public @NonNull BinaryTag write(@NonNull ChatType object) {
        return null;
    }
}