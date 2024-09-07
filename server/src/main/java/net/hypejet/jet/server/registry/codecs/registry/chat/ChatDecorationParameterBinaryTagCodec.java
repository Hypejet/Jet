package net.hypejet.jet.server.registry.codecs.registry.chat;

import net.hypejet.jet.data.model.registry.registries.chat.decoration.ChatDecorationParameter;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ChatDecorationParameterBinaryTagCodec implements BinaryTagCodec<ChatDecorationParameter> {
    @Override
    public @NonNull ChatDecorationParameter read(@NonNull BinaryTag tag) {
        return null;
    }

    @Override
    public @NonNull BinaryTag write(@NonNull ChatDecorationParameter object) {
        return null;
    }
}