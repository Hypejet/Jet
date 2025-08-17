package net.hypejet.jet.server.entity.player;

import net.hypejet.jet.entity.movement.acquisition.MovementAcquisition;
import net.hypejet.jet.event.events.world.InitialSpawnEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.acquisition.world.EntityWorldAcquisition;
import net.hypejet.jet.server.tick.Ticker;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A list of {@linkplain JetPlayer players} connected to the {@linkplain JetMinecraftServer server}.
 *
 * @since 1.0
 * @see JetMinecraftServer
 */
public final class PlayerList {

    private final EventNode<Object> eventNode;
    private final Ticker ticker;

    private final Set<JetPlayer> players = ConcurrentHashMap.newKeySet();

    /**
     * Constructs the {@linkplain PlayerList player list}.
     *
     * @param eventNode an event node of the server that the player list is being constructed for
     * @param ticker a ticker of the server that the player list is being constructed for
     * @since 1.0
     */
    public PlayerList(@NonNull EventNode<Object> eventNode, @NonNull Ticker ticker) {
        this.eventNode = Objects.requireNonNull(eventNode, "event node");
        this.ticker = Objects.requireNonNull(ticker, "ticker");
    }

    /**
     * Gets a copy of a {@linkplain Set set} of {@linkplain JetPlayer players} currently connected
     * to the {@linkplain JetMinecraftServer server} associated with this {@linkplain PlayerList player list}.
     *
     * @return the player set
     * @since 1.0
     */
    public @NonNull Set<JetPlayer> players() {
        return Set.copyOf(this.players);
    }

    /**
     * Registers the specified {@linkplain JetPlayer player} to this {@linkplain PlayerList player list}.
     *
     * @param player the player to register
     * @since 1.0
     */
    public void registerPlayer(@NonNull JetPlayer player) {
        this.ticker.ensureRunsInTickLoop();

        /* A call outside the event loop is safe in this case. When a player gets disconnected, the unregister method
           is going to be called and that method also runs in a tick loop. It means that the unregister method
           cannot be called until this method stops running, therefore no race conditions should happen. */
        if (!player.connection().isActive()) return;

        player.sendJoinGamePacket();
        // TODO: Difficulty packets, ability packets, held slot packets, etc.
        player.getScoreboard().addViewer(player);

        try (
                MovementAcquisition movementAcquisition = player.acquireMovementRead();
                EntityWorldAcquisition<?> worldAcquisition = player.acquireWorldRead()
        ) {
            Position position = movementAcquisition.position();
            player.movementHandler().synchronize(position, Vector.zero(), Set.of());

            this.players.add(player);

            // TODO: Send other world data
            player.chunkBatchHandler().scheduleTask(); // TODO: Ensure that it produces the same behaviour as vanilla

            JetWorld world = worldAcquisition.get();
            world.addPlayer(player);

            this.eventNode.call(new InitialSpawnEvent(player, world, position));
        }
    }

    /**
     * Unregisters the specified {@linkplain JetPlayer player} from this {@linkplain PlayerList player list}.
     *
     * @param player the player to unregister
     * @since 1.0
     */
    public void unregisterPlayer(@NonNull JetPlayer player) {
        this.ticker.ensureRunsInTickLoop();
        this.players.remove(player);
    }
}