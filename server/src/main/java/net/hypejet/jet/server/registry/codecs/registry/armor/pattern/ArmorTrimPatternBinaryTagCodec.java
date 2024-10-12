package net.hypejet.jet.server.registry.codecs.registry.armor.pattern;

import net.hypejet.jet.data.model.api.registry.registries.armor.pattern.ArmorTrimPattern;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.component.ComponentBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain ArmorTrimPattern armor trim pattern}.
 *
 * @since 1.0
 * @author Codestech
 * @see ArmorTrimPattern
 * @see BinaryTagCodec
 */
public final class ArmorTrimPatternBinaryTagCodec implements BinaryTagCodec<ArmorTrimPattern> {

    private static final ArmorTrimPatternBinaryTagCodec INSTANCE = new ArmorTrimPatternBinaryTagCodec();

    private static final String ASSET_ID_FIELD = "asset_id";
    private static final String TEMPLATE_ITEM_FIELD = "template_item";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String DECAL_FIELD = "decal";

    private ArmorTrimPatternBinaryTagCodec() {}

    @Override
    public @NonNull ArmorTrimPattern read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");

        PackedIdentifierBinaryTagCodec identifierCodec = PackedIdentifierBinaryTagCodec.instance();
        return new ArmorTrimPattern(BinaryTagUtil.read(ASSET_ID_FIELD, compound, identifierCodec),
                BinaryTagUtil.read(TEMPLATE_ITEM_FIELD, compound, identifierCodec),
                BinaryTagUtil.read(DESCRIPTION_FIELD, compound, ComponentBinaryTagCodec.instance()),
                compound.getBoolean(DECAL_FIELD));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull ArmorTrimPattern object) {
        return CompoundBinaryTag.builder()
                .put(ASSET_ID_FIELD, PackedIdentifierBinaryTagCodec.instance().write(object.asset()))
                .put(TEMPLATE_ITEM_FIELD, PackedIdentifierBinaryTagCodec.instance().write(object.templateItem()))
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.instance().write(object.description()))
                .putBoolean(DECAL_FIELD, object.decal())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain ArmorTrimPatternBinaryTagCodec armor trim pattern binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ArmorTrimPatternBinaryTagCodec instance() {
        return INSTANCE;
    }
}