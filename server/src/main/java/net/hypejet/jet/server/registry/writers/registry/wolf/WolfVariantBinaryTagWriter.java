package net.hypejet.jet.server.registry.writers.registry.wolf;

import net.hypejet.jet.data.model.api.registries.wolf.WolfVariant;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain WolfVariant a wolf variant} into
 * {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see WolfVariant
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class WolfVariantBinaryTagWriter implements Writer<WolfVariant, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain WolfVariantBinaryTagWriter wold varioant binary tag writer}.
     *
     * @since 1.0
     */
    public static final WolfVariantBinaryTagWriter INSTANCE = new WolfVariantBinaryTagWriter();

    private static final String WILD_TEXTURE_FIELD = "wild_texture";
    private static final String TAME_TEXTURE_FIELD = "tame_texture";
    private static final String ANGRY_TEXTURE_FIELD = "angry_texture";
    private static final String BIOMES_FIELD = "biomes";

    private WolfVariantBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull WolfVariant object) {
        return CompoundBinaryTag.builder()
                .put(WILD_TEXTURE_FIELD, PackedKeyBinaryTagWriter.INSTANCE.write(object.wildTexture()))
                .put(TAME_TEXTURE_FIELD, PackedKeyBinaryTagWriter.INSTANCE.write(object.tameTexture()))
                .put(ANGRY_TEXTURE_FIELD, PackedKeyBinaryTagWriter.INSTANCE.write(object.angryTexture()))
                .put(BIOMES_FIELD, WolfBiomesBinaryTagWriter.INSTANCE.write(object.biomes()))
                .build();
    }
}