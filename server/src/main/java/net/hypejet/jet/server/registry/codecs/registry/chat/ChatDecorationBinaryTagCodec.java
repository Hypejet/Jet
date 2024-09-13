package net.hypejet.jet.server.registry.codecs.registry.chat;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.registry.registries.chat.decoration.ChatDecoration;
import net.hypejet.jet.data.model.registry.registries.chat.decoration.ChatDecorationParameter;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.mapper.MapperBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.component.StyleBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain ChatDecoration cha decoration}.
 *
 * @since 1.0
 * @author Codestech
 * @see ChatDecoration
 * @see BinaryTagCodec
 */
public final class ChatDecorationBinaryTagCodec implements BinaryTagCodec<ChatDecoration> {

    private static final String TRANSLATION_KEY_FIELD = "translation_key";
    private static final String STYLE_FIELD = "style";
    private static final String PARAMETERS_FIELD = "parameters";

    private static final BinaryTagCodec<ChatDecorationParameter> PARAMETER_CODEC = MapperBinaryTagCodec.codec(
            Mapper.builder(ChatDecorationParameter.class, String.class)
                    .register(ChatDecorationParameter.SENDER, "sender")
                    .register(ChatDecorationParameter.TARGET, "target")
                    .register(ChatDecorationParameter.CONTENT, "content")
                    .build(),
            StringBinaryTag.class, StringBinaryTag::value, StringBinaryTag::stringBinaryTag
    );

    private static final ChatDecorationBinaryTagCodec INSTANCE = new ChatDecorationBinaryTagCodec();

    private ChatDecorationBinaryTagCodec() {}

    @Override
    public @NonNull ChatDecoration read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");

        ListBinaryTag parameterBinaryTags = compound.getList(PARAMETERS_FIELD);
        List<ChatDecorationParameter> parameters = new ArrayList<>();

        for (BinaryTag parameterBinaryTag : parameterBinaryTags)
            parameters.add(PARAMETER_CODEC.read(parameterBinaryTag));

        return new ChatDecoration(compound.getString(TRANSLATION_KEY_FIELD),
                BinaryTagUtil.readOptional(STYLE_FIELD, compound, StyleBinaryTagCodec.instance()),
                List.copyOf(parameters));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull ChatDecoration object) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(TRANSLATION_KEY_FIELD, object.translationKey());

        Style style = object.style();
        if (style != null)
            builder.put(STYLE_FIELD, StyleBinaryTagCodec.instance().write(style));

        List<BinaryTag> parameterBinaryTags = new ArrayList<>();
        for (ChatDecorationParameter parameter : object.parameters())
            parameterBinaryTags.add(PARAMETER_CODEC.write(parameter));

        return builder.put(PARAMETERS_FIELD, ListBinaryTag.from(parameterBinaryTags)).build();
    }

    /**
     * Gets an instance of the {@linkplain ChatDecorationBinaryTagCodec chat decoration binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ChatDecorationBinaryTagCodec instance() {
        return INSTANCE;
    }
}