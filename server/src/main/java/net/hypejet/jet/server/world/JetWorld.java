package net.hypejet.jet.server.world;

import net.hypejet.jet.data.model.coordinate.Coordinate;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of the {@linkplain World world}.
 *
 * @since 1.0
 * @author Codestech
 * @see World
 */
public final class JetWorld implements World {

    private static final int CHUNK_EDGE_LENGTH = 16;

    private final UUID uniqueId;

    private final Set<JetEntity> entities = new HashSet<>();
    private final ReentrantReadWriteLock entitiesLock = new ReentrantReadWriteLock();

    private final Map<ChunkPosition, JetChunk> chunks = new HashMap<>();
    private final ReentrantReadWriteLock chunkLock = new ReentrantReadWriteLock();

    /**
     * Constructs the {@linkplain JetWorld world}.
     *
     * @param uniqueId a unique identifier of the world
     * @since 1.0
     */
    public JetWorld(@NonNull UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public @NonNull UUID uniqueId() {
        return this.uniqueId;
    }

    @Override
    public @NonNull Collection<Entity> entities() {
        this.entitiesLock.readLock().lock();
        try {
            return Set.copyOf(this.entities);
        } finally {
            this.entitiesLock.readLock().unlock();
        }
    }

    @Override
    public @Nullable Chunk chunkAt(@NonNull Coordinate<?> coordinate) {
        return this.chunkAt((int) Math.floor(coordinate.x() / CHUNK_EDGE_LENGTH),
                (int) Math.floor(coordinate.z() / CHUNK_EDGE_LENGTH));
    }

    @Override
    public @Nullable Chunk chunkAt(int chunkX, int chunkZ) {
        return this.chunkAt(new ChunkPosition(chunkX, chunkZ));
    }

    @Override
    public @Nullable Chunk chunkAt(@NonNull ChunkPosition position) {
        try {
            this.chunkLock.readLock().lock();
            return this.chunks.get(position);
        } finally {
            this.chunkLock.readLock().unlock();
        }
    }

    /**
     * Adds an entity to the world.
     *
     * @param entity the entity to add
     * @since 1.0
     */
    public void addEntity(@NonNull JetEntity entity) {
        this.entitiesLock.writeLock().lock();
        try {
            this.entities.add(entity);
        } finally {
            this.entitiesLock.writeLock().unlock();
        }
    }

    /**
     * Removes an entity from the world.
     *
     * @param entity the entity to remove
     * @since 1.0
     */
    public void removeEntity(@NonNull JetEntity entity) {
        this.entitiesLock.writeLock().lock();
        try {
            this.entities.remove(entity);
        } finally {
            this.entitiesLock.writeLock().unlock();
        }
    }
}