package net.hypejet.jet.server.registry;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.data.codecs.JetDataJson;
import net.hypejet.jet.data.generated.Biomes;
import net.hypejet.jet.data.model.registry.RegistryEntry;
import net.hypejet.jet.data.model.registry.registries.biome.Biome;
import net.hypejet.jet.data.model.registry.registries.biome.BiomeRegistryEntry;
import net.hypejet.jet.registry.RegistryManager;
import net.hypejet.jet.server.registry.codecs.biome.BiomeBinaryTagCodec;
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
        Set<JetRegistry<?>> registrySet = Set.of(
                new JetRegistry<>(Key.key("worldgen/biome"),
                        Biome.class, server, BiomeBinaryTagCodec.instance(),
                        getEntries(BiomeRegistryEntry.class, Biomes.SPEC_JSON_FILE_NAME))
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