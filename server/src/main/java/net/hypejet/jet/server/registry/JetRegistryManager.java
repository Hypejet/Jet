package net.hypejet.jet.server.registry;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.data.json.JetDataJson;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.registry.RegistryManager;
import net.hypejet.jet.registry.registries.biome.Biome;
import net.hypejet.jet.registry.registries.biome.BiomeRegistryEntry;
import net.hypejet.jet.server.registry.codecs.biome.BiomeBinaryTagCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.InputStream;
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
        Set<RegistryEntry<Biome>> entries = new HashSet<>();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("vanilla-biomes.json");

        if (stream != null) {
            try {
                String json = new String(stream.readAllBytes());
                entries.addAll(JetDataJson.deserialize(json, BiomeRegistryEntry.class));
                stream.close();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        JetRegistry<Biome> registry = new JetRegistry<>(Key.key("worldgen/biome"), Biome.class, server,
                BiomeBinaryTagCodec.instance(), Set.copyOf(entries));
        this.registries = Map.of(registry.registryIdentifier(), registry);
    }

    @Override
    public @Nullable JetRegistry<?> getRegistry(@NonNull Key identifier) {
        return this.registries.get(identifier);
    }

    @Override
    public @NonNull Map<Key, JetRegistry<?>> getRegistries() {
        return Map.copyOf(this.registries);
    }
}