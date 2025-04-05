package net.hypejet.jet.server.world;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.world.chunk.factory.JetChunkFactory;
import net.hypejet.jet.server.world.chunk.factory.light.JetLightStorageFactory;
import net.hypejet.jet.server.world.chunk.factory.palette.BiomeChunkPaletteFactory;
import net.hypejet.jet.server.world.chunk.factory.palette.JetBlockStateChunkPaletteFactory;
import net.hypejet.jet.server.world.chunk.factory.section.JetChunkSectionFactory;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.world.WorldManager;
import net.hypejet.jet.world.chunk.ChunkLoader;
import net.hypejet.jet.world.chunk.factory.ChunkFactory;
import net.hypejet.jet.world.chunk.factory.light.LightStorageFactory;
import net.hypejet.jet.world.chunk.factory.palette.BlockStateChunkPaletteFactory;
import net.hypejet.jet.world.chunk.factory.palette.ChunkPaletteFactory;
import net.hypejet.jet.world.chunk.factory.section.ChunkSectionFactory;
import net.hypejet.jet.world.data.WorldData;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain WorldManager a world manager}.
 *
 * @since 1.0
 * @see WorldManager
 */
public final class JetWorldManager implements WorldManager<JetChunkSection, JetLightSection, BlockState> {

    private final JetMinecraftServer server;

    private final BlockStateChunkPaletteFactory<BlockState> blockStateChunkPaletteFactory;
    private final ChunkPaletteFactory<RegistryEntry<Biome>> biomeChunkPaletteFactory;

    private final ChunkSectionFactory<JetChunkSection, JetLightSection, BlockState> chunkSectionFactory;
    private final ChunkFactory<JetChunkSection, JetLightSection, BlockState> chunkFactory;

    /**
     * Constructs the {@linkplain JetWorldManager world manager implementation}.
     *
     * @param server a server that should own the world manager
     * @since 1.0
     */
    public JetWorldManager(@NonNull JetMinecraftServer server) {
        this.server = NullabilityUtil.requireNonNull(server, "server");

        this.blockStateChunkPaletteFactory = new JetBlockStateChunkPaletteFactory(server);
        this.biomeChunkPaletteFactory = new BiomeChunkPaletteFactory(server);

        this.chunkSectionFactory = new JetChunkSectionFactory(server);
        this.chunkFactory = new JetChunkFactory(server);
    }

    @Override
    public @NonNull JetWorld createWorld(@NonNull RegistryEntry<DimensionType> dimensionType,
                                         @NonNull WorldData worldData, @NonNull ChunkLoader<BlockState> chunkLoader) {
        if (!(dimensionType instanceof JetRegistryEntry<DimensionType> validatedDimensionType)) {
            throw new IllegalArgumentException("A dimension type registry entry" +
                    " specified is not a valid registry entry");
        }
        return new JetWorld(validatedDimensionType, worldData, chunkLoader, this.server);
    }

    @Override
    public @NonNull BlockStateChunkPaletteFactory<BlockState> blockStateChunkPaletteFactory() {
        return this.blockStateChunkPaletteFactory;
    }

    @Override
    public @NonNull ChunkPaletteFactory<RegistryEntry<Biome>> biomeChunkPaletteFactory() {
        return this.biomeChunkPaletteFactory;
    }

    @Override
    public @NonNull LightStorageFactory lightStorageFactory() {
        return JetLightStorageFactory.INSTANCE;
    }

    @Override
    public @NonNull ChunkSectionFactory<JetChunkSection, JetLightSection, BlockState> chunkSectionFactory() {
        return this.chunkSectionFactory;
    }

    @Override
    public @NonNull ChunkFactory<JetChunkSection, JetLightSection, BlockState> chunkFactory() {
        return this.chunkFactory;
    }

    @Override
    public @NonNull MinecraftServer server() {
        return this.server;
    }
}