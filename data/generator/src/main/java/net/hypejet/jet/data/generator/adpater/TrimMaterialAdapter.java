package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.trim.material.JsonMaterialAssetGroup;
import net.hypejet.jet.data.json.model.trim.material.JsonTrimMaterial;
import net.kyori.adventure.key.Key;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents something converting {@linkplain TrimMaterial trim materials} to a Jet data equivalent.
 *
 * @since 1.0
 * @see TrimMaterial
 */
public final class TrimMaterialAdapter {

    private TrimMaterialAdapter() {}

    /**
     * Converts the specified {@linkplain TrimMaterial trim material} to a Jet data equivalent.
     *
     * @param material the trim material to convert
     * @return the converted trim material
     * @since 1.0
     */
    public static @NonNull JsonTrimMaterial convert(@NonNull TrimMaterial material) {
        return new JsonTrimMaterial(
                convertAssetGroup(material.assets()),
                ComponentAdapter.convert(material.description())
        );
    }

    private static @NonNull JsonMaterialAssetGroup convertAssetGroup(@NonNull MaterialAssetGroup group) {
        Map<Key, JsonMaterialAssetGroup.Asset> overrides = new HashMap<>();

        group.overrides().forEach((key, asset) -> overrides.put(
                KeyAdapter.convert(key.location()),
                convertAsset(asset))
        );

        return new JsonMaterialAssetGroup(convertAsset(group.base()), overrides);
    }

    private static JsonMaterialAssetGroup.@NonNull Asset convertAsset(MaterialAssetGroup.@NonNull AssetInfo asset) {
        return new JsonMaterialAssetGroup.Asset(asset.suffix());
    }
}