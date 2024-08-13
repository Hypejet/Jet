package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.biome.Biome;
import net.hypejet.jet.registry.Registry;
import net.hypejet.jet.registry.RegistryManager;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class JetRegistryManager implements RegistryManager {

    private final JetRegistry<Biome> biomeRegistry;

    public JetRegistryManager() {
        this.biomeRegistry = new JetRegistry<>(Key.key("worldgen/biome"), Biome.class);
    }

    @Override
    public @NonNull Registry<Biome> biomeRegistry() {
        return null;
    }
}