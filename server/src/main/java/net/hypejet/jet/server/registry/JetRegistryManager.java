package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.codecs.JetDataJson;
import net.hypejet.jet.data.generated.api.ArmorTrimMaterials;
import net.hypejet.jet.data.generated.api.ArmorTrimPatterns;
import net.hypejet.jet.data.generated.api.BannerPatterns;
import net.hypejet.jet.data.generated.api.Biomes;
import net.hypejet.jet.data.generated.api.ChatTypes;
import net.hypejet.jet.data.generated.api.DamageTypes;
import net.hypejet.jet.data.generated.api.DimensionTypes;
import net.hypejet.jet.data.generated.api.PaintingVariants;
import net.hypejet.jet.data.generated.api.WolfVariants;
import net.hypejet.jet.data.generated.server.Blocks;
import net.hypejet.jet.data.generated.server.FeaturePacks;
import net.hypejet.jet.data.model.api.registry.DataRegistryEntry;
import net.hypejet.jet.data.model.api.registry.registries.armor.material.ArmorTrimMaterial;
import net.hypejet.jet.data.model.api.registry.registries.armor.pattern.ArmorTrimPattern;
import net.hypejet.jet.data.model.api.registry.registries.banner.BannerPattern;
import net.hypejet.jet.data.model.api.registry.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registry.registries.chat.ChatType;
import net.hypejet.jet.data.model.api.registry.registries.damage.DamageType;
import net.hypejet.jet.data.model.api.registry.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.registry.registries.painting.PaintingVariant;
import net.hypejet.jet.data.model.api.registry.registries.wolf.WolfVariant;
import net.hypejet.jet.data.model.server.registry.registries.block.Block;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.registry.RegistryManager;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.codecs.registry.armor.material.ArmorTrimMaterialBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.armor.pattern.ArmorTrimPatternBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.banner.BannerPatternBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.biome.BiomeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.chat.ChatTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.damage.DamageTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.dimension.DimensionTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.painting.PaintingVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.wolf.WolfVariantBinaryTagCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents an implementation of the {@linkplain RegistryManager registry manager}.
 *
 * @since 1.0
 * @author Codestech
 * @see RegistryManager
 */
public final class JetRegistryManager implements RegistryManager {

    private final Map<Key, JetMinecraftRegistry<?>> registries;
    private final Set<FeaturePack> enabledFeaturePacks;

