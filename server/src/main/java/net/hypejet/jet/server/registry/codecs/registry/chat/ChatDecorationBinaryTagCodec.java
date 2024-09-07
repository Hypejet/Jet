package net.hypejet.jet.server.registry.codecs.registry.chat;

import net.hypejet.jet.data.model.registry.registries.chat.decoration.ChatDecoration;
import net.hypejet.jet.data.model.registry.registries.chat.decoration.ChatDecorationParameter;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.component.StyleBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Set;

public final class ChatDecorationBinaryTagCodec implements BinaryTagCodec<ChatDecoration> {

    private static final String TRANSLATION_KEY_FIELD = "translation_key";
    private static final String STYLE_FIELD = "style";
    private static final String PARAMETERS_FIELD = "parameters";

    private ChatDecorationBinaryTagCodec() {}

    @Override
    public @NonNull ChatDecoration read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");

        ListBinaryTag parameterBinaryTags = compound.getList(PARAMETERS_FIELD);
        Set<ChatDecorationParameter> parameters = new HashSet<>();

        for (BinaryTag parameterBinaryTag : parameterBinaryTags)
            parameters.add()

        return new ChatDecoration(compound.getString(TRANSLATION_KEY_FIELD),
                BinaryTagUtil.read(STYLE_FIELD, compound, StyleBinaryTagCodec.instance()),
                );
    }

    @Override
    public @NonNull BinaryTag write(@NonNull ChatDecoration object) {
        return null;
    }
}