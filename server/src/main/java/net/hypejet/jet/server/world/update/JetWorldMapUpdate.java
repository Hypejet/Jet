package net.hypejet.jet.server.world.update;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBiomesPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBlockEntityPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBlockStatePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateChunkSectionBlockStatesPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateLightPlayPacket;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.registry.blockstate.JetBlockStateRegistry;
import net.hypejet.jet.server.util.coordinate.AbsolutePositionUtil;
import net.hypejet.jet.server.util.coordinate.ChunkPositionUtil;
import net.hypejet.jet.server.util.coordinate.ChunkRelativePositionUtil;
import net.hypejet.jet.server.world.acquisition.worldmap.WriteWorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.block.state.JetBlockState;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.light.LightSerializationData;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.chunk.view.ChunkView;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.server.world.coordinate.chunk.section.ChunkSectionPosition;
import net.hypejet.jet.server.world.handler.ChunkBatchHandler;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.block.BlockType;
import net.hypejet.jet.world.block.entity.BlockEntityType;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.biome.BiomePosition;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.hypejet.jet.world.update.WorldMapUpdate;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain WorldMapUpdate a world map update}.
 *
 * @since 1.0
 * @see WorldMapUpdate
 */
public final class JetWorldMapUpdate implements WorldMapUpdate {

    private final Map<ChunkPosition, ChunkUpdateBuilder> updateBuilders = new HashMap<>();
    private final WriteWorldMapAcquisitionImpl acquisition;

    private boolean updated;

    /**
     * Constructs the {@linkplain JetWorldMapUpdate world map update implementation}.
     *
     * @param acquisition a write acquisition of a world map that the update should be done for
     * @since 1.0
     */
    public JetWorldMapUpdate(@NonNull WriteWorldMapAcquisitionImpl acquisition) {
        this.acquisition = Objects.requireNonNull(acquisition, "acquisition");
    }

    @Override
    public @NonNull WorldMapUpdate updateBlockState(@NonNull BlockPosition position, @NonNull BlockState blockState) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(blockState, "block state");
        this.check();

        ChunkPosition chunkPosition = ChunkPositionUtil.fromCoordinate(position);
        ChunkRelativeBlockPosition relativePosition = ChunkRelativePositionUtil.from(position);

