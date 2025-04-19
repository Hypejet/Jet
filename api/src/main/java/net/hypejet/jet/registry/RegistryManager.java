package net.hypejet.jet.registry;

import net.hypejet.jet.data.model.api.registries.armor.material.ArmorTrimMaterial;
import net.hypejet.jet.data.model.api.registries.armor.pattern.ArmorTrimPattern;
import net.hypejet.jet.data.model.api.registries.banner.BannerPattern;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.chat.ChatType;
import net.hypejet.jet.data.model.api.registries.damage.DamageType;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.registries.painting.PaintingVariant;
import net.hypejet.jet.data.model.api.registries.wolf.WolfVariant;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a manager of {@linkplain MinecraftRegistry Minecraft registries}.
 *
 * @since 1.0
 * @see MinecraftRegistry
 */
public interface RegistryManager {
    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain DimensionType dimension types}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<DimensionType> dimensionTypeRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain ChatType chat types}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<ChatType> chatTypeRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain DamageType damage types}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<DamageType> damageTypeRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain BannerPattern banner patterns}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<BannerPattern> bannerPatternRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain WolfVariant wolf variants}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<WolfVariant> wolfVariantRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain Biome biomes}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<Biome> biomeRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain PaintingVariant painting variants}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<PaintingVariant> paintingVariantRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain ArmorTrimMaterial armor trim materials}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<ArmorTrimMaterial> trimMaterialRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of {@linkplain ArmorTrimPattern armor trim patterns}.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<ArmorTrimPattern> trimPatternRegistry();

    /**
     * Gets {@linkplain MinecraftRegistry a registry} of types of Minecraft blocks.
     *
     * @return the registry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<?> blockTypeRegistry();
}