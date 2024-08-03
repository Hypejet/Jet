package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.hypejet.jet.world.chunk.BlockEntity;
import net.hypejet.jet.world.chunk.ChunkSection;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which sets data of a chunk.
 *
 * @param chunkX a {@code chunk X} coordinate of the chunk
 * @param chunkZ a {@code chunk Z} coordinate of the chunk
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
 * @param skyLight an array of skylight data, whose contents are arrays for each bit set to true in the skylight mask,
 *                 starting with the lowest value, half a byte per light value
 * @param blockLight an array of block light data, whose contents are arrays for each bit set to true in the block
 *                   light mask starting with the lowest value, half a byte per light value
 * @since 1.0
 * @author Codestech
 * @see ChunkSection
 * @see BlockEntity
 */
public record ServerChunkAndLightDataPlayPacket(int chunkX, int chunkZ, @NonNull BinaryTag heightmaps,
                                                @NonNull Collection<ChunkSection> sections,
                                                @NonNull Collection<BlockEntity> blockEntities,
                                                @NonNull BitSet skyLightMask, @NonNull BitSet blockLightMask,
                                                @NonNull BitSet emptySkyLightMask, @NonNull BitSet emptyBlockLightMask,
                                                byte @NonNull [] @NonNull [] skyLight,
                                                byte @NonNull [] @NonNull [] blockLight) implements ServerPlayPacket {
    /**
     * Constructs the {@linkplain ServerChunkAndLightDataPlayPacket chunk and light data play packet}.
     *
     * <p>The light bitsets and light arrays with their contents are cloned during the construction to prevent
     * modifications on the record.</p>
     *
     * @param chunkX a {@code chunk X} coordinate of the chunk
     * @param chunkZ a {@code chunk Z} coordinate of the chunk
     * @param heightmaps heightmaps of the chunk
     * @param sections sections of the chunk and their data
     * @param blockEntities block entities of the chunk
     * @param skyLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                     a corresponding chunk section has data in a skylight array
     * @param blockLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                       a corresponding chunk section has data in a block light array
     * @param emptySkyLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                          a corresponding section has all zeros for its skylight data
     * @param emptyBlockLightMask a bitset containing bits for each section in the world + 2, which indicate that
     *                            a corresponding section has all zeros for its block light data
     * @param skyLight an array of skylight data, which contents are arrays for each bit set to true in the skylight
     *                 mask, starting with the lowest value, half a byte per light value
     * @param blockLight an array of block light data, which contents are arrays for each bit set to true in the block
     *                   light mask, starting with the lowest value, half a byte per light value
     * @since 1.0
     */
    public ServerChunkAndLightDataPlayPacket {
        Objects.requireNonNull(sections, "The sections must not be null");
        Objects.requireNonNull(blockEntities, "The block entities must not be null");
        Objects.requireNonNull(skyLightMask, "The skylight mask must not be null");
        Objects.requireNonNull(blockLightMask, "The block light mask must not be null");
        Objects.requireNonNull(skyLight, "The skylight must not be null");
        Objects.requireNonNull(blockLight, "The blockLight must not be null");

        sections = List.copyOf(sections);
        blockEntities = List.copyOf(blockEntities);
        skyLightMask = (BitSet) skyLightMask.clone();
        blockLightMask = (BitSet) blockLightMask.clone();
        emptySkyLightMask = (BitSet) emptyBlockLightMask.clone();
        emptyBlockLightMask = (BitSet) emptySkyLightMask.clone();
        skyLight = clone(skyLight);
        blockLight = clone(blockLight);
    }

    /**
     * Gets a bitset containing bits for each section in the world + {@code 2}, which indicate that a corresponding chunk
     * section has data in a skylight array.
     *
     * <p>The bitset returned is a clone to prevent modifications of the original bitset.</p>
     *
     * @return the bitset
     * @since 1.0
     */
    @Override
    public @NonNull BitSet skyLightMask() {
        return (BitSet) this.skyLightMask.clone();
    }

    /**
     * Gets a bitset containing bits for each section in the world + {@code 2}, which indicate that a corresponding chunk
     * section has data in a block light array.
     *
     * <p>The bitset returned is a clone to prevent modifications of the original bitset.</p>
     *
     * @return the bitset
     * @since 1.0
     */
    @Override
    public @NonNull BitSet blockLightMask() {
        return (BitSet) this.blockLightMask.clone();
    }

    /**
     * Gets a bitset containing bits for each section in the world + 2, which indicate that a corresponding section
     * has all zeros for its skylight data.
     *
     * <p>The bitset returned is a clone to prevent modifications of the original bitset.</p>
     *
     * @return the bitset
     * @since 1.0
     */
    @Override
    public @NonNull BitSet emptySkyLightMask() {
        return (BitSet) this.emptySkyLightMask.clone();
    }

    /**
     * Gets a bitset containing bits for each section in the world + 2, which indicate that a corresponding section
     * has all zeros for its block light data.
     *
     * <p>The bitset returned is a clone to prevent modifications of the original bitset.</p>
     *
     * @return the bitset
     * @since 1.0
     */
    @Override
    public @NonNull BitSet emptyBlockLightMask() {
        return (BitSet) this.emptyBlockLightMask.clone();
    }

    /**
     * Gets an array of skylight data, whose contents are arrays for each bit set to true in the skylight mask,
     * starting with the lowest value, half a byte per light value.
     *
     * <p>The array returned and its contents are clones to prevent modifications of the original arrays.</p>
     *
     * @return the array
     * @since 1.0
     */
    @Override
    public byte @NonNull [] @NonNull [] skyLight() {
        return clone(this.skyLight);
    }

    /**
     * Gets an array of block light data, whose contents are arrays for each bit set to true in the block light mask,
     * starting with the lowest value, half a byte per light value.
     *
     * <p>The array returned and its contents are clones to prevent modifications of the original arrays.</p>
     *
     * @return the array
     * @since 1.0
     */
    @Override
    public byte @NonNull [] @NonNull [] blockLight() {
        return clone(this.blockLight);
    }

    private static byte @NonNull [] @NonNull [] clone(byte @NonNull [] @NonNull [] array) {
        byte[][] clone = array.clone();
        for (int index = 0; index < clone.length; index++)
            clone[index] = Objects.requireNonNull(clone[index], "The array must not be null").clone();
        return clone;
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records natively do not compare contents of arrays
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ServerChunkAndLightDataPlayPacket packet)) return false;
        return this.chunkX == packet.chunkX
                && this.chunkZ == packet.chunkZ
                && Objects.equals(this.skyLightMask, packet.skyLightMask)
                && Objects.equals(this.heightmaps, packet.heightmaps)
                && Objects.equals(this.blockLightMask, packet.blockLightMask)
                && Objects.equals(this.emptySkyLightMask, packet.emptySkyLightMask)
                && Objects.equals(this.emptyBlockLightMask, packet.emptyBlockLightMask)
                && Objects.equals(this.sections, packet.sections)
                && Objects.equals(this.blockEntities, packet.blockEntities)
                && Objects.deepEquals(this.skyLight, packet.skyLight)
                && Objects.deepEquals(this.blockLight, packet.blockLight);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records natively do not compare contents of arrays
    @Override
    public int hashCode() {
        return Objects.hash(this.chunkX, this.chunkZ, this.heightmaps, this.sections, this.blockEntities,
                this.skyLightMask, this.blockLightMask, this.emptySkyLightMask, this.emptyBlockLightMask,
                Arrays.deepHashCode(this.skyLight), Arrays.deepHashCode(this.blockLight));
    }
}