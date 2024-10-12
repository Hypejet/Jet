package net.hypejet.jet.server.registry.codecs.registry.wolf;

import net.hypejet.jet.data.model.api.registry.registries.wolf.WolfVariant;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * the {@linkplain WolfVariant wolf variant}.
 *
 * @since 1.0
 * @author Codestech
 * @see WolfVariant
 * @see BinaryTagCodec
 */
public final class WolfVariantBinaryTagCodec implements BinaryTagCodec<WolfVariant> {

    private static final String WILD_TEXTURE_FIELD = "wild_texture";
    private static final String TAME_TEXTURE_FIELD = "tame_texture";
    private static final String ANGRY_TEXTURE_FIELD = "angry_texture";
    private static final String BIOMES_FIELD = "biomes";

    private static final WolfVariantBinaryTagCodec INSTANCE = new WolfVariantBinaryTagCodec();

    private WolfVariantBinaryTagCodec() {}

    @Override
    public @NonNull WolfVariant read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");

        PackedIdentifierBinaryTagCodec identifierCodec = PackedIdentifierBinaryTagCodec.instance();
        return new WolfVariant(BinaryTagUtil.read(WILD_TEXTURE_FIELD, compound, identifierCodec),
                BinaryTagUtil.read(TAME_TEXTURE_FIELD, compound, identifierCodec),
                BinaryTagUtil.read(ANGRY_TEXTURE_FIELD, compound, identifierCodec),
                BinaryTagUtil.read(BIOMES_FIELD, compound, WolfBiomesBinaryTagCodec.instance()));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull WolfVariant object) {
        return CompoundBinaryTag.builder()
                .put(WILD_TEXTURE_FIELD, PackedIdentifierBinaryTagCodec.instance().write(object.wildTexture()))
                .put(TAME_TEXTURE_FIELD, PackedIdentifierBinaryTagCodec.instance().write(object.tameTexture()))
                .put(ANGRY_TEXTURE_FIELD, PackedIdentifierBinaryTagCodec.instance().write(object.angryTexture()))
                .put(BIOMES_FIELD, WolfBiomesBinaryTagCodec.instance().write(object.biomes()))
                .build();
    }

    /**
     * Gets an instance of the {@linkplain WolfVariantBinaryTagCodec wolf variant binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull WolfVariantBinaryTagCodec instance() {
        return INSTANCE;
    }
}