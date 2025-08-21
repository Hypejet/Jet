package net.hypejet.jet.entity;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.scoreboard.score.Score;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.UUID;

/**
 * A Minecraft entity.
 *
 * @since 1.0
 */
public interface Entity extends Identified, Pointered, HoverEventSource<HoverEvent.ShowEntity>, Keyed {
    /**
     * Gets the {@linkplain Key} of type of this {@linkplain Entity entity}.
     *
     * @return the entity type key
     * @since 1.0
     */
    @NonNull Key entityType();

    /**
     * Gets a numeric identifier of this {@linkplain Entity entity}, unique to
     * a {@linkplain MinecraftServer server} that this {@linkplain Entity entity} is in.
     *
     * @return the identifier
     * @since 1.0
     */
    int entityId();

    /**
     * Gets a {@linkplain UUID unique identifier} of this {@linkplain Entity entity}.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();

    /**
     * Gets current {@linkplain Position position} of this {@linkplain Entity entity}.
     *
     * @return the position where this entity currently is
     * @since 1.0
     */
    @NonNull Position position();

    /**
     * Gets a {@linkplain Vector} of current velocity of this {@linkplain Entity entity}.
     *
     * @return the velocity vector
     * @since 1.0
     */
    @NonNull Vector velocity();

    /**
     * Updates {@linkplain Position position} and velocity {@linkplain Vector vector}
     * of this {@linkplain Entity entity}.
     *
     * <p>Specifying a {@linkplain RelativeFlag relative flag} makes a value
     * associated with that flag relative to the current one.</p>
     *
     * @param position the position that the entity should have
     * @param velocity the velocity that the entity should have
     * @param flags the relative flags specifying which values of the specified position
     *              and velocity should be recognised as relative to current ones
     * @since 1.0
     */
    void updatePosition(@NonNull Position position, @NonNull Vector velocity,
                        @NonNull RelativeFlag @NonNull ... flags);

    /**
     * Updates {@linkplain Position position} and velocity {@linkplain Vector vector}
     * of this {@linkplain Entity entity}.
     *
     * <p>Specifying a {@linkplain RelativeFlag relative flag} makes a value
     * associated with that flag relative to the current one.</p>
     *
     * @param position the position that the entity should have
     * @param velocity the velocity that the entity should have
     * @param flags the relative flags specifying which values of the specified position
     *              and velocity should be recognised as relative to current ones
     * @since 1.0
     */
    void updatePosition(@NonNull Position position, @NonNull Vector velocity,
                        @NonNull Collection<RelativeFlag> flags);

    /**
     * Gets a {@linkplain World world} where this {@linkplain Entity entity} is.
     *
     * @return the world of the entity
     * @since 1.0
     */
    @NonNull World world();

    /**
     * Teleports this {@linkplain Entity entity} to the specified {@linkplain World world}.
     *
     * <p>The initial position of the entity is going
     * to be the {@linkplain World#defaultSpawnPosition() default spawn position}
     * of the world that the entity is being teleported to.</p>
     *
     * <p>Attributes and metadata of the entity are kept after the world change.</p>
     *
     * @param world the world that this entity should be teleported to
     * @since 1.0
     */
    void teleport(@NonNull World world);

    /**
     * Teleports this {@linkplain Entity entity} to the specified {@linkplain World world}.
     *
     * <p>Attributes and metadata of the entity are kept after the world change.</p>
     *
     * @param world the world that this entity should be teleported to
     * @param position an initial position where the entity should spawn after the world change
     * @since 1.0
     */
    void teleport(@NonNull World world, @NonNull Position position);

    /**
     * Teleports this {@linkplain Entity entity} to the specified {@linkplain World world}.
     *
     * @param world the world that this entity should be teleported to
     * @param position an initial position where the entity should spawn after the world change
     * @param keepAttributes whether attributes of the entity should be kept after the world change
     * @param keepMetadata whether metadata of the entity should be kept after the world change
     * @since 1.0
     */
    void teleport(@NonNull World world, @NonNull Position position, boolean keepAttributes, boolean keepMetadata);

    /**
     * Gets a name that this {@linkplain Entity entity} uses in {@linkplain Score score} management
     * of {@linkplain Scoreboard scoreboards}.
     *
     * @return the name
     * @since 1.0
     */
    @NonNull String scoreboardName();

    /**
     * Represents a hand of an entity.
     *
     * <p>Contents of this enum depend on Minecraft, however it is safe to keep it an enum, since it is very unlikely
     * to change.</p>
     *
     * @since 1.0
     */
    enum Hand {
        /**
         * A left hand of an entity.
         *
         * @since 1.0
         */
        LEFT,
        /**
         * A right hand of an entity.
         *
         * @since 1.0
         */
        RIGHT
    }
}