package net.hypejet.jet.server.registry;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.data.codecs.JetDataJson;
import net.hypejet.jet.data.generated.*;
import net.hypejet.jet.data.model.registry.RegistryEntry;
import net.hypejet.jet.data.model.registry.registries.armor.material.ArmorTrimMaterial;
import net.hypejet.jet.data.model.registry.registries.armor.material.ArmorTrimMaterialRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.armor.pattern.ArmorTrimPattern;
import net.hypejet.jet.data.model.registry.registries.armor.pattern.ArmorTrimPatternRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.banner.BannerPattern;
import net.hypejet.jet.data.model.registry.registries.banner.BannerPatternRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.biome.Biome;
import net.hypejet.jet.data.model.registry.registries.biome.BiomeRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.chat.ChatType;
import net.hypejet.jet.data.model.registry.registries.chat.ChatTypeRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.damage.DamageType;
import net.hypejet.jet.data.model.registry.registries.damage.DamageTypeRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.registry.registries.dimension.DimensionTypeRegistryEntry;
import net.hypejet.jet.data.model.registry.registries.painting.PaintingVariant;
import net.hypejet.jet.data.model.registry.registries.painting.PaintingVariantRegistryEntry;
import net.hypejet.jet.registry.RegistryManager;
import net.hypejet.jet.server.registry.codecs.registry.armor.material.ArmorTrimMaterialBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.armor.pattern.ArmorTrimPatternBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.banner.BannerPatternBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.biome.BiomeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.chat.ChatTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.damage.DamageTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.dimension.DimensionTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.painting.PaintingVariantBinaryTagCodec;
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
    public JetRegistryManager(@NonNull MinecraftServer server) {
        Set<JetRegistry<?>> registrySet;
        registrySet = Set.of(
                new JetRegistry<>(Key.key("worldgen/biome"),
                        Biome.class, server, BiomeBinaryTagCodec.instance(),
                        getEntries(BiomeRegistryEntry.class, Biomes.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("dimension_type"),
                        DimensionType.class, server, DimensionTypeBinaryTagCodec.instance(),
                        getEntries(DimensionTypeRegistryEntry.class, DimensionTypes.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("painting_variant"),
                        PaintingVariant.class, server, PaintingVariantBinaryTagCodec.instance(),
                        getEntries(PaintingVariantRegistryEntry.class, PaintingVariants.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("banner_pattern"),
                        BannerPattern.class, server, BannerPatternBinaryTagCodec.instance(),
                        getEntries(BannerPatternRegistryEntry.class, BannerPatterns.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("trim_material"),
                        ArmorTrimMaterial.class, server, ArmorTrimMaterialBinaryTagCodec.instance(),
                        getEntries(ArmorTrimMaterialRegistryEntry.class, ArmorTrimMaterials.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("trim_pattern"),
                        ArmorTrimPattern.class, server, ArmorTrimPatternBinaryTagCodec.instance(),
                        getEntries(ArmorTrimPatternRegistryEntry.class, ArmorTrimPatterns.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("chat_type"),
                        ChatType.class, server, ChatTypeBinaryTagCodec.instance(),
                        getEntries(ChatTypeRegistryEntry.class, ChatTypes.SPEC_JSON_FILE_NAME)),
                new JetRegistry<>(Key.key("damage_type"),
                        DamageType.class, server, DamageTypeBinaryTagCodec.instance(),
                        getEntries(DamageTypeRegistryEntry.class, DamageTypes.SPEC_JSON_FILE_NAME))
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

    private static <E> Set<RegistryEntry<E>> getEntries(@NonNull Class<? extends RegistryEntry<E>> entryClass,
                                                        @NonNull String jsonSpecFileName) {
        Set<RegistryEntry<E>> entries = new HashSet<>();
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