        this.updateBuilder(chunkPosition).updateBlockState(relativePosition, blockState);
        return this;
    }

    @Override
    public @NonNull WorldMapUpdate updateBlockEntity(@NonNull BlockPosition position,
                                                     @NonNull CompoundBinaryTag blockEntityData) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(blockEntityData, "block entity data");
        this.check();

        ChunkPosition chunkPosition = ChunkPositionUtil.fromCoordinate(position);
        ChunkRelativeBlockPosition relativePosition = ChunkRelativePositionUtil.from(position);

        this.updateBuilder(chunkPosition).updateBlockEntity(relativePosition, blockEntityData);
        return this;
    }

    @Override
    public @NonNull WorldMapUpdate updateBiome(@NonNull BiomePosition position,
                                               Holder.@NonNull Reference<Biome> biome) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(biome, "biome");
        this.check();

        ChunkPosition chunkPosition = ChunkPositionUtil.fromBiomePosition(position);
        ChunkRelativeBiomePosition relativePosition = ChunkRelativePositionUtil.from(position);

        this.updateBuilder(chunkPosition).updateBiome(relativePosition, biome);
        return this;
    }

    @Override
    public @NonNull WorldMapUpdate updateSkyLightLevel(@NonNull BlockPosition position, byte level) {
        Objects.requireNonNull(position, "position");
        this.check();

        ChunkPosition chunkPosition = ChunkPositionUtil.fromCoordinate(position);
        ChunkRelativeBlockPosition relativePosition = ChunkRelativePositionUtil.from(position);

        this.updateBuilder(chunkPosition).updateSkyLightLevel(relativePosition, level);
        return this;
    }

    @Override
    public @NonNull WorldMapUpdate updateBlockLightLevel(@NonNull BlockPosition position, byte level) {
        Objects.requireNonNull(position, "position");
        this.check();

        ChunkPosition chunkPosition = ChunkPositionUtil.fromCoordinate(position);
        ChunkRelativeBlockPosition relativePosition = ChunkRelativePositionUtil.from(position);

        this.updateBuilder(chunkPosition).updateBlockLightLevel(relativePosition, level);
        return this;
    }

    @Override
    public void update() {
        this.check();
        this.updated = true;

        for (ChunkUpdateBuilder updateBuilder : this.updateBuilders.values())
            this.acquisition.setChunk(updateBuilder.chunkPosition, updateBuilder.createUpdatedChunk());

        Map<ChunkView, ServerUpdateBiomesPlayPacket> biomeUpdatePackets = new HashMap<>(); // TODO: Frame
        Multimap<ChunkPosition, ServerPacket> blockAndLightUpdatePackets = ArrayListMultimap.create(); // TODO: Frame

        this.acquisition.world().players().forEach(player -> {
            ChunkBatchHandler chunkBatchHandler = player.chunkBatchHandler();
            ChunkView chunkView = chunkBatchHandler.chunkView();
            if (chunkView == null) return;

            chunkView.forEach(chunkPosition -> {
                if (chunkBatchHandler.isPending(chunkPosition)) return;

                if (!blockAndLightUpdatePackets.containsKey(chunkPosition)) {
                    blockAndLightUpdatePackets.putAll(
                            chunkPosition,
                            this.updateBuilder(chunkPosition).createBlockAndLightUpdatePackets()
                    );
                }

                blockAndLightUpdatePackets.get(chunkPosition).forEach(player::sendPacket);
            });

            // FIXME: Biome cache is used while the cache might have been created when certain chunks are pending
            ServerUpdateBiomesPlayPacket packet = biomeUpdatePackets.computeIfAbsent(
                    chunkView,
                    this::createBiomeUpdatePacket
            );

            if (packet == null) return;
            player.sendPacket(packet);
        });
    }

    private @Nullable ServerUpdateBiomesPlayPacket createBiomeUpdatePacket(@NonNull ChunkView chunkView) {
        Set<ServerUpdateBiomesPlayPacket.BiomeData> biomeData = new HashSet<>();

        chunkView.forEach(chunkPosition -> {
            ChunkUpdateBuilder updateBuilder = this.updateBuilders.get(chunkPosition);

            if (updateBuilder == null) return;
            if (updateBuilder.biomeUpdates.isEmpty()) return;

            JetChunk chunk = this.acquisition.getChunk(chunkPosition);
            List<AbstractChunkPalette<Holder.Reference<Biome>>> palettes = new ArrayList<>();

            for (JetChunkSection section : chunk.sections())
                palettes.add(section.biomePalette());
            biomeData.add(new ServerUpdateBiomesPlayPacket.BiomeData(chunkPosition, palettes));
        });

        if (biomeData.isEmpty()) return null;
        return new ServerUpdateBiomesPlayPacket(biomeData);
    }

    private @NonNull ChunkUpdateBuilder updateBuilder(@NonNull ChunkPosition chunkPosition) {
        return this.updateBuilders.computeIfAbsent(
                chunkPosition,
                ignored -> new ChunkUpdateBuilder(chunkPosition, this.acquisition)
        );
    }

    private void check() {
        this.acquisition.ensurePermittedAndLocked();
        if (this.updated)
            throw new IllegalStateException("The update has been already performed");
    }

    /**
     * Represents builder of an update of {@linkplain JetChunk a chunk}.
     *
     * @since 1.0
     * @see JetChunk
     */
    private static final class ChunkUpdateBuilder {

        private final ChunkPosition chunkPosition;
        private final WriteWorldMapAcquisitionImpl acquisition;

        private final Map<ChunkRelativeBlockPosition, JetBlockState> blockStateUpdates = new HashMap<>();
        private final Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntityUpdates = new HashMap<>();
        private final Map<ChunkRelativeBiomePosition, Holder.Reference<Biome>> biomeUpdates = new HashMap<>();

        private final Object2ByteMap<ChunkRelativeBlockPosition> skyLightUpdates = new Object2ByteOpenHashMap<>();
        private final Object2ByteMap<ChunkRelativeBlockPosition> blockLightUpdates = new Object2ByteOpenHashMap<>();

        /**
         * Constructs the {@linkplain ChunkUpdateBuilder chunk update builder}.
         *
         * @param chunkPosition a chunk position of the chunk that is being updated
         * @param acquisition a write acquisition of a world map that the chunk belongs to
         * @since 1.0
         */
        private ChunkUpdateBuilder(@NonNull ChunkPosition chunkPosition,
                                   @NonNull WriteWorldMapAcquisitionImpl acquisition) {
            this.chunkPosition = Objects.requireNonNull(chunkPosition, "chunk position");
            this.acquisition = Objects.requireNonNull(acquisition, "acquisition");
        }

        /**
         * Sets {@linkplain BlockState a block state} of a block
         * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified to be updated
         * with a value specified.
         *
         * <p>Note that a block entity associated with the block will be removed if block types of previous block
         * state and the block state specified are different.</p>
         *
         * @param position the chunk-relative block positions
         * @param blockState the value
         * @since 1.0
         */
        private void updateBlockState(@NonNull ChunkRelativeBlockPosition position, @NonNull BlockState blockState) {
            if (!(blockState instanceof JetBlockState validatedBlockState))
                throw new IllegalArgumentException("The block state specified is not a valid block state");

            BlockState currentBlockState = this.chunk().blockState(position);
            if (currentBlockState == blockState) {
                this.blockStateUpdates.remove(position);
                return;
            }

            this.blockStateUpdates.put(position, validatedBlockState);

            /* Since clients receiving block state use their light engines to automatically update the light,
               we need to always send a light value as we do not know the new client light value, because the server
               has no light engine implemented by default.
               TODO: Replace the light hack with a proper light implementation */
            if (currentBlockState.blockType() != blockState.blockType()) {
                this.updateSkyLightLevel(position, this.chunk().skyLightLevel(position));
                this.updateBlockLightLevel(position, this.chunk().blockLightLevel(position));
            }
        }

        /**
         * Sets block entity of a block at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position}
         * specified to be updated with a block entity data specified.
         *
         * @param position the chunk-relative block position
         * @param blockEntityData the block entity data
         * @since 1.0
         */
        private void updateBlockEntity(@NonNull ChunkRelativeBlockPosition position,
                                       @NonNull CompoundBinaryTag blockEntityData) {
            if (Objects.equals(this.chunk().blockEntityData(position), blockEntityData)) {
                this.blockEntityUpdates.remove(position);
                return;
            }
            this.blockEntityUpdates.put(position, blockEntityData);
        }

        /**
         * Sets a {@linkplain Biome biome} at the specified
         * {@linkplain ChunkRelativeBiomePosition chunk-relative biome position} to be updated
         * with the specified value.
         *
         * @param position the chunk-relative biome position
         * @param biome a holder referencing to a biome that should be present at the specified position
         * @since 1.0
         */
        private void updateBiome(@NonNull ChunkRelativeBiomePosition position,
                                 Holder.@NonNull Reference<Biome> biome) {
            if (this.chunk().biome(position) == biome) {
                this.biomeUpdates.remove(position);
                return;
            }
            this.biomeUpdates.put(position, biome);
        }

        /**
         * Sets a skylight level value of a block
         * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified to be updated
         * with a value specified.
         *
         * @param position the chunk-relative block position
         * @param level the value
         * @since 1.0
         */
        private void updateSkyLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level) {
            if (this.chunk().skyLightLevel(position) == level) {
                // TODO: Replace the light hack with a proper light implementation
                BlockState previousBlockState = this.chunk().blockState(position);
                BlockState updatedBlockState = this.blockStateUpdates.get(position);

                if (updatedBlockState == null || previousBlockState.blockType() == updatedBlockState.blockType()) {
                    this.skyLightUpdates.removeByte(position);
                    return;
                }
            }

            this.skyLightUpdates.put(position, level);
        }

        /**
         * Sets a block light level value of a block
         * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified to be updated
         * with a value specified.
         *
         * @param position the chunk-relative block position
         * @param level the value
         * @since 1.0
         */
        private void updateBlockLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level) {
            if (this.chunk().blockLightLevel(position) == level) {
                // TODO: Replace the light hack with a proper light implementation
                BlockState previousBlockState = this.chunk().blockState(position);
                BlockState updatedBlockState = this.blockStateUpdates.get(position);

                if (updatedBlockState == null || previousBlockState.blockType() == updatedBlockState.blockType()) {
                    this.blockLightUpdates.removeByte(position);
                    return;
                }
            }
            this.blockLightUpdates.put(position, level);
        }

        /**
         * Creates an updated {@linkplain JetChunk chunk} with updates specified
         * in this {@linkplain ChunkUpdateBuilder chunk update builder}.
         *
         * @return the chunk
         * @since 1.0
         */
        private @NonNull JetChunk createUpdatedChunk() {
            return this.chunk().withUpdates(
                    this.blockStateUpdates, this.blockEntityUpdates, this.biomeUpdates,
                    this.skyLightUpdates, this.blockLightUpdates
            );
        }

        /**
         * Creates {@linkplain List a list} of {@linkplain ServerPacket server packets} which request
         * clients to perform updates specified in this {@linkplain ChunkUpdateBuilder chunk update builder}.
         *
         * @return the list
         * @since 1.0
         */
        private @NonNull List<ServerPacket> createBlockAndLightUpdatePackets() {
            List<ServerPacket> packets = new ArrayList<>();

            if (
                    this.blockStateUpdates.isEmpty() && this.blockEntityUpdates.isEmpty()
                    && this.skyLightUpdates.isEmpty() && this.blockLightUpdates.isEmpty()
            ) {
                return List.of();
            }

            JetRegistryManager registryManager = this.acquisition.world().registryManager();
            JetMinecraftRegistry<BlockType> blockTypeRegistry = registryManager.registry(RegistryReference.BLOCK);

            JetMinecraftRegistry<BlockEntityType> blockEntityTypeRegistry = registryManager.registry(
                    RegistryReference.BLOCK_ENTITY_TYPE
            );

            Map<ChunkSectionPosition, Map<ChunkRelativeBlockPosition, BlockState>> sectionUpdates = new HashMap<>();
            for (Map.Entry<ChunkRelativeBlockPosition, JetBlockState> blockUpdate : this.blockStateUpdates.entrySet()) {
                ChunkSectionPosition chunkSectionPosition = ChunkSectionPosition.from(
                        this.chunkPosition,
                        blockUpdate.getKey().absoluteY(),
                        ChunkPaletteType.BLOCK_STATE
                );

                sectionUpdates.computeIfAbsent(chunkSectionPosition, ignored -> new HashMap<>()).put(
                        blockUpdate.getKey(),
                        blockUpdate.getValue()
                );
            }

            JetBlockStateRegistry blockStateRegistry = registryManager.blockStateRegistry();
            for (ChunkSectionPosition sectionPosition : sectionUpdates.keySet()) {
                Map<ChunkRelativeBlockPosition, BlockState> updates = sectionUpdates.get(sectionPosition);
                if (updates.size() == 1) {
                    Map.Entry<ChunkRelativeBlockPosition, BlockState> update = updates.entrySet().iterator().next();

                    BlockPosition blockPosition = AbsolutePositionUtil.from(update.getKey(), this.chunkPosition);
                    int blockStateRegistryIndex = blockStateRegistry.indexOf(update.getValue());

                    packets.add(new ServerUpdateBlockStatePlayPacket(blockPosition, blockStateRegistryIndex));
                    continue;
                }

                Object2IntMap<ChunkPaletteRelativePosition> updateMap = new Object2IntOpenHashMap<>();
                for (Map.Entry<ChunkRelativeBlockPosition, BlockState> entry : updates.entrySet()) {
                    ChunkRelativeBlockPosition position = entry.getKey();
                    ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);
                    updateMap.put(palettePosition, blockStateRegistry.indexOf(entry.getValue()));
                }

                if (updateMap.isEmpty()) continue;
                packets.add(new ServerUpdateChunkSectionBlockStatesPlayPacket(sectionPosition, updateMap));
            }

            for (
                    Map.Entry<ChunkRelativeBlockPosition, CompoundBinaryTag> update
                    : this.blockEntityUpdates.entrySet()
            ) {
                ChunkRelativeBlockPosition position = update.getKey();
                CompoundBinaryTag blockEntityData = update.getValue();

                JetChunk chunk = this.acquisition.getChunk(this.chunkPosition);
                if (!(chunk.blockState(position) instanceof JetBlockState blockState)) {
                    throw new IllegalArgumentException(String.format(
                            "Block state of a block at a position of %s is not a valid block state",
                            position
                    ));
                }

                Holder.Reference<BlockType> blockType = blockState.blockType();
                Key blockEntityTypeKey = null;

                // FIXME: Temporal solution, needs to be replaced with a proper block-entity system
                for (
                        JetMinecraftRegistry.RegistrationInfo<BlockEntityType> info
                        : blockEntityTypeRegistry.registrationInfos()
                ) {
                    List<Holder<BlockType>> validBlocks = info.value().validBlocks().contents(blockTypeRegistry);
                    if (!validBlocks.contains(blockType)) continue;
                    blockEntityTypeKey = info.key();
                    break;
                }

                if (blockEntityTypeKey == null) {
                    throw new IllegalArgumentException(String.format(
                            "Block type with key of %s cannot have a block entity",
                            blockType.key()
                    ));
                }

                packets.add(new ServerUpdateBlockEntityPlayPacket(
                        AbsolutePositionUtil.from(position, this.chunkPosition),
                        blockEntityTypeRegistry.indexOf(blockEntityTypeKey),
                        blockEntityData
                ));
            }

            if (!this.skyLightUpdates.isEmpty() || !this.blockLightUpdates.isEmpty()) {
                LightSectionList sectionList = this.acquisition.getChunk(this.chunkPosition).lightSectionList();
                LightSerializationData data = LightSerializationData.create(
                        this.skyLightUpdates.keySet(),
                        this.blockLightUpdates.keySet(),
                        sectionList,
                        this.acquisition.world()
                                .dimensionType()
                                .valueOrThrow(registryManager.registry(RegistryReference.DIMENSION_TYPE))
                );

                packets.add(new ServerUpdateLightPlayPacket(this.chunkPosition, data));
            }

            return packets;
        }

        /**
         * Gets a current state of {@linkplain JetChunk a chunk} that the update is being built for.
         *
         * @return the current state of the chunk
         * @since 1.0
         */
        private @NonNull JetChunk chunk() {
            return this.acquisition.getChunk(this.chunkPosition);
        }
    }
}