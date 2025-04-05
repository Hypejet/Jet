package net.hypejet.jet.server.world.update;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBiomesPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBlockStatePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateChunkSectionBlockStatesPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateLightPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.server.util.coordinate.AbsolutePositionUtil;
import net.hypejet.jet.server.util.coordinate.ChunkPositionUtil;
import net.hypejet.jet.server.util.coordinate.ChunkRelativePositionUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.acquisition.worldmap.WriteWorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.block.BlockType;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.LightSectionList;
import net.hypejet.jet.server.world.chunk.light.LightSerializationData;
import net.hypejet.jet.server.world.chunk.light.LightType;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.server.world.chunk.update.BiomeUpdate;
import net.hypejet.jet.server.world.chunk.update.BlockUpdate;
import net.hypejet.jet.server.world.chunk.update.LightUpdate;
import net.hypejet.jet.server.world.chunk.view.ChunkView;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.server.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.server.world.coordinate.chunk.section.ChunkSectionPosition;
import net.hypejet.jet.server.world.handler.ChunkBatchHandler;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.coordinate.BiomePosition;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.hypejet.jet.world.update.WorldMapUpdate;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain WorldMapUpdate a world map update}.
 *
 * @since 1.0
 * @see WorldMapUpdate
 */
public final class JetWorldMapUpdate implements WorldMapUpdate {

    private final Multimap<ChunkPosition, BlockUpdate> blockUpdates = HashMultimap.create();
    private final Multimap<ChunkPosition, BiomeUpdate> biomeUpdates = HashMultimap.create();
    private final Multimap<ChunkPosition, LightUpdate> lightUpdates = HashMultimap.create();

    private final WriteWorldMapAcquisitionImpl acquisition;

    /**
     * Constructs the {@linkplain JetWorldMapUpdate world map update implementation}.
     *
     * @param acquisition a write acquisition of a world map that the update should be done for
     * @since 1.0
     */
    public JetWorldMapUpdate(@NonNull WriteWorldMapAcquisitionImpl acquisition) {
        this.acquisition = NullabilityUtil.requireNonNull(acquisition, "acquisition");
    }

    @Override
    public @NonNull WorldMapUpdate updateBlock(@NonNull BlockPosition position, @NonNull Key blockTypeKey) {
        return this.updateBlock(position, blockTypeKey, null, null);
    }

    @Override
    public @NonNull WorldMapUpdate updateBlock(@NonNull BlockPosition position, @NonNull Key blockTypeKey,
                                               @Nullable BlockEntity blockEntity) {
        return this.updateBlock(position, blockTypeKey, null, blockEntity);
    }

    @Override
    public @NonNull WorldMapUpdate updateBlock(@NonNull BlockPosition position, @NonNull Key blockTypeKey,
                                               @Nullable Map<String, String> properties) {
        return this.updateBlock(position, blockTypeKey, properties, null);
    }

