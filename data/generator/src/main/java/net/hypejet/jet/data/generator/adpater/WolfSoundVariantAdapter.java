package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain WolfSoundVariant wolf sound variants} to a Jet data equivalent.
 *
 * @since 1.0
 * @see WolfSoundVariant
 */
public final class WolfSoundVariantAdapter {

    private WolfSoundVariantAdapter() {}

    /**
     * Converts the specified {@linkplain WolfSoundVariant wolf sound variant} to a Jet data equivalent.
     *
     * @param variant the wolf sound variant to convert
     * @return the converted wolf sound variant
     * @since 1.0
     */
    public static @NonNull JsonWolfSoundVariant convert(@NonNull WolfSoundVariant variant) {
        return new JsonWolfSoundVariant(
                HolderAdapter.convertSoundEventHolder(variant.ambientSound()),
                HolderAdapter.convertSoundEventHolder(variant.deathSound()),
                HolderAdapter.convertSoundEventHolder(variant.growlSound()),
                HolderAdapter.convertSoundEventHolder(variant.hurtSound()),
                HolderAdapter.convertSoundEventHolder(variant.pantSound()),
                HolderAdapter.convertSoundEventHolder(variant.whineSound())
        );
    }
}