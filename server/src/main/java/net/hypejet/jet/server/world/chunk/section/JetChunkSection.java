package net.hypejet.jet.server.world.chunk.section;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.section.ChunkSection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents an implementation of {@linkplain ChunkSection a chunk section}.
 *
 * @since 1.0
 * @see ChunkSection
 */
public final class JetChunkSection implements ChunkSection {

    private final JetMinecraftServer server;
    private final short nonAirBlockCount;

    private final AbstractChunkPalette<BlockState> blockStatePalette;
    private final AbstractChunkPalette<RegistryEntry<Biome>> biomePalette;

    /**
     * Constructs the {@linkplain JetChunkSection chunk section implementation}.
     *
     * @param server a server that the chunk section should belong to
     * @param blockStatePalette a chunk palette of block states of the chunk section
     * @param biomePalette a chunk palette of biomes of the chunk section
     * @throws IllegalArgumentException if usage types of palettes specified are invalid
     * @since 1.0
     */
    public JetChunkSection(@NonNull JetMinecraftServer server,
                           @NonNull AbstractChunkPalette<BlockState> blockStatePalette,
                           @NonNull AbstractChunkPalette<RegistryEntry<Biome>> biomePalette) {
        this(server, calculateNonAirBlockCount(blockStatePalette), blockStatePalette, biomePalette);
    }

    /**
     * Constructs the {@linkplain JetChunkSection chunk section implementation}.
     *
     * @param server a server that the chunk section should belong to
     * @param nonAirBlockStateCount a count of non-air block states of the chunk section
     * @param blockStatePalette a chunk palette of block states of the chunk section
     * @param biomePalette a chunk palette of biomes of the chunk section
     * @throws IllegalArgumentException if usage types of palettes specified are invalid
     * @since 1.0
     */
    private JetChunkSection(@NonNull JetMinecraftServer server, short nonAirBlockStateCount,
                            @NonNull AbstractChunkPalette<BlockState> blockStatePalette,
                            @NonNull AbstractChunkPalette<RegistryEntry<Biome>> biomePalette) {
        this.server = NullabilityUtil.requireNonNull(server, "server");
        NullabilityUtil.requireNonNull(blockStatePalette, "block state palette");
        NullabilityUtil.requireNonNull(biomePalette, "biome palette");

        if (blockStatePalette.type() != ChunkPaletteType.BLOCK_STATE)
            throw new IllegalArgumentException("Type of the block state palette is not a block state palette type");
        if (biomePalette.type() != ChunkPaletteType.BIOME)
            throw new IllegalArgumentException("Type of the biome palette is not a biome palette type");

        this.blockStatePalette = blockStatePalette;
        this.biomePalette = biomePalette;
        this.nonAirBlockCount = nonAirBlockStateCount;
    }

    /**
     * Gets a chunk palette of block states of this chunk section.
     *
     * @return the chunk palette
     * @since 1.0
     */
    @Override
    public @NonNull AbstractChunkPalette<BlockState> blockStatePalette() {
        return this.blockStatePalette;
    }

    /**
     * Gets a chunk palette of biomes of this chunk section.
     *
     * @return the chunk palette
     * @since 1.0
     */
    @Override
    public @NonNull AbstractChunkPalette<RegistryEntry<Biome>> biomePalette() {
        return this.biomePalette;
    }

    /**
     * Gets a count of non-air block states of this chunk section.
     *
     * @return the count
     * @since 1.0
     */
    public short nonAirBlockCount() {
        return this.nonAirBlockCount;
    }

    /**
     * Creates a new {@linkplain JetChunkSection chunk section}, which is a copy of this chunk section and uses a new
     * block state chunk palette and a new biome chunk palette, which were created by applying updates specified.
     *
     * @param blockStateUpdates updates that should be applied to the block state chunk palette
     * @param biomeUpdates updates that should be applied to the biome chunk palette
     * @return the chunk section
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull JetChunkSection withUpdates(
            @NonNull Collection<ChunkPaletteUpdate<BlockState>> blockStateUpdates,
            @NonNull Collection<ChunkPaletteUpdate<RegistryEntry<Biome>>> biomeUpdates
    ) {
        if (blockStateUpdates.isEmpty() && biomeUpdates.isEmpty())
            return this;

        AbstractChunkPalette<BlockState> blockStatePalette = this.blockStatePalette.withUpdates(blockStateUpdates);
        AbstractChunkPalette<RegistryEntry<Biome>> biomePalette = this.biomePalette.withUpdates(biomeUpdates);

        boolean blockStatePaletteUnchanged = this.blockStatePalette.equals(blockStatePalette);
        if (blockStatePaletteUnchanged && this.biomePalette.equals(biomePalette))
            return this;

        if (blockStatePaletteUnchanged)
            return new JetChunkSection(this.server, this.nonAirBlockCount, blockStatePalette, biomePalette);
        return new JetChunkSection(this.server, blockStatePalette, biomePalette);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JetChunkSection otherSection)) return false;
        return this.nonAirBlockCount == otherSection.nonAirBlockCount
                && Objects.equals(this.blockStatePalette, otherSection.blockStatePalette)
                && Objects.equals(this.biomePalette, otherSection.biomePalette);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nonAirBlockCount, this.blockStatePalette, this.biomePalette);
    }

    @Override
    public String toString() {
        return "ChunkSection{" +
                "nonAirBlockCount=" + this.nonAirBlockCount +
                ", blockStatePalette=" + this.blockStatePalette +
                ", biomePalette=" + this.biomePalette +
                '}';
    }

    private static short calculateNonAirBlockCount(@NonNull AbstractChunkPalette<BlockState> blockStatePalette) {
        NullabilityUtil.requireNonNull(blockStatePalette, "block state palette");
        short nonAirBlockCount = 0;

        for (Object2ShortMap.Entry<BlockState> entry : blockStatePalette.elementCountMap().object2ShortEntrySet()) {
            BlockState blockState = entry.getKey();
            if (!(blockState instanceof JetBlockState validatedBlockState))
                throw new IllegalArgumentException("The block state is not a valid block state");

            if (validatedBlockState.isAir()) continue;
            nonAirBlockCount += entry.getShortValue();
        }

        return nonAirBlockCount;
    }
}