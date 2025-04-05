package net.hypejet.jet.server.world.chunk.factory.palette;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.server.world.block.BlockType;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.SingleValuedChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.chunk.factory.palette.BlockStateChunkPaletteFactory;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Represents an implementation of {@linkplain BlockStateChunkPaletteFactory a block-state chunk palette factory}.
 *
 * @since 1.0
 * @see BlockStateChunkPaletteFactory
 */
public final class JetBlockStateChunkPaletteFactory implements BlockStateChunkPaletteFactory<BlockState> {

    private final JetMinecraftRegistry<BlockType> blockTypeRegistry;
    private final BlockStateRegistry blockStateRegistry;

    /**
     * Constructs the {@linkplain JetBlockStateChunkPaletteFactory block-state chunk palette factory implementation}.
     *
     * @param server a server that should own chunk palettes created by the factory
     * @since 1.0
     */
    public JetBlockStateChunkPaletteFactory(@NonNull JetMinecraftServer server) {
        NullabilityUtil.requireNonNull(server, "server");
        JetRegistryManager registryManager = server.registryManager();
        this.blockTypeRegistry = registryManager.blockTypeRegistry();
        this.blockStateRegistry = registryManager.blockStateRegistry();
    }

    @Override
    public @NonNull AbstractChunkPalette<BlockState> createSingleValued(@NonNull BlockState element) {
        NullabilityUtil.requireNonNull(element, "element");
        return new SingleValuedChunkPalette<>(ChunkPaletteType.BLOCK_STATE, element, this.blockStateRegistry.order());
    }

    @Override
    public @NonNull AbstractChunkPalette<BlockState> createDirect(@NonNull List<BlockState> elements) {
        NullabilityUtil.requireNonNull(elements, "elements");
        return AbstractChunkPalette.create(ChunkPaletteType.BLOCK_STATE, this.blockStateRegistry.order(), elements);
    }

    @Override
    public @NonNull BlockState blockState(@NonNull Key blockTypeKey, @NonNull Map<String, String> properties) {
        NullabilityUtil.requireNonNull(blockTypeKey, "block type key");
        NullabilityUtil.requireNonNull(properties, "properties");
        return this.blockType(blockTypeKey).value().state(properties);
    }

    @Override
    public @NonNull Key blockTypeKey(@NonNull BlockState blockState) {
        NullabilityUtil.requireNonNull(blockState, "block state");
        return this.blockStateRegistry.blockType(blockState).key();
    }

    @Override
    public @NonNull Map<String, String> blockProperties(@NonNull BlockState blockState) {
        NullabilityUtil.requireNonNull(blockState, "block state");
        return blockState.properties();
    }

    @Override
    public @NonNull BlockState defaultBlockState(@NonNull Key blockTypeKey) {
        NullabilityUtil.requireNonNull(blockTypeKey, "block type key");
        return this.blockType(blockTypeKey).value().defaultState();
    }

    private @NonNull JetRegistryEntry<BlockType> blockType(@NonNull Key blockTypeKey) {
        JetRegistryEntry<BlockType> blockType = this.blockTypeRegistry.get(blockTypeKey);
        if (blockType == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block type with key of %s",
                    blockTypeKey
            ));
        }
        return blockType;
    }
}