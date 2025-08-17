package net.hypejet.jet.server.world;
import net.hypejet.jet.event.events.lifecycle.WorldManagerLoadEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.registry.blockstate.JetBlockStateRegistry;
import net.hypejet.jet.server.world.chunk.factory.JetChunkFactory;
import net.hypejet.jet.server.world.chunk.factory.light.JetLightStorageFactory;
import net.hypejet.jet.server.world.chunk.factory.palette.JetChunkPaletteFactory;
import net.hypejet.jet.server.world.chunk.factory.section.JetChunkSectionFactory;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.WorldManager;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.ChunkLoader;
import net.hypejet.jet.world.chunk.factory.ChunkFactory;
import net.hypejet.jet.world.chunk.factory.light.LightStorageFactory;
import net.hypejet.jet.world.chunk.factory.section.ChunkSectionFactory;
import net.hypejet.jet.world.data.WorldData;
import net.hypejet.jet.world.dimension.DimensionType;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents an implementation of a {@linkplain WorldManager world manager}.
 *
 * @since 1.0
 * @see WorldManager
 */
public final class JetWorldManager implements WorldManager {

    private final JetRegistryManager registryManager;

    private final JetChunkPaletteFactory<BlockState> blockStateChunkPaletteFactory;
    private final JetChunkPaletteFactory<Holder.Reference<Biome>> biomeChunkPaletteFactory;

    private final ChunkFactory chunkFactory;

    /**
     * Constructs the {@linkplain JetWorldManager world manager implementation}.
     *
     * @param eventNode an event node of the server that the world manager is being constructed for
     * @param registryManager a registry manager of the server that the world manager is being constructed for
     * @since 1.0
     */
    public JetWorldManager(@NonNull EventNode<Object> eventNode, @NonNull JetRegistryManager registryManager) {
        Objects.requireNonNull(eventNode, "event node");
        this.registryManager = Objects.requireNonNull(registryManager, "registry manager");

        this.blockStateChunkPaletteFactory = new JetChunkPaletteFactory<>(
                ChunkPaletteType.BLOCK_STATE,
                new BlockStateIndexSpecification(registryManager.blockStateRegistry())
        );

        this.biomeChunkPaletteFactory = new JetChunkPaletteFactory<>(
                ChunkPaletteType.BIOME,
                new BiomeIndexSpecification(registryManager.registry(RegistryReference.BIOME))
        );

        this.chunkFactory = new JetChunkFactory(
                registryManager,
                this.blockStateChunkPaletteFactory,
                this.biomeChunkPaletteFactory
        );

        eventNode.call(new WorldManagerLoadEvent(this));
    }

    @Override
    public @NonNull JetWorld createWorld(Holder.@NonNull Reference<DimensionType> dimensionType,
                                         @NonNull WorldData worldData, @NonNull ChunkLoader chunkLoader) {
        return new JetWorld(dimensionType, worldData, chunkLoader, this.registryManager);
    }

    @Override
    public @NonNull JetChunkPaletteFactory<BlockState> blockStateChunkPaletteFactory() {
        return this.blockStateChunkPaletteFactory;
    }

    @Override
    public @NonNull JetChunkPaletteFactory<Holder.Reference<Biome>> biomeChunkPaletteFactory() {
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

    /**
     * A {@linkplain AbstractChunkPalette.IndexSpecification chunk-palette index specification}
     * providing registry indices of {@linkplain BlockState block states}.
     *
     * @param blockStateRegistry the block-state registry to get the indices from
     * @since 1.0
     * @see BlockState
     * @see AbstractChunkPalette.IndexSpecification
     */
    private record BlockStateIndexSpecification(@NonNull JetBlockStateRegistry blockStateRegistry)
            implements AbstractChunkPalette.IndexSpecification<BlockState> {
        @Override
        public int indexFor(@NonNull BlockState value) {
            return this.blockStateRegistry.indexOf(value);
        }

        @Override
        public @NonNull BlockState valueByIndex(int index) {
            return this.blockStateRegistry.byIndex(index);
        }
    }

    /**
     * A {@linkplain AbstractChunkPalette.IndexSpecification chunk-palette index specification}
     * providing registry indices of {@linkplain Biome biomes}.
     *
     * @param biomeRegistry the biome registry to get the indices from
     * @since 1.0
     * @see Biome
     * @see AbstractChunkPalette.IndexSpecification
     */
    private record BiomeIndexSpecification(@NonNull JetMinecraftRegistry<Biome> biomeRegistry)
            implements AbstractChunkPalette.IndexSpecification<Holder.Reference<Biome>> {
        @Override
        public int indexFor(Holder.@NonNull Reference<Biome> value) {
            return this.biomeRegistry.indexOf(value.key());
        }

        @Override
        public Holder.@NonNull Reference<Biome> valueByIndex(int index) {
            JetMinecraftRegistry.RegistrationInfo<Biome> info = this.biomeRegistry.registrationInfos().get(index);
            if (info == null) {
                throw new IllegalArgumentException(String.format(
                        "Biome with index %d has not been registered",
                        index
                ));
            }
            return new Holder.Reference<>(info.key());
        }
    }
}