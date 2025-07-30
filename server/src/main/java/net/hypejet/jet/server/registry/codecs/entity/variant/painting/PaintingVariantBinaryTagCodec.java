package net.hypejet.jet.server.registry.codecs.entity.variant.painting;

import net.hypejet.jet.entity.variant.painting.PaintingVariant;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.ComponentBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain PaintingVariant painting variants}.
 *
 * @since 1.0
 * @see PaintingVariant
 * @see BinaryTagCodec
 */
public final class PaintingVariantBinaryTagCodec implements BinaryTagCodec<PaintingVariant> {

    private static final String WIDTH_FIELD = "width";
    private static final String HEIGHT_FIELD = "height";
    private static final String ASSET_FIELD = "asset";
    private static final String TITLE_FIELD = "title";
    private static final String AUTHOR_FIELD = "author";

    /**
     * An instance of the {@linkplain PaintingVariantBinaryTagCodec painting variant binary-tag codec}.
     *
     * @since 1.0
     */
    public static final PaintingVariantBinaryTagCodec INSTANCE = new PaintingVariantBinaryTagCodec();

    private PaintingVariantBinaryTagCodec() {}

    @Override
    public @NotNull PaintingVariant decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            BinaryTag titleTag = compound.get(TITLE_FIELD);
            BinaryTag authorTag = compound.get(AUTHOR_FIELD);
            return new PaintingVariant(
                    requiredTag(WIDTH_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(HEIGHT_FIELD, compound, BinaryTagTypes.INT).value(),
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound)),
                    titleTag == null ? null : ComponentBinaryTagCodec.INSTANCE.decode(titleTag),
                    authorTag == null ? null : ComponentBinaryTagCodec.INSTANCE.decode(authorTag)
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a painting variant"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull PaintingVariant decoded) throws Exception {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putInt(WIDTH_FIELD, decoded.width())
                .putInt(HEIGHT_FIELD, decoded.height())
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(decoded.asset()));

        Component title = decoded.title();
        if (title != null) {
            builder.put(TITLE_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(title));
        }

        Component author = decoded.author();
        if (author != null) {
            builder.put(AUTHOR_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(author));
        }

        return builder.build();
    }
}