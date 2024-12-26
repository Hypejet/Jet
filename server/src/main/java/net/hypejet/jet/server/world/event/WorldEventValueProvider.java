package net.hypejet.jet.server.world.event;

import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.world.event.WorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that provides necessary values to write
 * {@linkplain ServerWorldEventPlayPacket a server world event play packet}.
 *
 * @param <WE> a type of world event for which the values should be provided
 * @since 1.0
 * @see WorldEvent
 * @see ServerWorldEventPlayPacket
 */
public class WorldEventValueProvider<WE extends WorldEvent> {

    private final byte identifier;

    /**
     * Constructs the {@linkplain WorldEventValueProvider world event value provider}.
     *
     * @param identifier an identifier of the world event that the provider should provide values for
     * @since 1.0
     */
    public WorldEventValueProvider(byte identifier) {
        this.identifier = identifier;
    }

    /**
     * Gets an identifier of the world event that this provider provides values for.
     *
     * @return the identifier
     * @since 1.0
     */
    public final byte identifier() {
        return this.identifier;
    }

    /**
     * Gets a float representation of values of the world event specified.
     *
     * @param worldEvent the world event
     * @return the float representation
     * @since 1.0
     */
    public float value(@NonNull WE worldEvent) {
        return 0F;
    }
}