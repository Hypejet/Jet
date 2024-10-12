package net.hypejet.jet.server.registry.codecs.registry.armor.material;

import net.hypejet.jet.data.model.api.registry.registries.armor.material.ArmorTrimMaterial;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.component.ComponentBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain ArmorTrimMaterial armor trim material}.
 *
 * @since 1.0
 * @author Codestech
 * @see ArmorTrimMaterial
 * @see BinaryTagCodec
 */
public final class ArmorTrimMaterialBinaryTagCodec implements BinaryTagCodec<ArmorTrimMaterial> {

    private static final ArmorTrimMaterialBinaryTagCodec INSTANCE = new ArmorTrimMaterialBinaryTagCodec();

    private static final String ASSET_NAME_FIELD = "asset_name";
    private static final String INGREDIENT_FIELD = "ingredient";
    private static final String ITEM_MODEL_INDEX_FIELD = "item_model_index";
    private static final String OVERRIDE_ARMOR_MATERIALS_FIELD = "override_armor_materials";
    private static final String DESCRIPTION_FIELD = "description";

    private ArmorTrimMaterialBinaryTagCodec() {}

    @Override
    public @NonNull ArmorTrimMaterial read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be compound binary tag");

        Key asset = BinaryTagUtil.read(ASSET_NAME_FIELD, compound, PackedIdentifierBinaryTagCodec.instance());
        Key ingredient = BinaryTagUtil.read(INGREDIENT_FIELD, compound, PackedIdentifierBinaryTagCodec.instance());

        float itemModelIndex = compound.getFloat(ITEM_MODEL_INDEX_FIELD);

        CompoundBinaryTag overrideArmorMaterialsBinaryTag = compound.getCompound(OVERRIDE_ARMOR_MATERIALS_FIELD);
        Component description = BinaryTagUtil.read(DESCRIPTION_FIELD, compound, ComponentBinaryTagCodec.instance());

        Map<Key, Key> overrideArmorMaterials = new HashMap<>();
        for (Map.Entry<String, ? extends BinaryTag> entry : overrideArmorMaterialsBinaryTag) {
            Key value = PackedIdentifierBinaryTagCodec.instance().read(entry.getValue());
            overrideArmorMaterials.put(Key.key(entry.getKey()), value);
        }

        return new ArmorTrimMaterial(asset, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    @Override
    public @NonNull BinaryTag write(@NonNull ArmorTrimMaterial object) {
        CompoundBinaryTag.Builder overrideArmorMaterialsBuilder = CompoundBinaryTag.builder();
        for (Map.Entry<Key, Key> entry : object.overrideArmorMaterials().entrySet()) {
            String key = entry.getKey().asString();
            BinaryTag value = PackedIdentifierBinaryTagCodec.instance().write(entry.getValue());
            overrideArmorMaterialsBuilder.put(key, value);
        }

        return CompoundBinaryTag.builder()
                .putString(ASSET_NAME_FIELD, object.asset().value())
                .put(INGREDIENT_FIELD, PackedIdentifierBinaryTagCodec.instance().write(object.ingredient()))
                .putFloat(ITEM_MODEL_INDEX_FIELD, object.itemModelIndex())
                .put(OVERRIDE_ARMOR_MATERIALS_FIELD, overrideArmorMaterialsBuilder.build())
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.instance().write(object.description()))
                .build();
    }

    /**
     * Gets an instance of the {@linkplain ArmorTrimMaterialBinaryTagCodec armor trim material binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ArmorTrimMaterialBinaryTagCodec instance() {
        return INSTANCE;
    }
}