    @Override
    public @NonNull WorldMapUpdate updateBlock(@NonNull BlockPosition position, @NonNull Key blockTypeKey,
                                               @Nullable Map<String, String> properties,
                                               @Nullable BlockEntity blockEntity) {
        JetRegistryManager registryManager = this.acquisition.world().server().registryManager();
        JetRegistryEntry<BlockType> blockTypeEntry = registryManager.blockTypeRegistry().get(blockTypeKey);

        if (blockTypeEntry == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block type with key of %s",
                    blockTypeKey
            ));
        }

        BlockType blockType = blockTypeEntry.value();
        BlockState blockState;

        if (properties == null) blockState = blockType.defaultState();
        else blockState = blockType.state(properties);

        ChunkPosition chunkPosition = ChunkPositionUtil.fromCoordinate(position);
        ChunkRelativeBlockPosition chunkRelativePosition = ChunkRelativePositionUtil.from(position);
        this.blockUpdates.put(chunkPosition, new BlockUpdate(chunkRelativePosition, blockState, blockEntity));

        return this;
    }

    @Override
    public @NonNull WorldMapUpdate updateBlockEntity(@NonNull BlockPosition position,
                                                     @Nullable BlockEntity blockEntity) {
        // TODO
        return this;
    }

    @Override
    public @NonNull WorldMapUpdate updateBiome(@NonNull BiomePosition position,
                                               @NonNull RegistryEntry<Biome> biome) {
        if (!(biome instanceof JetRegistryEntry<Biome> validatedBiome))
            throw new IllegalArgumentException("The biome registry entry specified is not a valid registry entry");

        ChunkPosition chunkPosition = ChunkPositionUtil.fromBiomePosition(position);
        ChunkRelativeBiomePosition chunkRelativePosition = ChunkRelativePositionUtil.from(position);

        this.biomeUpdates.put(chunkPosition, new BiomeUpdate(chunkRelativePosition, validatedBiome));
        return this;
    }

    @Override
    public @NonNull WorldMapUpdate updateSkyLightLevel(@NonNull BlockPosition position, byte level) {
        return this.updateLightLevel(position, level, LightType.SKY);
    }

    @Override
    public @NonNull WorldMapUpdate updateBlockLightLevel(@NonNull BlockPosition position, byte level) {
        return this.updateLightLevel(position, level, LightType.BLOCK);
    }

    @Override
    public void update() {
        Set<ChunkPosition> chunkPositions = new HashSet<>();

        chunkPositions.addAll(this.blockUpdates.keySet());
        chunkPositions.addAll(this.biomeUpdates.keySet());
        chunkPositions.addAll(this.lightUpdates.keySet());

        for (ChunkPosition chunkPosition : chunkPositions) {
            JetChunk chunk = this.acquisition.getChunk(chunkPosition);

            Collection<BlockUpdate> blockUpdates = this.blockUpdates.get(chunkPosition);
            Collection<BiomeUpdate> biomeUpdates = this.biomeUpdates.get(chunkPosition);
            Collection<LightUpdate> lightUpdates = this.lightUpdates.get(chunkPosition);

            // TODO: Remove updates that do not change anything

            chunk = chunk.withUpdates(blockUpdates, biomeUpdates, lightUpdates);
            this.acquisition.setChunk(chunkPosition, chunk);
        }

        try (CollectionAcquisition<JetEntity, ?> entitiesAcquisition = this.acquisition.world().entities()) {
            Map<ChunkView, ServerUpdateBiomesPlayPacket> biomeUpdatePackets = new HashMap<>(); // TODO: Frame
            Multimap<ChunkPosition, ServerPacket> updatePackets = ArrayListMultimap.create(); // TODO: Frame

            for (JetEntity entity : entitiesAcquisition.collection()) {
                if (!(entity instanceof JetPlayer player)) continue;
                ChunkBatchHandler chunkBatchHandler = player.chunkBatchHandler();

                try (NotNullObjectAcquisition<ChunkView> chunkViewAcquisition = chunkBatchHandler.chunkView()) {
                    ChunkView chunkView = chunkViewAcquisition.get();
                    for (int chunkX = chunkView.minimumChunkX(); chunkX <= chunkView.maximumChunkX(); chunkX++) {
                        for (int chunkZ = chunkView.minimumChunkZ(); chunkZ <= chunkView.maximumChunkZ(); chunkZ++) {
                            ChunkPosition chunkPosition = new ChunkPosition(chunkX, chunkZ);
                            if (!updatePackets.containsKey(chunkPosition))
                                updatePackets.putAll(chunkPosition, this.createUpdatePackets(chunkPosition));
                            // TODO: Do not send packets for chunks that have not been sent yet
                            updatePackets.get(chunkPosition).forEach(player::sendPacket);
                        }
                    }

                    ServerUpdateBiomesPlayPacket packet = biomeUpdatePackets.computeIfAbsent(
                            chunkView,
                            this::createBiomeUpdatePacket
                    );

                    if (packet == null) continue;
                    player.sendPacket(packet);
                }
            }
        }
    }

    private @NonNull List<ServerPacket> createUpdatePackets(@NonNull ChunkPosition chunkPosition) {
        Collection<BlockUpdate> blockUpdates = this.blockUpdates.get(chunkPosition);
        Collection<LightUpdate> lightUpdates = this.lightUpdates.get(chunkPosition);

        if (blockUpdates.isEmpty() && lightUpdates.isEmpty())
            return List.of();

        List<ServerPacket> updatePackets = new ArrayList<>();

        JetRegistryManager registryManager = this.acquisition.world().server().registryManager();
        BlockStateRegistry blockStateRegistry = registryManager.blockStateRegistry();
        ElementOrder<BlockState> blockStateOrder = blockStateRegistry.order();

        Multimap<ChunkSectionPosition, BlockUpdate> sectionToBlockUpdateMap = HashMultimap.create();
        for (BlockUpdate blockUpdate : blockUpdates) {
            sectionToBlockUpdateMap.put(
                    ChunkSectionPosition.from(
                            chunkPosition,
                            blockUpdate.position().absoluteY(),
                            ChunkPaletteType.BLOCK_STATE
                    ),
                    blockUpdate
            );
        }

        for (ChunkSectionPosition sectionPosition : sectionToBlockUpdateMap.keySet()) {
            Collection<BlockUpdate> updates = sectionToBlockUpdateMap.get(sectionPosition);
            if (updates.size() == 1) {
                BlockUpdate update = updates.iterator().next();

                BlockPosition blockPosition = AbsolutePositionUtil.from(update.position(), chunkPosition);
                int blockStateIdentifier = blockStateOrder.identifierOf(update.blockState());

                updatePackets.add(new ServerUpdateBlockStatePlayPacket(blockPosition, blockStateIdentifier));
                continue;
            }

            Object2IntMap<ChunkPaletteRelativePosition> updateMap = new Object2IntOpenHashMap<>();
            for (BlockUpdate update : updates) {
                ChunkRelativeBlockPosition position = update.position();
                ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);
                updateMap.put(palettePosition, blockStateOrder.identifierOf(update.blockState()));
            }

            if (updateMap.isEmpty()) continue;
            updatePackets.add(new ServerUpdateChunkSectionBlockStatesPlayPacket(sectionPosition, updateMap));
        }

        // TODO: Update block entities

        if (!lightUpdates.isEmpty()) {
            DimensionType dimensionType = this.acquisition.world().dimensionType().value();
            LightSectionList sectionList = this.acquisition.getChunk(chunkPosition).lightSectionList();
            LightSerializationData data = LightSerializationData.create(lightUpdates, sectionList, dimensionType);
            updatePackets.add(new ServerUpdateLightPlayPacket(chunkPosition, data));
        }

        return updatePackets;
    }

    private @Nullable ServerUpdateBiomesPlayPacket createBiomeUpdatePacket(@NonNull ChunkView chunkView) {
        Set<ServerUpdateBiomesPlayPacket.BiomeData> biomeData = new HashSet<>();
        for (ChunkPosition chunkPosition : this.biomeUpdates.keySet()) {
            if (!chunkView.isInView(chunkPosition)) continue;
            JetChunk chunk = this.acquisition.getChunk(chunkPosition);

            List<AbstractChunkPalette<RegistryEntry<Biome>>> palettes = new ArrayList<>();
            for (JetChunkSection section : chunk.sections())
                palettes.add(section.biomePalette());

            biomeData.add(new ServerUpdateBiomesPlayPacket.BiomeData(chunkPosition, palettes));
        }

        if (biomeData.isEmpty()) return null;
        return new ServerUpdateBiomesPlayPacket(biomeData);
    }

    private @NonNull WorldMapUpdate updateLightLevel(@NonNull BlockPosition position, byte level,
                                                     @NonNull LightType lightType) {
        ChunkPosition chunkPosition = ChunkPositionUtil.fromCoordinate(position);
        ChunkRelativeBlockPosition chunkRelativePosition = ChunkRelativePositionUtil.from(position);
        this.lightUpdates.put(chunkPosition, new LightUpdate(chunkRelativePosition, level, lightType));
        return this;
    }
}