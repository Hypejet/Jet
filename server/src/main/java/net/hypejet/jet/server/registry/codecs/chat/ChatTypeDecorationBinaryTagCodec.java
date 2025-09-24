package net.hypejet.jet.server.registry.codecs.chat;

import net.hypejet.jet.chat.ChatTypeDecoration;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.StyleBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.ListBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain ChatTypeDecoration chat type decoration}.
 *
 * @since 1.0
 * @see ChatTypeDecoration
 * @see BinaryTagCodec
 */
@NullMarked
public final class ChatTypeDecorationBinaryTagCodec implements BinaryTagCodec<ChatTypeDecoration> {

    private static final String TRANSLATION_KEY_FIELD = "translation_key";
    private static final String PARAMETERS_FIELD = "parameters";
    private static final String STYLE_FIELD = "style";

    private static final ListBinaryTagCodec<ChatTypeDecoration.Parameter> PARAMETERS_CODEC = new ListBinaryTagCodec<>(
            new IndexBinaryTagCodec<>(
                    IndexUtil.fromMap(Map.of(
                            "sender", ChatTypeDecoration.Parameter.SENDER,
                            "target", ChatTypeDecoration.Parameter.TARGET,
                            "content", ChatTypeDecoration.Parameter.CONTENT
                    )),
                    StringBinaryTagCodec.INSTANCE
            )
    );

    /**
     * An instance of the {@linkplain ChatTypeDecorationBinaryTagCodec chat-type-decoration binary tag codec}.
     *
     * @since 1.0
     */
    public static final ChatTypeDecorationBinaryTagCodec INSTANCE = new ChatTypeDecorationBinaryTagCodec();

    private ChatTypeDecorationBinaryTagCodec() {}

    @Override
    public ChatTypeDecoration decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            BinaryTag styleTag = compound.get(STYLE_FIELD);
            return new ChatTypeDecoration(
                    requiredTag(TRANSLATION_KEY_FIELD, compound, BinaryTagTypes.STRING).value(),
                    PARAMETERS_CODEC.decode(requiredTag(PARAMETERS_FIELD, compound), server),
                    styleTag == null ? Style.empty() : StyleBinaryTagCodec.INSTANCE.decode(styleTag, server)
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a chat type decoration"
            );
        }
    }

    @Override
    public BinaryTag encode(ChatTypeDecoration value, JetMinecraftServer server) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(TRANSLATION_KEY_FIELD, value.translationKey())
                .put(PARAMETERS_FIELD, PARAMETERS_CODEC.encode(value.parameters(), server));

        Style style = value.style();
        if (!style.isEmpty()) {
            builder.put(STYLE_FIELD, StyleBinaryTagCodec.INSTANCE.encode(style, server));
        }

        return builder.build();
    }
}