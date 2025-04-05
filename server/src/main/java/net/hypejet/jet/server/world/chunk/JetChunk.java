package net.hypejet.jet.server.world.chunk;

import net.hypejet.jet.data.model.api.block.entity.BlockEntityType;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.server.world.block.BlockType;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMapType;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.light.LightSerializationData;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.chunk.update.BiomeUpdate;
import net.hypejet.jet.server.world.chunk.update.BlockUpdate;
import net.hypejet.jet.server.world.chunk.update.LightUpdate;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain Chunk a chunk}.
 *
 * @since 1.0
 * @see Chunk
 */
public final class JetChunk implements Chunk<BlockState> {

    private final JetMinecraftServer server;

    private final ChunkSectionList chunkSectionList;
    private final LightSectionList lightSectionList;

    private final Set<HeightMap> heightMaps;
    private final Map<ChunkRelativeBlockPosition, BlockEntity> blockEntities;

    /**
     * Constructs the {@linkplain JetChunk chunk implementation}.
     *
     * @param server a server that the chunk is constructed for
     * @param chunkSectionList a chunk section list, which contains all chunk sections that the chunk should have
     * @param lightSectionList a light section list, which contains all light sections that the chunk should have
     * @param heightMaps a set of height maps that the chunk should have
     * @param blockEntities a map, which maps chunk-relative block positions of blocks that the chunk should have
     *                      to block entities associated with them
     * @since 1.0
     */
    private JetChunk(@NonNull JetMinecraftServer server, @NonNull ChunkSectionList chunkSectionList,
                     @NonNull LightSectionList lightSectionList, @NonNull Set<HeightMap> heightMaps,
                     @NonNull Map<ChunkRelativeBlockPosition, BlockEntity> blockEntities) {
        this.server = NullabilityUtil.requireNonNull(server, "server");

        this.chunkSectionList = NullabilityUtil.requireNonNull(chunkSectionList, "chunk section list");
        this.lightSectionList = NullabilityUtil.requireNonNull(lightSectionList, "light section list");

        this.heightMaps = Set.copyOf(NullabilityUtil.requireNonNull(heightMaps, "height maps"));
        this.blockEntities = Map.copyOf(NullabilityUtil.requireNonNull(blockEntities, "block entities"));

        BlockStateRegistry blockStateRegistry = server.registryManager().blockStateRegistry();
        for (Map.Entry<ChunkRelativeBlockPosition, BlockEntity> entry : blockEntities.entrySet()) {
            ChunkRelativeBlockPosition position = entry.getKey();
            BlockEntity blockEntity = entry.getValue();

            int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);

            JetChunkSection chunkSection = chunkSectionList.section(sectionY);
            ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);

            if (!(blockEntity.type() instanceof JetRegistryEntry<BlockEntityType> validatedRegistryEntry)) {
                throw new IllegalArgumentException(
                        "A registry entry of a type of block entity specified is not a valid registry entry"
                );
            }

            BlockState blockState = chunkSection.blockStatePalette().getElement(palettePosition);
            JetRegistryEntry<BlockType> blockType = blockStateRegistry.blockType(blockState);

            Key blockTypeKey = blockType.key();
            Key blockEntityTypeKey = validatedRegistryEntry.key();

