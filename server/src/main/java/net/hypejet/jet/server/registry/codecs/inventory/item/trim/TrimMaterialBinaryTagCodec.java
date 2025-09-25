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
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain TrimMaterial trim materials}.
 *
 * @since 1.0
 * @see TrimMaterial
 * @see BinaryTagCodec
 */
public final class TrimMaterialBinaryTagCodec implements BinaryTagCodec<TrimMaterial> {

    private static final String BASE_ASSET_FIELD = "asset_name";
    private static final String OVERRIDES_FIELD = "override_armor_assets";
    private static final String DESCRIPTION_FIELD = "description";

    private static final MapBinaryTagCodec<Key, MaterialAssetGroup.Asset> OVERRIDES_CODEC = new MapBinaryTagCodec<>(
            Codec.codec(Key::key, Key::asString),
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
    public @NotNull TrimMaterial decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
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
    public @NotNull BinaryTag encode(@NotNull TrimMaterial decoded) throws Exception {
        MaterialAssetGroup assets = decoded.assets();
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(BASE_ASSET_FIELD, AssetBinaryTagCodec.INSTANCE.encode(assets.baseAsset()))
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(decoded.description()));

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
        public @NotNull MaterialAssetGroup.Asset decode(@NotNull BinaryTag encoded) {
            if (encoded instanceof StringBinaryTag tag) {
                return new MaterialAssetGroup.Asset(tag.value());
            } else {
                throw new IllegalArgumentException("The encoded tag must be of string type to decode it to a asset");
            }
        }

        @Override
        public @NotNull BinaryTag encode(MaterialAssetGroup.@NotNull Asset decoded) {
            return StringBinaryTagCodec.INSTANCE.encode(decoded.value());
        }
    }
}