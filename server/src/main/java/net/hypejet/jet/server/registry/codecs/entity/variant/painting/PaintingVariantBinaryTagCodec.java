package net.hypejet.jet.server.registry.codecs.entity.variant.painting;

import net.hypejet.jet.entity.variant.painting.PaintingVariant;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.ComponentBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain PaintingVariant painting variants}.
 *
 * @since 1.0
 * @see PaintingVariant
 * @see BinaryTagCodec
 */
@NullMarked
public final class PaintingVariantBinaryTagCodec implements BinaryTagCodec<PaintingVariant> {

    private static final String WIDTH_FIELD = "width";
    private static final String HEIGHT_FIELD = "height";
    private static final String ASSET_FIELD = "asset_id";
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
    public PaintingVariant decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            BinaryTag titleTag = compound.get(TITLE_FIELD);
            BinaryTag authorTag = compound.get(AUTHOR_FIELD);
            return new PaintingVariant(
                    requiredTag(WIDTH_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(HEIGHT_FIELD, compound, BinaryTagTypes.INT).value(),
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound), server),
                    titleTag == null ? null : ComponentBinaryTagCodec.INSTANCE.decode(titleTag, server),
                    authorTag == null ? null : ComponentBinaryTagCodec.INSTANCE.decode(authorTag, server)
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a painting variant"
            );
        }
    }

    @Override
    public BinaryTag encode(PaintingVariant value, JetMinecraftServer server) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putInt(WIDTH_FIELD, value.width())
                .putInt(HEIGHT_FIELD, value.height())
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.asset(), server));

        Component title = value.title();
        if (title != null) {
            builder.put(TITLE_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(title, server));
        }

        Component author = value.author();
        if (author != null) {
            builder.put(AUTHOR_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(author, server));
        }

        return builder.build();
    }
}