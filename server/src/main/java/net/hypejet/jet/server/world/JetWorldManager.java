package net.hypejet.jet.server.world;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.chunk.factory.JetChunkFactory;
import net.hypejet.jet.server.world.chunk.factory.light.JetLightStorageFactory;
import net.hypejet.jet.server.world.chunk.factory.palette.JetChunkPaletteFactory;
import net.hypejet.jet.server.world.chunk.factory.section.JetChunkSectionFactory;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.WorldManager;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.ChunkLoader;
import net.hypejet.jet.world.chunk.factory.ChunkFactory;
import net.hypejet.jet.world.chunk.factory.light.LightStorageFactory;
import net.hypejet.jet.world.chunk.factory.section.ChunkSectionFactory;
import net.hypejet.jet.world.data.WorldData;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain WorldManager a world manager}.
 *
 * @since 1.0
 * @see WorldManager
 */
public final class JetWorldManager implements WorldManager {

    private final JetMinecraftServer server;

    private final JetChunkPaletteFactory<BlockState> blockStateChunkPaletteFactory;
    private final JetChunkPaletteFactory<RegistryEntry<Biome>> biomeChunkPaletteFactory;

    private final ChunkFactory chunkFactory;

    /**
     * Constructs the {@linkplain JetWorldManager world manager implementation}.
     *
     * @param server a server that should own the world manager
     * @since 1.0
     */
    public JetWorldManager(@NonNull JetMinecraftServer server) {
        this.server = NullabilityUtil.requireNonNull(server, "server");
        JetRegistryManager registryManager = server.registryManager();

        this.blockStateChunkPaletteFactory = new JetChunkPaletteFactory<>(
                ChunkPaletteType.BLOCK_STATE,
                registryManager.blockStateRegistry().order()
        );

        this.biomeChunkPaletteFactory = new JetChunkPaletteFactory<>(
                ChunkPaletteType.BIOME,
                registryManager.biomeRegistry().elementOrder()
        );

        this.chunkFactory = new JetChunkFactory(server);
    }

    @Override
    public @NonNull JetWorld createWorld(@NonNull RegistryEntry<DimensionType> dimensionType,
                                         @NonNull WorldData worldData, @NonNull ChunkLoader chunkLoader) {
        if (!(dimensionType instanceof JetRegistryEntry<DimensionType> validatedDimensionType)) {
            throw new IllegalArgumentException("A dimension type registry entry" +
                    " specified is not a valid registry entry");
        }
        return new JetWorld(validatedDimensionType, worldData, chunkLoader, this.server);
    }

    @Override
    public @NonNull JetChunkPaletteFactory<BlockState> blockStateChunkPaletteFactory() {
        return this.blockStateChunkPaletteFactory;
    }

    @Override
    public @NonNull JetChunkPaletteFactory<RegistryEntry<Biome>> biomeChunkPaletteFactory() {
        return this.biomeChunkPaletteFactory;
    }

    @Override
    public @NonNull LightStorageFactory lightStorageFactory() {
        return JetLightStorageFactory.INSTANCE;
    }

    @Override
    public @NonNull ChunkSectionFactory chunkSectionFactory() {
        return JetChunkSectionFactory.INSTANCE;
    }

    @Override
    public @NonNull ChunkFactory chunkFactory() {
        return this.chunkFactory;
    }

    @Override
    public @NonNull MinecraftServer server() {
        return this.server;
    }
}