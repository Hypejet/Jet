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
import net.hypejet.jet.data.generated.server.DataPacks;
import net.hypejet.jet.data.model.pack.DataPack;
import net.hypejet.jet.data.model.registry.DataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.armor.material.ArmorTrimMaterial;
import net.hypejet.jet.data.model.registry.registries.armor.material.ArmorTrimMaterialDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.armor.pattern.ArmorTrimPattern;
import net.hypejet.jet.data.model.registry.registries.armor.pattern.ArmorTrimPatternDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.banner.BannerPattern;
import net.hypejet.jet.data.model.registry.registries.banner.BannerPatternDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.biome.Biome;
import net.hypejet.jet.data.model.registry.registries.biome.BiomeDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.chat.ChatType;
import net.hypejet.jet.data.model.registry.registries.chat.ChatTypeDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.damage.DamageType;
import net.hypejet.jet.data.model.registry.registries.damage.DamageTypeDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.datapack.DataPackDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.registry.registries.dimension.DimensionTypeDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.painting.PaintingVariant;
import net.hypejet.jet.data.model.registry.registries.painting.PaintingVariantDataRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.wolf.WolfVariant;
import net.hypejet.jet.data.model.registry.registries.wolf.WolfVariantDataRegistryEntry;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
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

    private final Map<Key, JetSerializableMinecraftRegistry<?>> registries;
    private final Set<DataPack> enabledDataPacks;

    /**
     * Constructs the {@linkplain JetRegistryManager registry manager}.
     *
     * @param server a server, for which the registries should be managed
     * @since 1.0
     */
    public JetRegistryManager(@NonNull JetMinecraftServer server) {
        Set<DataRegistryEntry<DataPack>> dataPackEntries = getEntries(
                DataPackDataRegistryEntry.class,
                DataPacks.SPEC_JSON_FILE_NAME
        );

        Logger logger = LoggerFactory.getLogger(JetRegistryManager.class);
        Set<DataPack> enabledDataPacks = new HashSet<>();

        for (Key packKey : server.configuration().enabledPacks()) {
            DataRegistryEntry<DataPack> entry = null;

            for (DataRegistryEntry<DataPack> dataPackEntry : dataPackEntries) {
                if (!dataPackEntry.key().equals(packKey)) continue;
                entry = dataPackEntry;
            }

            if (entry == null) {
                logger.warn("Could not find a data pack with key of \"{}\", skipping...", packKey);
                continue;
            }

            enabledDataPacks.add(entry.value());
            logger.info("Enabled data pack with key of \"{}\"", packKey);
        }

        this.enabledDataPacks = Set.copyOf(enabledDataPacks);

        Set<JetSerializableMinecraftRegistry<?>> registrySet = Set.of(
                new JetSerializableMinecraftRegistry<>(Key.key("worldgen/biome"),
                        Biome.class, server, BiomeBinaryTagCodec.instance(),
                        getEntries(BiomeDataRegistryEntry.class, Biomes.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks),
                new JetSerializableMinecraftRegistry<>(Key.key("dimension_type"),
                        DimensionType.class, server, DimensionTypeBinaryTagCodec.instance(),
                        getEntries(DimensionTypeDataRegistryEntry.class, DimensionTypes.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks),
                new JetSerializableMinecraftRegistry<>(Key.key("painting_variant"),
                        PaintingVariant.class, server, PaintingVariantBinaryTagCodec.instance(),
                        getEntries(PaintingVariantDataRegistryEntry.class, PaintingVariants.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks),
                new JetSerializableMinecraftRegistry<>(Key.key("banner_pattern"),
                        BannerPattern.class, server, BannerPatternBinaryTagCodec.instance(),
                        getEntries(BannerPatternDataRegistryEntry.class, BannerPatterns.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks),
                new JetSerializableMinecraftRegistry<>(Key.key("trim_material"),
                        ArmorTrimMaterial.class, server, ArmorTrimMaterialBinaryTagCodec.instance(),
                        getEntries(ArmorTrimMaterialDataRegistryEntry.class, ArmorTrimMaterials.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks),
                new JetSerializableMinecraftRegistry<>(Key.key("trim_pattern"),
                        ArmorTrimPattern.class, server, ArmorTrimPatternBinaryTagCodec.instance(),
                        getEntries(ArmorTrimPatternDataRegistryEntry.class, ArmorTrimPatterns.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks),
                new JetSerializableMinecraftRegistry<>(Key.key("chat_type"),
                        ChatType.class, server, ChatTypeBinaryTagCodec.instance(),
                        getEntries(ChatTypeDataRegistryEntry.class, ChatTypes.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks),
                new JetSerializableMinecraftRegistry<>(Key.key("damage_type"),
                        DamageType.class, server, DamageTypeBinaryTagCodec.instance(),
                        getEntries(DamageTypeDataRegistryEntry.class, DamageTypes.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks),
                new JetSerializableMinecraftRegistry<>(Key.key("wolf_variant"),
                        WolfVariant.class, server, WolfVariantBinaryTagCodec.instance(),
                        getEntries(WolfVariantDataRegistryEntry.class, WolfVariants.SPEC_JSON_FILE_NAME),
                        this.enabledDataPacks)
        );

        Map<Key, JetSerializableMinecraftRegistry<?>> registries = new HashMap<>();
        registrySet.forEach(registry -> registries.put(registry.registryIdentifier(), registry));
        this.registries = Map.copyOf(registries);
    }

    @Override
    public @Nullable JetSerializableMinecraftRegistry<?> getRegistry(@NonNull Key identifier) {
        return this.registries.get(identifier);
    }

    @Override
    public @NonNull Map<Key, JetSerializableMinecraftRegistry<?>> getRegistries() {
        return Map.copyOf(this.registries);
    }

    /**
     * Gets a {@linkplain Set set} of enabled {@linkplain DataPack data packs}.
     *
     * @return the set
     * @since 1.0
     */
    public @NonNull Set<DataPack> enabledDataPacks() {
        return this.enabledDataPacks;
    }

    private static <E> Set<DataRegistryEntry<E>> getEntries(@NonNull Class<? extends DataRegistryEntry<E>> entryClass,
                                                            @NonNull String jsonSpecFileName) {
        Set<DataRegistryEntry<E>> entries = new HashSet<>();
        InputStream stream = JetRegistryManager.class
                .getClassLoader()
                .getResourceAsStream(jsonSpecFileName);

        if (stream != null) {
            try {
                String json = new String(stream.readAllBytes());
                entries.addAll(JetDataJson.deserialize(json, entryClass));
                stream.close();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        return Set.copyOf(entries);
    }
}