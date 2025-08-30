package net.hypejet.jet.server.world;

import net.hypejet.concurrency.map.MapAcquirable;
import net.hypejet.concurrency.map.hashmap.HashMapAcquirable;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.acquisition.worldmap.WorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.acquisition.worldmap.WriteWorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.chunk.ChunkLoader;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.data.WorldData;
import net.hypejet.jet.world.dimension.DimensionType;
import net.hypejet.jet.world.event.world.events.StartWaitingForWorldChunksWorldEvent;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of the {@linkplain World world}.
 *
 * @since 1.0
 * @see World
 */
@NullMarked
public final class JetWorld implements World {

    private final Holder.Reference<DimensionType> dimensionType;
    private final WorldData worldData;

    private final ChunkLoader chunkLoader;
    private final JetRegistryManager registryManager;

    private final MapAcquirable<ChunkPosition, JetChunk, ?> chunks = new HashMapAcquirable<>();
    private final Set<JetPlayer> players = ConcurrentHashMap.newKeySet();

    private Position defaultSpawnPosition = Position.zero();

    /**
     * Constructs the {@linkplain JetWorld world}.
     *
     * @param dimensionType a holder referencing to a dimension type, of which type the world should be
     * @param worldData an additional world data that the world should have
     * @param chunkLoader a chunk loader that should be used for loading and saving chunks of the world
     * @param registryManager a registry manager of the server that the world is being constructed for
     * @since 1.0
     */
    public JetWorld(Holder.Reference<DimensionType> dimensionType, WorldData worldData,
                    ChunkLoader chunkLoader, JetRegistryManager registryManager) {
        this.dimensionType = Objects.requireNonNull(dimensionType, "dimension type");
        this.worldData = Objects.requireNonNull(worldData, "world data");
        this.chunkLoader = Objects.requireNonNull(chunkLoader, "chunk loader");
        this.registryManager = Objects.requireNonNull(registryManager, "registry manager");
    }

    @Override
    public Holder.Reference<DimensionType> dimensionType() {
        return this.dimensionType;
    }

    @Override
    public WorldData worldData() {
        return this.worldData;
    }

    @Override
    public Position defaultSpawnPosition() {
        return this.defaultSpawnPosition;
    }

    @Override
    public void defaultSpawnPosition(Position position) {
        this.defaultSpawnPosition = Objects.requireNonNull(position, "position");
    }

    @Override
    public WorldMapAcquisitionImpl acquireWorldMapRead() {
        return new WorldMapAcquisitionImpl(this, this.chunks.acquireRead());
    }

    @Override
    public WriteWorldMapAcquisitionImpl acquireWorldMapWrite() {
        return new WriteWorldMapAcquisitionImpl(this, this.chunks.acquireWrite());
    }

    @Override
    public Set<JetPlayer> players() {
        return Set.copyOf(this.players);
    }

    /**
     * Gets {@linkplain ChunkLoader a chunk loader}, which should be used for loading and saving
     * {@linkplain net.hypejet.jet.world.chunk.Chunk chunks} of this world.
     *
     * @return the chunk provider
     * @since 1.0
     */
    public ChunkLoader chunkLoader() {
        return this.chunkLoader;
    }

    /**
     * Gets a {@linkplain JetRegistryManager registry manager} of the {@linkplain JetMinecraftServer server}
     * that this {@linkplain JetWorld world} was created for.
     *
     * @return the registry manager
     * @since 1.0
     */
    public JetRegistryManager registryManager() {
        return this.registryManager;
    }

    /**
     * Adds the specified {@linkplain JetPlayer player} to this {@linkplain JetWorld world}.
     *
     * @param player the player to add to this world
     * @throws IllegalArgumentException if the specified player has already been added to this world
     * @since 1.0
     */
    public void addPlayer(JetPlayer player) {
        player.server().ticker().ensureRunsInTickLoop();
        if (!this.players.add(player))
            throw new IllegalArgumentException("The specified player has already been added to this world");
        player.sendPacket(new ServerWorldEventPlayPacket(StartWaitingForWorldChunksWorldEvent.INSTANCE));
    }

    /**
     * Removes the specified {@linkplain JetPlayer player} from this {@linkplain JetWorld world}.
     *
     * @param player the player to remove from this world
     * @throws IllegalArgumentException if the specified player has not been previously added to this world
     * @since 1.0
     */
    public void removePlayer(JetPlayer player) {
        player.server().ticker().ensureRunsInTickLoop();
        if (!this.players.remove(player))
            throw new IllegalArgumentException("The specified entity has not been added to this world");
    }
}