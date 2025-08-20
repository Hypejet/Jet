package net.hypejet.jet.entity;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.acquisition.world.WriteEntityWorldAcquisition;
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
import org.checkerframework.checker.nullness.qual.NonNull;

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
     * Creates {@linkplain NotNullObjectAcquisition a not-null object acquisition} of {@linkplain World a world}
     * of this {@linkplain Entity entity}.
     *
     * @return the not-null object acquisition
     * @since 1.0
     */
    @NonNull NotNullObjectAcquisition<World> acquireWorldRead();

    /**
     * Creates {@linkplain WriteEntityWorldAcquisition a write entity world acquisition} of {@linkplain World a world}
     * of this {@linkplain Entity entity}.
     *
     * @return the write entity world acquisition
     * @since 1.0
     */
    @NonNull WriteEntityWorldAcquisition acquireWorldWrite();

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