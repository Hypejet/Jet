package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;
import net.hypejet.jet.data.model.server.registry.registries.block.entity.BlockEntityType;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.block.JetBlockType;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.hypejet.jet.server.world.chunk.light.LightSerializationData;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents {@linkplain ServerPacket a server packet}, which initializes {@linkplain JetChunk a chunk} for a client.
 *
 * @param chunkPosition a position of the chunk
 * @param heightMaps a collection of heightmaps that the chunk should have
 * @param chunkSectionList a chunk-section list of chunk sections that the chunk should have
 * @param blockEntities a map, which maps chunk-relative block positions to block entities that block at these
 *                      positions should have
 * @param lightSerializationData a light serialization data of light data that the chunk should have
 * @since 1.0
 * @see JetChunk
 * @see ServerPacket
 */
public record ServerChunkAndLightDataPlayPacket(
        @NonNull ChunkPosition chunkPosition, @NonNull Collection<HeightMap> heightMaps,
        @NonNull ChunkSectionList chunkSectionList,
        @NonNull Map<ChunkRelativeBlockPosition, BlockEntity> blockEntities,
        @NonNull LightSerializationData lightSerializationData
) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerChunkAndLightDataPlayPacket server chunk and light data play packet}.
     *
     * @param chunkPosition a position of the chunk
     * @param heightMaps a collection of heightmaps that the chunk should have
     * @param chunkSectionList a chunk-section list of chunk sections that the chunk should have
     * @param blockEntities a map, which maps chunk-relative block positions to block entities that block at these
     *                      positions should have
     * @param lightSerializationData a light serialization data of light data that the chunk should have
     * @since 1.0
     */
    public ServerChunkAndLightDataPlayPacket {
        Objects.requireNonNull(chunkPosition, "chunk position");
        Objects.requireNonNull(heightMaps, "height maps");
        Objects.requireNonNull(chunkSectionList, "chunk-section list");
        Objects.requireNonNull(blockEntities, "block entities");
        Objects.requireNonNull(lightSerializationData, "light serialization data");

        heightMaps = Set.copyOf(heightMaps);
        blockEntities = Map.copyOf(blockEntities);
    }

    public static @NonNull ServerChunkAndLightDataPlayPacket create(
            @NonNull ChunkPosition chunkPosition, @NonNull JetChunk chunk,
            @NonNull JetRegistryManager registryManager
    ) {
        Map<ChunkRelativeBlockPosition, BlockEntity> blockEntities = new HashMap<>();
        JetMinecraftRegistry<BlockEntityType> blockEntityTypeRegistry = registryManager.blockEntityTypeRegistry();

        for (Map.Entry<ChunkRelativeBlockPosition, CompoundBinaryTag> entry : chunk.blockEntities().entrySet()) {
            ChunkRelativeBlockPosition position = entry.getKey();
            if (!(chunk.blockState(position) instanceof JetBlockState blockState)) {
                throw new IllegalArgumentException(String.format(
                        "Block state at position %s is not a valid block state",
                        position
                ));
            }

            JetRegistryEntry<JetBlockType> blockType = blockState.blockType();
            JetRegistryEntry<BlockEntityType> blockEntityType = blockType.value().blockEntityType();

            if (blockEntityType == null) {
                throw new IllegalArgumentException(String.format(
                        "Block type with key of %s cannot have a block entity",
                        blockType.key()
                ));
            }

            CompoundBinaryTag blockEntityData = entry.getValue();
            int blockEntityTypeIdentifier = blockEntityTypeRegistry.identifierOf(blockEntityType);
            blockEntities.put(position, new BlockEntity(blockEntityTypeIdentifier, blockEntityData));
        }

        return new ServerChunkAndLightDataPlayPacket(
                chunkPosition, chunk.heightMaps().values(), chunk.chunkSectionList(),
                blockEntities, chunk.lightSerializationData()
        );
    }

    /**
     * Represents a serialization-ready Minecraft block entity entry.
     *
     * @param typeIdentifier an identifier of a block entity type of the block entity
     * @param data data of the block entity
     * @since 1.0
     */
    public record BlockEntity(int typeIdentifier, @NonNull CompoundBinaryTag data) {
        /**
         * Constructs the {@linkplain BlockEntity block entity}.
         *
         * @param typeIdentifier an identifier of a block entity type of the block entity
         * @param data data of the block entity
         * @since 1.0
         */
        public BlockEntity {
            Objects.requireNonNull(data, "data");
        }
    }
}