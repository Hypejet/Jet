package net.hypejet.jet.server.world.chunk.factory.palette;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.SingleValuedChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.chunk.factory.palette.ChunkPaletteFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents {@linkplain ChunkPaletteFactory a chunk palette factory}, which creates
 * {@linkplain AbstractChunkPalette chunk palettes} containing {@linkplain Biome biomes}.
 *
 * @since 1.0
 * @see Biome
 * @see ChunkPaletteFactory
 */
public final class BiomeChunkPaletteFactory implements ChunkPaletteFactory<RegistryEntry<Biome>> {

    private final ElementOrder<JetRegistryEntry<Biome>> biomeOrder;

    /**
     * Constructs the {@linkplain BiomeChunkPaletteFactory biome chunk palette factory implementation}.
     *
     * @param server a server that should own chunk palettes created by the factory
     * @since 1.0
     */
    public BiomeChunkPaletteFactory(@NonNull JetMinecraftServer server) {
        NullabilityUtil.requireNonNull(server, "server");
        this.biomeOrder = server.registryManager().biomeRegistry().elementOrder();
    }

    @Override
    public @NonNull AbstractChunkPalette<RegistryEntry<Biome>> createSingleValued(
            @NonNull RegistryEntry<Biome> element
    ) {
        return new SingleValuedChunkPalette<>(ChunkPaletteType.BIOME, element, this.biomeOrder);
    }

    @Override
    public @NonNull AbstractChunkPalette<RegistryEntry<Biome>> createDirect(
            @NonNull List<RegistryEntry<Biome>> elements
    ) {
        return AbstractChunkPalette.create(ChunkPaletteType.BIOME, this.biomeOrder, elements);
    }
}