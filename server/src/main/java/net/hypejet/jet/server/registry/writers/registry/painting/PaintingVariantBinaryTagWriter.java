package net.hypejet.jet.server.registry.writers.registry.painting;

import net.hypejet.jet.data.model.api.registries.painting.PaintingVariant;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain PaintingVariant a painting variant} into
 * {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see PaintingVariant
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class PaintingVariantBinaryTagWriter implements Writer<PaintingVariant, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain PaintingVariantBinaryTagWriter painting variant binary tag writer}.
     *
     * @since 1.0
     */
    public static final PaintingVariantBinaryTagWriter INSTANCE = new PaintingVariantBinaryTagWriter();

    private static final String ASSET_ID_FIELD = "asset_id";
    private static final String HEIGHT_FIELD = "height";
    private static final String WIDTH_FIELD = "width";

    private PaintingVariantBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull PaintingVariant object) {
        return CompoundBinaryTag.builder()
                .put(ASSET_ID_FIELD, PackedKeyBinaryTagWriter.INSTANCE.write(object.asset()))
                .putInt(HEIGHT_FIELD, object.height())
                .putInt(WIDTH_FIELD, object.width())
                .build();
    }
}