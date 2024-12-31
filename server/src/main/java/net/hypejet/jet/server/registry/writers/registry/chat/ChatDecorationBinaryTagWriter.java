package net.hypejet.jet.server.registry.writers.registry.chat;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.api.registries.chat.decoration.ChatDecoration;
import net.hypejet.jet.data.model.api.registries.chat.decoration.ChatDecorationParameter;
import net.hypejet.jet.server.registry.writers.mapper.MapperBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.component.StyleBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain ChatDecoration a chat decoration}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see ChatDecoration
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class ChatDecorationBinaryTagWriter implements Writer<ChatDecoration, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain ChatDecorationBinaryTagWriter chat decoration binary tag writer}.
     *
     * @since 1.0
     */
    public static final ChatDecorationBinaryTagWriter INSTANCE = new ChatDecorationBinaryTagWriter();

    private static final String TRANSLATION_KEY_FIELD = "translation_key";
    private static final String STYLE_FIELD = "style";
    private static final String PARAMETERS_FIELD = "parameters";

    private static final Writer<ChatDecorationParameter, StringBinaryTag> PARAMETER_CODEC =
            MapperBinaryTagWriter.stringCodec(Mapper.builder(ChatDecorationParameter.class, String.class)
                    .register(ChatDecorationParameter.SENDER, "sender")
                    .register(ChatDecorationParameter.TARGET, "target")
                    .register(ChatDecorationParameter.CONTENT, "content")
                    .build());

    private ChatDecorationBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull ChatDecoration object) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(TRANSLATION_KEY_FIELD, object.translationKey());

        Style style = object.style();
        if (style != null)
            builder.put(STYLE_FIELD, StyleBinaryTagWriter.INSTANCE.write(style));

        List<BinaryTag> parameterBinaryTags = new ArrayList<>();
        for (ChatDecorationParameter parameter : object.parameters())
            parameterBinaryTags.add(PARAMETER_CODEC.write(parameter));

        return builder.put(PARAMETERS_FIELD, ListBinaryTag.from(parameterBinaryTags)).build();
    }
}