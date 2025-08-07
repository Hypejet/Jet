package net.hypejet.jet.server.world.chunk.factory;

import com.google.common.collect.Lists;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.block.JetBlockType;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.builder.JetChunkBuilder;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.factory.ChunkFactory;
import net.hypejet.jet.world.chunk.light.LightSection;
import net.hypejet.jet.world.chunk.section.ChunkSection;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.hypejet.jet.world.dimension.DimensionType;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an implementation of {@linkplain ChunkFactory a chunk factory}.
 *
 * @implSpec 1.0
 * @see ChunkFactory
 */
public final class JetChunkFactory implements ChunkFactory {

    private final JetMinecraftServer server;

    /**
     * Constructs the {@linkplain JetChunkFactory chunk factory implementation}.
     *
     * @param server a server that should own chunks created by the factory
     * @since 1.0
     */
    public JetChunkFactory(@NonNull JetMinecraftServer server) {
        this.server = Objects.requireNonNull(server, "server");
    }

    @Override
    public @NonNull JetChunk createChunk(@NonNull DimensionType dimensionType,
                                         @NonNull List<ChunkSection> chunkSections,
                                         @NonNull List<LightSection> lightSections,
                                         @NonNull Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities) {
        return JetChunk.create(
                dimensionType, validateChunkSections(chunkSections),
                validateLightSections(lightSections), blockEntities
        );
    }

    @Override
    public @NonNull JetChunkBuilder createChunkBuilder(@NonNull DimensionType dimensionType) {
        JetRegistryManager registryManager = this.server.registryManager();

        JetRegistryEntry<JetBlockType> blockType = registryManager.blockTypeRegistry().get(Blocks.AIR);
        if (blockType == null)
            throw new IllegalArgumentException("Could not find an air block type");

        JetRegistryEntry<Biome> biome = registryManager.biomeRegistry().get(Biomes.PLAINS);
        if (biome == null)
            throw new IllegalStateException("Could not find a plains biome");

        JetBlockState blockState = registryManager.blockStateRegistry().defaultBlockState(blockType);
        return this.createChunkBuilder(dimensionType, blockState, biome, null);
    }

    @Override
    public @NonNull JetChunkBuilder createChunkBuilder(
            @NonNull DimensionType dimensionType, @NonNull BlockState blockState,
            Holder.@NonNull Reference<Biome> defaultBiome, @Nullable CompoundBinaryTag defaultBlockEntity
    ) {
        Objects.requireNonNull(dimensionType, "dimension type");
        Objects.requireNonNull(blockState, "block state");
        Objects.requireNonNull(defaultBiome, "default biome");

        if (!(blockState instanceof JetBlockState validatedBlockState))
            throw new IllegalArgumentException("The block state specified is not a valid block state");

        JetRegistryManager registryManager = this.server.registryManager();
        return new JetChunkBuilder(
                dimensionType, registryManager.blockStateRegistry(),
                registryManager.biomeRegistry().elementOrder(),
                validatedBlockState, validatedBiome, defaultBlockEntity
        );
    }

    private static @NonNull List<JetChunkSection> validateChunkSections(@NonNull List<ChunkSection> chunkSections) {
        return Lists.transform(chunkSections, section -> {
            if (!(section instanceof JetChunkSection validatedSection))
                throw new IllegalArgumentException("The chunk section is not a valid chunk section");
            return validatedSection;
        });
    }

    private static @NonNull List<JetLightSection> validateLightSections(@NonNull List<LightSection> lightSections) {
        return Lists.transform(lightSections, section -> {
            if (!(section instanceof JetLightSection validatedSection))
                throw new IllegalArgumentException("The light section is not a valid chunk section");
            return validatedSection;
        });
    }
}