            if (!validatedRegistryEntry.value().validBlocks().contains(blockTypeKey)) {
                throw new IllegalArgumentException(String.format(
                        "Block with type of %s is not allowed for a %s block entity",
                        blockTypeKey, blockEntityTypeKey
                ));
            }
        }
    }

    @Override
    public @NonNull List<JetChunkSection> sections() {
        return this.chunkSectionList.sections();
    }

    @Override
    public @NonNull List<JetLightSection> lightSections() {
        return this.lightSectionList.sections();
    }

    @Override
    public @NonNull Map<ChunkRelativeBlockPosition, BlockEntity> blockEntities() {
        return this.blockEntities;
    }

    /**
     * Gets {@linkplain ChunkSectionList a chunk section list} of {@linkplain JetChunkSection chunk sections}
     * of this {@linkplain JetChunk chunk}.
     *
     * @return the chunk section list
     * @since 1.0
     */
    public @NonNull ChunkSectionList chunkSectionList() {
        return this.chunkSectionList;
    }

    /**
     * Gets {@linkplain ChunkSectionList a chunk section list} of {@linkplain JetLightSection light sections}
     * of this {@linkplain JetChunk chunk}.
     *
     * @return the light section list
     * @since 1.0
     */
    public @NonNull LightSectionList lightSectionList() {
        return this.lightSectionList;
    }

    /**
     * Gets {@linkplain JetMinecraftServer a server} that this chunk has been created for.
     *
     * @return the server
     * @since 1.0
     */
    public @NonNull JetMinecraftServer server() {
        return this.server;
    }

    /**
     * Gets {@linkplain Set a set} of {@linkplain HeightMap height maps} of the chunk.
     *
     * @return the set
     * @since 1.0
     */
    public @NonNull Set<HeightMap> heightMaps() {
        return this.heightMaps;
    }

    /**
     * Gets {@linkplain LightSerializationData a light serialization data} of light data of this chunk.
     *
     * @return the light serialization data
     * @since 1.0
     */
    public @NonNull LightSerializationData lightSerializationData() {
        return this.lightSectionList.serializationData();
    }

    /**
     * Creates {@linkplain JetChunk a chunk}, which is a copy of this chunk with updates specified applied.
     *
     * @param blockUpdates updates that should be applied to block states
     * @param biomeUpdates updates that should be applied to biomes
     * @param lightUpdates updates that should be applied to light
     * @return the chunk created
     * @since 1.0
     */
    public @NonNull JetChunk withUpdates(@NonNull Collection<BlockUpdate> blockUpdates,
                                         @NonNull Collection<BiomeUpdate> biomeUpdates,
                                         @NonNull Collection<LightUpdate> lightUpdates) {
        // TODO: Block entity updating

        ChunkSectionList chunkSectionList = this.chunkSectionList.withUpdates(blockUpdates, biomeUpdates);
        LightSectionList lightSectionList = this.lightSectionList.withUpdates(lightUpdates);

        Set<HeightMap> heightMaps = new HashSet<>();
        for (HeightMap heightMap : this.heightMaps)
            heightMaps.add(heightMap.withUpdates(chunkSectionList, blockUpdates));

        // TODO: Handle block entity updates
        return new JetChunk(this.server, chunkSectionList, lightSectionList, heightMaps, this.blockEntities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JetChunk otherChunk)) return false;
        return Objects.equals(this.chunkSectionList, otherChunk.chunkSectionList)
                && Objects.equals(this.lightSectionList, otherChunk.lightSectionList)
                && Objects.equals(this.heightMaps, otherChunk.heightMaps)
                && Objects.equals(this.blockEntities, otherChunk.blockEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chunkSectionList, this.lightSectionList, this.heightMaps, this.blockEntities);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "chunkSectionList=" + this.chunkSectionList +
                ", lightSectionList=" + this.lightSectionList +
                ", heightMaps=" + this.heightMaps +
                ", blockEntities=" + this.blockEntities +
                '}';
    }

    /**
     * Creates {@linkplain JetChunk a chunk}.
     *
     * @param server a server that the chunk should be created for
     * @param dimensionType a registry entry of a dimension type of worlds that the chunk is created for
     * @param chunkSections a list of chunk sections that the chunk should have, where the lowest index is the lowest
     *                      section and the highest index is the highest section
     * @param lightSections a list of light sections that the chunk should have, where the lowest index is the lowest
     *                      section and the highest index is the highest section
     * @param blockEntities a map, which maps chunk-relative block positions of blocks to block entities that
     *                      the blocks should have
     * @return the chunk
     * @since 1.0
     */
    public static @NonNull JetChunk create(
            @NonNull JetMinecraftServer server, @NonNull DimensionType dimensionType,
            @NonNull List<JetChunkSection> chunkSections, @NonNull List<JetLightSection> lightSections,
            @NonNull Map<ChunkRelativeBlockPosition, BlockEntity> blockEntities
    ) {
        NullabilityUtil.requireNonNull(server, "server");
        NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        NullabilityUtil.requireNonNull(chunkSections, "chunk sections");
        NullabilityUtil.requireNonNull(lightSections, "light sections");
        NullabilityUtil.requireNonNull(blockEntities, "block entities");

        ChunkSectionList chunkSectionList = new ChunkSectionList(dimensionType, chunkSections);
        LightSectionList lightSectionList = new LightSectionList(dimensionType, lightSections);

        Set<HeightMap> heightMaps = new HashSet<>();
        for (HeightMapType type : HeightMapType.values())
            heightMaps.add(HeightMap.create(type, chunkSectionList, dimensionType));

        return new JetChunk(server, chunkSectionList, lightSectionList, heightMaps, blockEntities);
    }
}