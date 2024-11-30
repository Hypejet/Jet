package net.hypejet.jet.network.packet.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.hypejet.jet.util.bitset.UnmodifiableBitSet;
import net.hypejet.jet.world.chunk.BlockEntity;
import net.hypejet.jet.world.chunk.ChunkSection;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

/**
 * Represents {@linkplain ServerPacket a server packet}, which sets block and light of a chunk.
 *
 * @param chunkX a {@code chunk X} coordinate value of the chunk
 * @param chunkZ a {@code chunk Z} coordinate value of the chunk
 * @param heightmaps heightmaps of the chunk
 * @param sections sections of the chunk and their data
 * @param blockEntities block entities of the chunk
 * @param skyLightMask a bitset containing bits for each section in the world + 2, which indicate that a corresponding
 *                     chunk section has data in the skylight array below
 * @param blockLightMask a bitset containing bits for each section in the world + 2, which indicate that a corresponding
 *                       chunk section has data in the block light array below
 * @param emptySkyLightMask a bitset containing bits for each section in the world + 2, which indicate that
 *                          a corresponding section has all zeros for its skylight data
 * @param emptyBlockLightMask a bitset containing bits for each section in the world + 2, which indicate that
 *                            a corresponding section has all zeros for its block light data
 * @param skyLight a list of skylight data, whose contents are arrays for each bit set to true in the skylight mask,
 *                 starting with the lowest value, half a byte per light value
 * @param blockLight a list of block light data, whose contents are arrays for each bit set to true in the block
 *                   light mask, starting with the lowest value, half a byte per light value
 * @since 1.0
 * @author Codestech
 * @see ChunkSection
 * @see BlockEntity
 */
public record ServerChunkAndLightDataPlayPacket(
        int chunkX, int chunkZ, @NonNull BinaryTag heightmaps, @NonNull List<ChunkSection> sections,
        @NonNull Collection<BlockEntity> blockEntities, @NonNull UnmodifiableBitSet skyLightMask,
        @NonNull UnmodifiableBitSet blockLightMask, @NonNull UnmodifiableBitSet emptySkyLightMask,
        @NonNull UnmodifiableBitSet emptyBlockLightMask, @NonNull List<UnmodifiableByteArray> skyLight,
        @NonNull List<UnmodifiableByteArray> blockLight
) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerChunkAndLightDataPlayPacket server chunk and light data play packet}.
     *
     * @param chunkX a {@code chunk X} coordinate value of the chunk
     * @param chunkZ a {@code chunk Z} coordinate value of the chunk
     * @param heightmaps heightmaps of the chunk
     * @param sections sections of the chunk and their data
     * @param blockEntities block entities of the chunk
     * @param skyLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                     a corresponding chunk section has data in the skylight array below
     * @param blockLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                       a corresponding chunk section has data in the block light array below
     * @param emptySkyLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                          a corresponding section has all zeros for its skylight data
     * @param emptyBlockLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                            a corresponding section has all zeros for its block light data
     * @param skyLight a list of skylight data, whose contents are arrays for each bit set to true in the skylight
     *                 mask, starting with the lowest value, half a byte per light value
     * @param blockLight a list of block light data, whose contents are arrays for each bit set to true in the block
     *                   light mask, starting with the lowest value, half a byte per light value
     * @since 1.0
     */
    public ServerChunkAndLightDataPlayPacket(
            int chunkX, int chunkZ, @NonNull BinaryTag heightmaps, @NonNull List<ChunkSection> sections,
            @NonNull Collection<BlockEntity> blockEntities, @NonNull BitSet skyLightMask,
            @NonNull BitSet blockLightMask, @NonNull BitSet emptySkyLightMask, @NonNull BitSet emptyBlockLightMask,
            byte @NonNull [] @NonNull [] skyLight, byte @NonNull [] @NonNull [] blockLight
    ) {
        this(chunkX, chunkZ, heightmaps, sections, blockEntities, new UnmodifiableBitSet(skyLightMask),
                new UnmodifiableBitSet(blockLightMask), new UnmodifiableBitSet(emptySkyLightMask),
                new UnmodifiableBitSet(emptyBlockLightMask), toList(skyLight), toList(blockLight));
    }

    /**
     * Constructs the {@linkplain ServerChunkAndLightDataPlayPacket server chunk and light data play packet}.
     *
     * @param chunkX a {@code chunk X} coordinate value of the chunk
     * @param chunkZ a {@code chunk Z} coordinate value of the chunk
     * @param heightmaps heightmaps of the chunk
     * @param sections sections of the chunk and their data
     * @param blockEntities block entities of the chunk
     * @param skyLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                     a corresponding chunk section has data in the skylight array below
     * @param blockLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                       a corresponding chunk section has data in the block light array below
     * @param emptySkyLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                          a corresponding section has all zeros for its skylight data
     * @param emptyBlockLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                            a corresponding section has all zeros for its block light data
     * @param skyLight a list of skylight data, whose contents are arrays for each bit set to true in the skylight
     *                 mask, starting with the lowest value, half a byte per light value
     * @param blockLight a list of block light data, whose contents are arrays for each bit set to true in the block
     *                   light mask, starting with the lowest value, half a byte per light value
     * @since 1.0
     */
    public ServerChunkAndLightDataPlayPacket {
        NullabilityUtil.requireNonNull(heightmaps, "heightmaps");
        sections = List.copyOf(NullabilityUtil.requireNonNull(sections, "sections"));
        blockEntities = List.copyOf(NullabilityUtil.requireNonNull(blockEntities, "block entities"));

        NullabilityUtil.requireNonNull(skyLightMask, "skylight mask");
        NullabilityUtil.requireNonNull(blockLightMask, "block light mask");
        NullabilityUtil.requireNonNull(emptySkyLightMask, "empty skylight mask");
        NullabilityUtil.requireNonNull(emptyBlockLightMask, "empty block light mask");

        NullabilityUtil.requireNonNull(skyLight, "skylight");
        NullabilityUtil.requireNonNull(blockLight, "block light");
    }

    private static @NonNull List<UnmodifiableByteArray> toList(byte @NonNull [] @NonNull [] arrayOfByteArray) {
        List<UnmodifiableByteArray> list = new ArrayList<>();
        for (int index = 0; index < arrayOfByteArray.length; index++)
            list.add(index, new UnmodifiableByteArray(arrayOfByteArray[index]));
        return List.copyOf(list);
    }
}