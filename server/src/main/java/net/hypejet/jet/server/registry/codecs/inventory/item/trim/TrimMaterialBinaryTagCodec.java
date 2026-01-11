package net.hypejet.jet.server.registry.codecs.inventory.item.trim;

import net.hypejet.jet.inventory.item.trim.MaterialAssetGroup;
import net.hypejet.jet.inventory.item.trim.TrimMaterial;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.ComponentBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.MapBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain TrimMaterial trim materials}.
 *
 * @since 1.0
 * @see TrimMaterial
 * @see BinaryTagCodec
 */
@NullMarked
public final class TrimMaterialBinaryTagCodec implements BinaryTagCodec<TrimMaterial> {

    private static final String BASE_ASSET_FIELD = "asset_name";
    private static final String OVERRIDES_FIELD = "override_armor_assets";
    private static final String DESCRIPTION_FIELD = "description";

    private static final MapBinaryTagCodec<Key, MaterialAssetGroup.Asset> OVERRIDES_CODEC = new MapBinaryTagCodec<>(
            Key::asString,
            Key::key,
            AssetBinaryTagCodec.INSTANCE
    );

    /**
     * An instance of the {@linkplain TrimMaterialBinaryTagCodec trim-material binary tag codec}.
     *
     * @since 1.0
     * @see TrimMaterialBinaryTagCodec
     */
    public static final TrimMaterialBinaryTagCodec INSTANCE = new TrimMaterialBinaryTagCodec();

    private TrimMaterialBinaryTagCodec() {}

    @Override
    public TrimMaterial decode(BinaryTag binaryTag) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            BinaryTag overridesTag = compound.get(OVERRIDES_FIELD);
            return new TrimMaterial(
                    new MaterialAssetGroup(
                            AssetBinaryTagCodec.INSTANCE.decode(requiredTag(BASE_ASSET_FIELD, compound)),
                            overridesTag == null ? Map.of() : OVERRIDES_CODEC.decode(overridesTag)
                    ),
                    ComponentBinaryTagCodec.INSTANCE.decode(requiredTag(DESCRIPTION_FIELD, compound))
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a trim material"
            );
        }
    }

    @Override
    public BinaryTag encode(TrimMaterial value) {
        MaterialAssetGroup assets = value.assets();
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(BASE_ASSET_FIELD, AssetBinaryTagCodec.INSTANCE.encode(assets.baseAsset()))
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(value.description()));

        Map<Key, MaterialAssetGroup.Asset> overrides = assets.overrides();
        if (!overrides.isEmpty()) {
            builder.put(OVERRIDES_FIELD, OVERRIDES_CODEC.encode(overrides));
        }

        return builder.build();
    }

    /**
     * A {@linkplain BinaryTagCodec binary tag codec}
     * of {@linkplain MaterialAssetGroup.Asset material asset group asset}.
     *
     * @since 1.0
     * @see MaterialAssetGroup.Asset
     * @see BinaryTagCodec
     */
    private static final class AssetBinaryTagCodec implements BinaryTagCodec<MaterialAssetGroup.Asset> {
        /**
         * An instance of the {@linkplain AssetBinaryTagCodec asset binary tag codec}.
         *
         * @since 1.0
         */
        private static final AssetBinaryTagCodec INSTANCE = new AssetBinaryTagCodec();

        private AssetBinaryTagCodec() {}

        @Override
        public MaterialAssetGroup.Asset decode(BinaryTag binaryTag) {
            if (binaryTag instanceof StringBinaryTag tag) {
                return new MaterialAssetGroup.Asset(tag.value());
            } else {
                throw new IllegalArgumentException("The encoded tag must be of string type to decode it to a asset");
            }
        }

        @Override
        public BinaryTag encode(MaterialAssetGroup.Asset value) {
            return StringBinaryTagCodec.INSTANCE.encode(value.value());
        }
    }
}