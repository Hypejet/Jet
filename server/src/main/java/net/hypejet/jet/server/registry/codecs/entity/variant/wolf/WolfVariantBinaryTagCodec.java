package net.hypejet.jet.server.registry.codecs.entity.variant.wolf;

import net.hypejet.jet.entity.variant.wolf.WolfVariant;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain WolfVariant wolf variants}.
 *
 * @since 1.0
 * @see WolfVariant
 * @see BinaryTagCodec
 */
public final class WolfVariantBinaryTagCodec implements BinaryTagCodec<WolfVariant> {

    private static final String ASSETS_FIELD = "assets";

    /**
     * An instance of the {@linkplain WolfVariantBinaryTagCodec wolf-variant binary tag codec}.
     *
     * @since 1.0
     */
    public static final WolfVariantBinaryTagCodec INSTANCE = new WolfVariantBinaryTagCodec();

    private WolfVariantBinaryTagCodec() {}

    @Override
    public @NotNull WolfVariant decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new WolfVariant(AssetInfoBinaryTagCodec.INSTANCE.decode(requiredTag(ASSETS_FIELD, compound)));
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a wolf variant"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull WolfVariant decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(ASSETS_FIELD, AssetInfoBinaryTagCodec.INSTANCE.encode(decoded.assetInfo()))
                .build();
    }

    /**
     * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain WolfVariant.AssetInfo asset infos}.
     *
     * @since 1.0
     * @see WolfVariant.AssetInfo
     * @see BinaryTagCodec
     */
    private static final class AssetInfoBinaryTagCodec implements BinaryTagCodec<WolfVariant.AssetInfo> {

        private static final String WILD_ASSET_FIELD = "wild";
        private static final String TAME_ASSET_FIELD = "tame";
        private static final String ANGRY_ASSET_FIELD = "angry";

        /**
         * An instance of the {@linkplain AssetInfoBinaryTagCodec asset-info binary tag codec}.
         *
         * @since 1.0
         */
        private static final AssetInfoBinaryTagCodec INSTANCE = new AssetInfoBinaryTagCodec();

        private AssetInfoBinaryTagCodec() {}

        @Override
        public @NotNull WolfVariant.AssetInfo decode(@NotNull BinaryTag encoded) throws Exception {
            if (encoded instanceof CompoundBinaryTag compound) {
                return new WolfVariant.AssetInfo(
                        KeyBinaryTagCodec.INSTANCE.decode(requiredTag(WILD_ASSET_FIELD, compound)),
                        KeyBinaryTagCodec.INSTANCE.decode(requiredTag(TAME_ASSET_FIELD, compound)),
                        KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ANGRY_ASSET_FIELD, compound))
                );
            } else {
                throw new IllegalArgumentException(
                        "The encoded tag must be of compound type to decode it to asset info"
                );
            }
        }

        @Override
        public @NotNull BinaryTag encode(WolfVariant.@NotNull AssetInfo decoded) throws Exception {
            return CompoundBinaryTag.builder()
                    .put(WILD_ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(decoded.wildAsset()))
                    .put(TAME_ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(decoded.tameAsset()))
                    .put(ANGRY_ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(decoded.angryAsset()))
                    .build();
        }
    }
}