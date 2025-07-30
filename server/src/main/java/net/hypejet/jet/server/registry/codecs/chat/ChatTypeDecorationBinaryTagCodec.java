package net.hypejet.jet.server.registry.codecs.chat;

import net.hypejet.jet.chat.ChatTypeDecoration;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.StyleBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.ListBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain ChatTypeDecoration chat type decoration}.
 *
 * @since 1.0
 * @see ChatTypeDecoration
 * @see BinaryTagCodec
 */
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
    public @NotNull ChatTypeDecoration decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new ChatTypeDecoration(
                    requiredTag(TRANSLATION_KEY_FIELD, compound, BinaryTagTypes.STRING).value(),
                    PARAMETERS_CODEC.decode(requiredTag(PARAMETERS_FIELD, compound)),
                    StyleBinaryTagCodec.INSTANCE.decode(requiredTag(STYLE_FIELD, compound))
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a chat type decoration"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull ChatTypeDecoration decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .putString(TRANSLATION_KEY_FIELD, decoded.translationKey())
                .put(PARAMETERS_FIELD, PARAMETERS_CODEC.encode(decoded.parameters()))
                .put(STYLE_FIELD, StyleBinaryTagCodec.INSTANCE.encode(decoded.style()))
                .build();
    }
}