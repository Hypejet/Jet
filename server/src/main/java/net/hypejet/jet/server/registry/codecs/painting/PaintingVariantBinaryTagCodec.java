package net.hypejet.jet.server.registry.codecs.painting;

import net.hypejet.jet.data.model.registry.registries.painting.PaintingVariant;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain PaintingVariant painting variant}.
 *
 * @since 1.0
 * @author Codestech
 * @see PaintingVariant
 * @see BinaryTagCodec
 */
public final class PaintingVariantBinaryTagCodec implements BinaryTagCodec<PaintingVariant> {

    private static final PaintingVariantBinaryTagCodec INSTANCE = new PaintingVariantBinaryTagCodec();

    private static final String ASSET_FIELD = "asset";
    private static final String HEIGHT_FIELD = "height";
    private static final String WIDTH_FIELD = "width";

    private PaintingVariantBinaryTagCodec() {}

    @Override
    public @NonNull PaintingVariant read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified is not a compound binary tag");

        Key asset = BinaryTagUtil.read(ASSET_FIELD, compound, PackedIdentifierBinaryTagCodec.instance());
        if (asset == null)
            throw new IllegalArgumentException("The asset field was not specified");

        return new PaintingVariant(asset, compound.getInt(HEIGHT_FIELD), compound.getInt(WIDTH_FIELD));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull PaintingVariant object) {
        return CompoundBinaryTag.builder()
                .put(ASSET_FIELD, PackedIdentifierBinaryTagCodec.instance().write(object.asset()))
                .putInt(HEIGHT_FIELD, object.height())
                .putInt(WIDTH_FIELD, object.width())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain PaintingVariantBinaryTagCodec painting variant binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull PaintingVariantBinaryTagCodec instance() {
        return INSTANCE;
    }
}