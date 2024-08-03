package net.hypejet.jet.server.world.chunk;

import net.hypejet.jet.protocol.packet.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.world.chunk.BlockEntity;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.chunk.ChunkSection;
import net.hypejet.jet.world.chunk.palette.SingleValuedPalette;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of a {@linkplain Chunk chunk}.
 *
 * @since 1.0
 * @author Codesstech
 * @see Chunk
 */
public final class JetChunk implements Chunk {

    private static final int CHUNK_SECTION_EDGE_LENGTH = 16;

    private static final ChunkSection EMPTY_CHUNK_SECTION = new ChunkSection((short) 0,
            new SingleValuedPalette(0),
            new SingleValuedPalette(0));

    private final BinaryTag heightmaps = CompoundBinaryTag.empty();

    private final int minSectionY;

    private final List<ChunkSection> sections;
    private final ReentrantReadWriteLock sectionLock = new ReentrantReadWriteLock();

    private final Set<BlockEntity> blockEntities = new HashSet<>();
    private final ReentrantReadWriteLock blockEntityLock = new ReentrantReadWriteLock();

    private final BitSet skylightMask = new BitSet();
    private final BitSet blockLightMask = new BitSet();
    private final BitSet emptySkylightMask = new BitSet();
    private final BitSet emptyBlockLightMask = new BitSet();

    private final byte[][] skylight = new byte[0][];
    private final byte[][] blockLight = new byte[0][];

    private final ReentrantReadWriteLock lightLock = new ReentrantReadWriteLock();

    /**
     * Constructs the {@linkplain JetChunk chunk}.
     *
     * @param height a height of the chunk
     * @param minY a minimum height of the chunk
     * @since 1.0
     */
    public JetChunk(int height, int minY) {
        this.minSectionY = minY / CHUNK_SECTION_EDGE_LENGTH;
        this.sections = new ArrayList<>(height / CHUNK_SECTION_EDGE_LENGTH);
        Collections.fill(this.sections, EMPTY_CHUNK_SECTION);
    }

    @Override
    public @NonNull Collection<ChunkSection> sections() {
        return List.copyOf(this.sections);
    }

    @Override
    public @NotNull ChunkSection sectionAt(int height) {
        return this.sections.get(this.sectionIndex(height));
    }

    @Override
    public @NonNull ServerChunkAndLightDataPlayPacket createChunkAndLightDataPacket(int chunkX, int chunkZ) {
        try {
            this.sectionLock.readLock().lock();
            this.blockEntityLock.readLock().lock();
            this.lightLock.readLock().lock();
            return new ServerChunkAndLightDataPlayPacket(chunkX, chunkZ, this.heightmaps, this.sections,
                    this.blockEntities, this.skylightMask, this.blockLightMask, this.emptySkylightMask,
                    this.emptyBlockLightMask, this.skylight, this.blockLight);
        } finally {
            this.lightLock.readLock().unlock();
            this.blockEntityLock.readLock().unlock();
            this.sectionLock.readLock().unlock();
        }
    }

    private int sectionIndex(int height) {
        int sectionY = height / CHUNK_SECTION_EDGE_LENGTH;
        if (sectionY < this.minSectionY)
            throw new IllegalArgumentException("The section Y is lower than a section Y of the lowest section");

        int sectionIndex = sectionY - this.minSectionY;
        if (this.sections.size() <= sectionIndex)
            throw new IllegalArgumentException("The section index is higher than a section count");

        return sectionIndex;
    }
}