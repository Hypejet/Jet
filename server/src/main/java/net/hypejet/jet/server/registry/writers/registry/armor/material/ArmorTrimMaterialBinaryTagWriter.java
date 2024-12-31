package net.hypejet.jet.server.registry.writers.registry.armor.material;

import net.hypejet.jet.data.model.api.registries.armor.material.ArmorTrimMaterial;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.component.ComponentBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain ArmorTrimMaterial an armor trim material}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see ArmorTrimMaterial
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class ArmorTrimMaterialBinaryTagWriter implements Writer<ArmorTrimMaterial, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain ArmorTrimMaterialBinaryTagWriter an armor trim material binary tag writer}.
     *
     * @since 1.0
     */
    public static final ArmorTrimMaterialBinaryTagWriter INSTANCE = new ArmorTrimMaterialBinaryTagWriter();

    private static final String ASSET_NAME_FIELD = "asset_name";
    private static final String INGREDIENT_FIELD = "ingredient";
    private static final String ITEM_MODEL_INDEX_FIELD = "item_model_index";
    private static final String OVERRIDE_ARMOR_MATERIALS_FIELD = "override_armor_materials";
    private static final String DESCRIPTION_FIELD = "description";

    private ArmorTrimMaterialBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull ArmorTrimMaterial object) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(ASSET_NAME_FIELD, object.asset().value())
                .put(INGREDIENT_FIELD, PackedKeyBinaryTagWriter.INSTANCE.write(object.ingredient()))
                .putFloat(ITEM_MODEL_INDEX_FIELD, object.itemModelIndex())
                .put(DESCRIPTION_FIELD, ComponentBinaryTagWriter.INSTANCE.write(object.description()));

        Map<Key, Key> overrideArmorMaterials = object.overrideArmorMaterials();
        if (overrideArmorMaterials != null) {
            CompoundBinaryTag.Builder overrideArmorMaterialsBuilder = CompoundBinaryTag.builder();
            for (Map.Entry<Key, Key> entry : overrideArmorMaterials.entrySet()) {
                String key = entry.getKey().asString();
                BinaryTag value = PackedKeyBinaryTagWriter.INSTANCE.write(entry.getValue());
                overrideArmorMaterialsBuilder.put(key, value);
            }

            builder.put(OVERRIDE_ARMOR_MATERIALS_FIELD, overrideArmorMaterialsBuilder.build());
        }

        return builder.build();
    }
}