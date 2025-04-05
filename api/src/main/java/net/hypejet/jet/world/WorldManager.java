package net.hypejet.jet.world;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.chunk.ChunkLoader;
import net.hypejet.jet.world.chunk.factory.ChunkFactory;
import net.hypejet.jet.world.chunk.factory.light.LightStorageFactory;
import net.hypejet.jet.world.chunk.factory.palette.BlockStateChunkPaletteFactory;
import net.hypejet.jet.world.chunk.factory.palette.ChunkPaletteFactory;
import net.hypejet.jet.world.chunk.factory.section.ChunkSectionFactory;
import net.hypejet.jet.world.chunk.light.LightSection;
import net.hypejet.jet.world.chunk.section.ChunkSection;
import net.hypejet.jet.world.data.WorldData;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that manages creation of {@linkplain World worlds} of {@linkplain MinecraftServer a server}.
 *
 * @param <CS> a type of chunk section implementation that the server uses
 * @param <LS> a type of light section implementation that the server uses
 * @param <BS> a type of block state implementation that the server uses
 * @since 1.0
 * @see World
 * @see MinecraftServer
 */
public interface WorldManager<CS extends ChunkSection<BS>, LS extends LightSection, BS> {
    /**
     * Creates {@linkplain World a world}.
     *
     * @param dimensionType a dimension type, of which type the world should be
     * @param worldData an additional world data that the world should have
     * @param chunkLoader a chunk loader that should be used for loading and saving chunks of the world
     * @return the world
     * @since 1.0
     */
    @NonNull World createWorld(@NonNull RegistryEntry<DimensionType> dimensionType,
                               @NonNull WorldData worldData, @NonNull ChunkLoader<BS> chunkLoader);

    /**
     * Gets {@linkplain BlockStateChunkPaletteFactory a block-state chunk palette factory}, which should be used
     * for creating block-state {@linkplain net.hypejet.jet.world.chunk.section.ChunkPalette chunk palettes}
     * for a server associated with this world manager.
     *
     * @return the block-state chunk palette factory
     * @since 1.0
     */
    @NonNull BlockStateChunkPaletteFactory<BS> blockStateChunkPaletteFactory();

    /**
     * Gets {@linkplain ChunkPaletteFactory a chunk palette factory}, which should be used for creating
     * {@linkplain Biome biome} {@linkplain net.hypejet.jet.world.chunk.section.ChunkPalette chunk palettes}
     * for a server associated with this world manager.
     *
     * @return the biome chunk palette factory
     * @since 1.0
     */
    @NonNull ChunkPaletteFactory<RegistryEntry<Biome>> biomeChunkPaletteFactory();

    /**
     * Gets {@linkplain LightStorageFactory a light storage factory}, which should be used for creating
     * {@linkplain net.hypejet.jet.world.chunk.light.LightStorage light storages} for a server associated
     * with this world manager.
     *
     * @return the light storage factory
     * @since 1.0
     */
    @NonNull LightStorageFactory lightStorageFactory();

    /**
     * Gets {@linkplain ChunkSectionFactory a chunk section factory}, which should be used for creating
     * {@linkplain ChunkSection chunk sections} for a server associated with this world manager.
     *
     * @return the chunk section factory
     * @since 1.0
     */
    @NonNull ChunkSectionFactory<CS, LS, BS> chunkSectionFactory();

    /**
     * Gets {@linkplain ChunkFactory a chunk factory}, which should be used for creating
     * {@linkplain net.hypejet.jet.world.chunk.Chunk chunks} for a server associated with this world manager.
     *
     * @return the chunk factory
     * @since 1.0
     */
    @NonNull ChunkFactory<CS, LS, BS> chunkFactory();

    /**
     * Gets {@linkplain MinecraftServer a server} that this world manager was created for.
     *
     * @return the server
     * @since 1.0
     */
    @NonNull MinecraftServer server();
}