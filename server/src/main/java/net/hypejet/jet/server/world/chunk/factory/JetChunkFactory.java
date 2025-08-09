package net.hypejet.jet.server.world.chunk.factory;

import com.google.common.collect.Lists;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.keys.BiomeKeys;
import net.hypejet.jet.registry.keys.BlockKeys;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.builder.JetChunkBuilder;
import net.hypejet.jet.server.world.chunk.factory.palette.JetChunkPaletteFactory;
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
 * An implementation of a {@linkplain ChunkFactory chunk factory}.
 *
 * @since 1.0
 * @see ChunkFactory
 */
public final class JetChunkFactory implements ChunkFactory {

    private final JetMinecraftServer server;
    private final JetChunkPaletteFactory<BlockState> blockStatePaletteFactory;
    private final JetChunkPaletteFactory<Holder.Reference<Biome>> biomePaletteFactory;

    /**
     * Constructs the {@linkplain JetChunkFactory chunk factory implementation}.
     *
     * @param server a server that should own chunks created by the factory
     * @param blockStatePaletteFactory a chunk-palette factory that chunk builders created by the constructed factory
     *                                 should use for block state chunk-palette creation
     * @param biomePaletteFactory a chunk-palette factory that chunk builders created by the constructed factory
     *                            should use for biome chunk-palette creation
     * @since 1.0
     */
    public JetChunkFactory(@NonNull JetMinecraftServer server,
                           @NonNull JetChunkPaletteFactory<BlockState> blockStatePaletteFactory,
                           @NonNull JetChunkPaletteFactory<Holder.Reference<Biome>> biomePaletteFactory) {
        this.server = Objects.requireNonNull(server, "server");
        this.blockStatePaletteFactory = Objects.requireNonNull(
                blockStatePaletteFactory,
                "block state palette factory"
        );
        this.biomePaletteFactory = Objects.requireNonNull(biomePaletteFactory, "biome palette factory");
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
        return this.createChunkBuilder(
                dimensionType,
                this.server.registryManager()
                        .blockStateRegistry()
                        .defaultBlockState(new Holder.Reference<>(BlockKeys.AIR)),
                new Holder.Reference<>(BiomeKeys.PLAINS),
                null
        );
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

        return new JetChunkBuilder(
                dimensionType,
                this.blockStatePaletteFactory,
                this.biomePaletteFactory,
                validatedBlockState,
                defaultBiome,
                defaultBlockEntity
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