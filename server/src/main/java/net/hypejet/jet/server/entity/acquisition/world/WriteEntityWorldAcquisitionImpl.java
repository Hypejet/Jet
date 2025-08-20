package net.hypejet.jet.server.entity.acquisition.world;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.notnull.WriteNotNullObjectAcquisition;
import net.hypejet.jet.entity.acquisition.world.WriteEntityWorldAcquisition;
import net.hypejet.jet.event.events.world.PreWorldSwitchEvent;
import net.hypejet.jet.event.events.world.WorldSwitchEvent;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.handler.ChunkBatchHandler;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain EntityWorldAcquisition an entity world acquisition}
 * and {@linkplain WriteEntityWorldAcquisition a write entity world acquisition}.
 *
 * @since 1.0
 * @see EntityWorldAcquisition
 * @see WriteEntityWorldAcquisition
 */
public final class WriteEntityWorldAcquisitionImpl
        extends EntityWorldAcquisition<WriteNotNullObjectAcquisition<JetWorld>>
        implements WriteEntityWorldAcquisition {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteEntityWorldAcquisitionImpl.class);
    private final JetEntity entity;

    /**
     * Constructs the {@linkplain WriteEntityWorldAcquisitionImpl write entity world acquisition implementation}.
     *
     * @param worldAcquisition a world acquisition that the entity world acquisition should wrap
     * @param entity the entity
     * @since 1.0
     */
    public WriteEntityWorldAcquisitionImpl(@NonNull WriteNotNullObjectAcquisition<JetWorld> worldAcquisition,
                                           @NonNull JetEntity entity) {
        super(worldAcquisition);
        this.entity = Objects.requireNonNull(entity, "entity");
    }

    @Override
    public void set(@NotNull World value) {
        try (NotNullObjectAcquisition<Position> acquisition = value.acquireDefaultSpawnPositionRead()) {
            this.set(value, acquisition.get());
        }
    }

    @Override
    public void set(@NonNull World world, @NonNull Position position) {
        this.set(world, position, true, true);
    }

    @Override
    public void set(@NonNull World world, @NonNull Position position,
                    boolean keepAttributes, boolean keepMetadata) {
        if (!(world instanceof JetWorld validatedWorld))
            throw new IllegalArgumentException("The world specified is not a valid world");

        JetWorld previousWorld = this.acquisition.get();
        if (this.entity instanceof JetPlayer player) {
            ChunkBatchHandler chunkBatchHandler = player.chunkBatchHandler();

            chunkBatchHandler.cancelTask();
            previousWorld.removePlayer(player);

            PreWorldSwitchEvent preSwitchEvent = new PreWorldSwitchEvent(player, previousWorld, world, position);
            player.server().eventNode().call(preSwitchEvent);

            World newWorld = preSwitchEvent.getNewWorld();
            if (newWorld != validatedWorld) {
                if (newWorld instanceof JetWorld validatedNewWorld) {
                    validatedWorld = validatedNewWorld;
                } else {
                    LOGGER.warn("An invalid world has been specified in a pre-world-switch event,"
                            + " falling back to a world specified as a method argument");
                }
            }

            position = preSwitchEvent.getStartingPosition();

            player.sendRespawnPacket(validatedWorld, keepAttributes, keepMetadata);
            validatedWorld.addPlayer(player);
            chunkBatchHandler.scheduleTask();
        }

        /* We are going to synchronize the position manually using
           the movement handler if the entity is a player. */
        this.entity.setPosition(position);

        if (this.entity instanceof JetPlayer player) {
            player.movementHandler().synchronize(position, Vector.zero(), Set.of()); // TODO: Ensure integrity with vanilla
            player.server().eventNode().call(new WorldSwitchEvent(player, previousWorld, world, position));
        }

        this.acquisition.set(validatedWorld);
    }
}