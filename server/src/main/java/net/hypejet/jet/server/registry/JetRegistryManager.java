package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.codecs.JetDataJson;
import net.hypejet.jet.data.generated.ArmorTrimMaterials;
import net.hypejet.jet.data.generated.ArmorTrimPatterns;
import net.hypejet.jet.data.generated.BannerPatterns;
import net.hypejet.jet.data.generated.Biomes;
import net.hypejet.jet.data.generated.ChatTypes;
import net.hypejet.jet.data.generated.DamageTypes;
import net.hypejet.jet.data.generated.DimensionTypes;
import net.hypejet.jet.data.generated.PaintingVariants;
import net.hypejet.jet.data.generated.WolfVariants;
import net.hypejet.jet.data.model.registry.RegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.armor.material.ArmorTrimMaterial;
import net.hypejet.jet.data.model.registry.registries.armor.material.ArmorTrimMaterialRegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.armor.pattern.ArmorTrimPattern;
import net.hypejet.jet.data.model.registry.registries.armor.pattern.ArmorTrimPatternRegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.banner.BannerPattern;
import net.hypejet.jet.data.model.registry.registries.banner.BannerPatternRegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.biome.Biome;
import net.hypejet.jet.data.model.registry.registries.biome.BiomeRegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.chat.ChatType;
import net.hypejet.jet.data.model.registry.registries.chat.ChatTypeRegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.damage.DamageType;
import net.hypejet.jet.data.model.registry.registries.damage.DamageTypeRegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.registry.registries.dimension.DimensionTypeRegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.painting.PaintingVariant;
import net.hypejet.jet.data.model.registry.registries.painting.PaintingVariantRegistryEntryData;
import net.hypejet.jet.data.model.registry.registries.wolf.WolfVariant;
import net.hypejet.jet.data.model.registry.registries.wolf.WolfVariantRegistryEntryData;
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

    private final Map<Key, JetRegistry<?>> registries;

    /**
     * Constructs the {@linkplain JetRegistryManager registry manager}.
     *
     * @since 1.0
     */
    public JetRegistryManager(@NonNull JetMinecraftServer server) {
        Set<JetRegistry<?>> registrySet = Set.of(
                new JetRegistry<>(Key.key("worldgen/biome"),
                        Biome.class, server, BiomeBinaryTagCodec.instance(),
                        getEntries(BiomeRegistryEntryData.class, Biomes.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("dimension_type"),
                        DimensionType.class, server, DimensionTypeBinaryTagCodec.instance(),
                        getEntries(DimensionTypeRegistryEntryData.class, DimensionTypes.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("painting_variant"),
                        PaintingVariant.class, server, PaintingVariantBinaryTagCodec.instance(),
                        getEntries(PaintingVariantRegistryEntryData.class, PaintingVariants.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("banner_pattern"),
                        BannerPattern.class, server, BannerPatternBinaryTagCodec.instance(),
                        getEntries(BannerPatternRegistryEntryData.class, BannerPatterns.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("trim_material"),
                        ArmorTrimMaterial.class, server, ArmorTrimMaterialBinaryTagCodec.instance(),
                        getEntries(ArmorTrimMaterialRegistryEntryData.class, ArmorTrimMaterials.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("trim_pattern"),
                        ArmorTrimPattern.class, server, ArmorTrimPatternBinaryTagCodec.instance(),
                        getEntries(ArmorTrimPatternRegistryEntryData.class, ArmorTrimPatterns.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("chat_type"),
                        ChatType.class, server, ChatTypeBinaryTagCodec.instance(),
                        getEntries(ChatTypeRegistryEntryData.class, ChatTypes.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("damage_type"),
                        DamageType.class, server, DamageTypeBinaryTagCodec.instance(),
                        getEntries(DamageTypeRegistryEntryData.class, DamageTypes.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("wolf_variant"),
                        WolfVariant.class, server, WolfVariantBinaryTagCodec.instance(),
                        getEntries(WolfVariantRegistryEntryData.class, WolfVariants.SPEC_JSON_FILE_NAME))
        );

        Map<Key, JetRegistry<?>> registries = new HashMap<>();
        registrySet.forEach(registry -> registries.put(registry.registryIdentifier(), registry));
        this.registries = Map.copyOf(registries);
    }

    @Override
    public @Nullable JetRegistry<?> getRegistry(@NonNull Key identifier) {
        return this.registries.get(identifier);
    }

    @Override
    public @NonNull Map<Key, JetRegistry<?>> getRegistries() {
        return Map.copyOf(this.registries);
    }

    private static <E> Set<RegistryEntryData<E>> getEntries(@NonNull Class<? extends RegistryEntryData<E>> dataClass,
                                                            @NonNull String jsonSpecFileName) {
        Set<RegistryEntryData<E>> entryData = new HashSet<>();
        InputStream stream = JetRegistryManager.class
                .getClassLoader()
                .getResourceAsStream(jsonSpecFileName);

        if (stream != null) {
            try {
                String json = new String(stream.readAllBytes());
                entryData.addAll(JetDataJson.deserialize(json, dataClass));
                stream.close();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        return Set.copyOf(entryData);
    }
}