    /**
     * Constructs the {@linkplain JetRegistryManager registry manager}.
     *
     * @param server a server, for which the registries should be managed
     * @since 1.0
     */
    public JetRegistryManager(@NonNull JetMinecraftServer server) {
        List<DataRegistryEntry<?>> dataPackEntries = JetMinecraftRegistry.entries(JetDataJson.createPlainGson(),
                FeaturePacks.RESOURCE_FILE_NAME);

        Logger logger = LoggerFactory.getLogger(JetRegistryManager.class);
        Set<FeaturePack> enabledFeaturePacks = new HashSet<>();

        for (Key packKey : server.configuration().enabledFeaturePacks()) {
            FeaturePack featurePack = null;

            for (DataRegistryEntry<?> featurePackEntry : dataPackEntries) {
                if (!featurePackEntry.key().equals(packKey)) continue;

                Object value = featurePackEntry.value();
                if (!FeaturePack.class.isAssignableFrom(value.getClass()))
                    throw new IllegalArgumentException("A feature pack clas is not assignable from the entry value");
                featurePack = (FeaturePack) value;
            }

            if (featurePack == null) {
                logger.warn("Could not find a feature pack with key of \"{}\", skipping...", packKey);
                continue;
            }

            enabledFeaturePacks.add(featurePack);
            logger.info("Enabled feature pack with key of \"{}\"", packKey);
        }

        this.enabledFeaturePacks = Set.copyOf(enabledFeaturePacks);

        Set<JetMinecraftRegistry<?>> registrySet = Set.of(
                JetMinecraftRegistry.create(Key.key("block"), Block.class, server, this.enabledFeaturePacks,
                        JetDataJson.createBlocksGson(), Blocks.RESOURCE_FILE_NAME), // TODO: Filter blocks based on required feature flags
                JetSerializableMinecraftRegistry.create(Key.key("dimension_type"), DimensionType.class, server,
                        DimensionTypeBinaryTagCodec.instance(), this.enabledFeaturePacks,
                        JetDataJson.createDimensionTypesGson(), DimensionTypes.RESOURCE_FILE_NAME),
                JetSerializableMinecraftRegistry.create(Key.key("chat_type"),
                        ChatType.class, server, ChatTypeBinaryTagCodec.instance(), this.enabledFeaturePacks,
                        JetDataJson.createChatTypesGson(), ChatTypes.RESOURCE_FILE_NAME),
                JetSerializableMinecraftRegistry.create(Key.key("damage_type"),
                        DamageType.class, server, DamageTypeBinaryTagCodec.instance(), this.enabledFeaturePacks,
                        JetDataJson.createDamageTypesGson(), DamageTypes.RESOURCE_FILE_NAME),
                JetSerializableMinecraftRegistry.create(Key.key("banner_pattern"),
                        BannerPattern.class, server, BannerPatternBinaryTagCodec.instance(), this.enabledFeaturePacks,
                        JetDataJson.createBannerPatternsGson(), BannerPatterns.RESOURCE_FILE_NAME),
                JetSerializableMinecraftRegistry.create(Key.key("wolf_variant"),
                        WolfVariant.class, server, WolfVariantBinaryTagCodec.instance(), this.enabledFeaturePacks,
                        JetDataJson.createWolfVariantsGson(), WolfVariants.RESOURCE_FILE_NAME),
                JetSerializableMinecraftRegistry.create(Key.key("worldgen/biome"), Biome.class, server,
                        BiomeBinaryTagCodec.instance(), this.enabledFeaturePacks, JetDataJson.createBiomesGson(),
                        Biomes.RESOURCE_FILE_NAME),
                JetSerializableMinecraftRegistry.create(Key.key("painting_variant"),
                        PaintingVariant.class, server, PaintingVariantBinaryTagCodec.instance(),
                        this.enabledFeaturePacks, JetDataJson.createPaintingVariantsGson(),
                        PaintingVariants.RESOURCE_FILE_NAME),
                JetSerializableMinecraftRegistry.create(Key.key("trim_material"),
                        ArmorTrimMaterial.class, server, ArmorTrimMaterialBinaryTagCodec.instance(),
                        this.enabledFeaturePacks, JetDataJson.createTrimMaterialsGson(),
                        ArmorTrimMaterials.RESOURCE_FILE_NAME),
                JetSerializableMinecraftRegistry.create(Key.key("trim_pattern"),
                        ArmorTrimPattern.class, server, ArmorTrimPatternBinaryTagCodec.instance(),
                        this.enabledFeaturePacks, JetDataJson.createTrimPatternsGson(),
                        ArmorTrimPatterns.RESOURCE_FILE_NAME)
        );

        Map<Key, JetMinecraftRegistry<?>> registries = new HashMap<>();
        registrySet.forEach(registry -> registries.put(registry.registryKey(), registry));
        this.registries = Map.copyOf(registries);
    }

    @Override
    public @Nullable JetMinecraftRegistry<?> getRegistry(@NonNull Key key) {
        return this.registries.get(key);
    }

    @Override
    public @NonNull Map<Key, JetMinecraftRegistry<?>> getRegistries() {
        return Map.copyOf(this.registries);
    }

    /**
     * Gets a {@linkplain Set set} of enabled {@linkplain FeaturePack feature packs}.
     *
     * @return the set
     * @since 1.0
     */
    public @NonNull Set<FeaturePack> enabledDataPacks() {
        return this.enabledFeaturePacks;
    }
}