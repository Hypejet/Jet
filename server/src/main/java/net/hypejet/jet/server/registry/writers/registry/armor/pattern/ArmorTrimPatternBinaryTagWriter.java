package net.hypejet.jet.server.registry.writers.registry.armor.pattern;

import net.hypejet.jet.data.model.api.registries.armor.pattern.ArmorTrimPattern;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.component.ComponentBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain ArmorTrimPattern an armor trim pattern}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see ArmorTrimPattern
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class ArmorTrimPatternBinaryTagWriter implements Writer<ArmorTrimPattern, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain ArmorTrimPatternBinaryTagWriter armor trim pattern binary tag writer}.
     *
     * @since 1.0
     */
    public static final ArmorTrimPatternBinaryTagWriter INSTANCE = new ArmorTrimPatternBinaryTagWriter();

    private static final String ASSET_ID_FIELD = "asset_id";
    private static final String TEMPLATE_ITEM_FIELD = "template_item";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String DECAL_FIELD = "decal";

    private ArmorTrimPatternBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull ArmorTrimPattern object) {
        return CompoundBinaryTag.builder()
                .put(ASSET_ID_FIELD, PackedKeyBinaryTagWriter.INSTANCE.write(object.asset()))
                .put(TEMPLATE_ITEM_FIELD, PackedKeyBinaryTagWriter.INSTANCE.write(object.templateItem()))
                .put(DESCRIPTION_FIELD, ComponentBinaryTagWriter.INSTANCE.write(object.description()))
                .putBoolean(DECAL_FIELD, object.decal())
                .build();
    }
}