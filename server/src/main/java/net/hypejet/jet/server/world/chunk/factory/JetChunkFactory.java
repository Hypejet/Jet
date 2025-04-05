package net.hypejet.jet.server.world.chunk.factory;

import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.chunk.factory.ChunkFactory;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Represents an implementation of {@linkplain ChunkFactory a chunk factory}.
 *
 * @implSpec 1.0
 * @see ChunkFactory
 */
public final class JetChunkFactory implements ChunkFactory<JetChunkSection, JetLightSection, BlockState> {

    private final JetMinecraftServer server;

    /**
     * Constructs the {@linkplain JetChunkFactory chunk factory implementation}.
     *
     * @param server a server that should own chunks created by the factory
     * @since 1.0
     */
    public JetChunkFactory(@NonNull JetMinecraftServer server) {
        this.server = NullabilityUtil.requireNonNull(server, "server");
    }

    @Override
    public @NonNull JetChunk createChunk(@NonNull DimensionType dimensionType,
                                         @NonNull List<JetChunkSection> chunkSections,
                                         @NonNull List<JetLightSection> lightSections,
                                         @NonNull Map<ChunkRelativeBlockPosition, BlockEntity> blockEntities) {
        return JetChunk.create(this.server, dimensionType, chunkSections, lightSections, blockEntities);
    }
}