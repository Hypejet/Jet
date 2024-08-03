package net.hypejet.jet.server.world;

import net.hypejet.jet.world.World;
import net.hypejet.jet.world.WorldManager;
import org.checkerframework.checker.lock.qual.GuardedBy;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of the {@linkplain WorldManager world manager}.
 *
 * @since 1.0
 * @author Codestech
 * @see WorldManager
 */
public final class JetWorldManager implements WorldManager {

    private final @GuardedBy("worldsLock") Map<UUID, JetWorld> worlds = new HashMap<>();
    private final ReentrantReadWriteLock worldsLock = new ReentrantReadWriteLock();

    @Override
    public @NonNull World createWorld() {
        return this.createWorld(UUID.randomUUID());
    }

    @Override
    public @NonNull World createWorld(@NonNull UUID uniqueId) {
        JetWorld world = new JetWorld(uniqueId);
        this.worldsLock.writeLock().lock();

        try {
            if (this.worlds.containsKey(uniqueId))
                throw worldExistsException(uniqueId);
            this.worlds.put(uniqueId, world);
        } finally {
            this.worldsLock.writeLock().unlock();
        }

        return world;
    }

    @Override
    public void registerWorld(@NonNull World world) {
        if (!(world instanceof JetWorld jetWorld))
            throw new IllegalArgumentException("The world is not a valid Jet world");

        this.worldsLock.writeLock().lock();

        try {
            UUID uniqueId = jetWorld.uniqueId();

            if (this.worlds.containsKey(uniqueId))
                throw worldExistsException(uniqueId);

            this.worlds.put(uniqueId, jetWorld);
        } finally {
            this.worldsLock.writeLock().unlock();
        }
    }

    @Override
    public @Nullable World getWorld(@NonNull UUID uniqueId) {
        this.worldsLock.readLock().lock();

        try {
            return this.worlds.get(uniqueId);
        } finally {
            this.worldsLock.readLock().unlock();
        }
    }

    @Override
    public boolean unregisterWorld(@NonNull World world) {
        if (!(world instanceof JetWorld jetWorld))
            throw new IllegalArgumentException("The world is not a valid Jet world");

        this.worldsLock.writeLock().lock();

        try {
            return this.worlds.remove(world.uniqueId(), jetWorld);
        } finally {
            this.worldsLock.writeLock().unlock();
        }
    }

    @Override
    public @NonNull Collection<World> worlds() {
        this.worldsLock.readLock().lock();
        try {
            return Set.copyOf(this.worlds.values());
        } finally {
            this.worldsLock.readLock().unlock();
        }
    }

    private static @NonNull IllegalArgumentException worldExistsException(@NonNull UUID uniqueId) {
        return new IllegalArgumentException("A world with unique identifier of " + uniqueId + "has been already" +
                "registered");
    }
}