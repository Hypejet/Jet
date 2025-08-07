package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import java.util.Objects;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.server.world.event.WorldEventValueProvider;
import net.hypejet.jet.server.world.event.events.ChangeGameModeWorldEventValueProvider;
import net.hypejet.jet.server.world.event.events.DemoWorldEventValueProvider;
import net.hypejet.jet.server.world.event.events.EnableLimitedCraftingWorldEventValueProvider;
import net.hypejet.jet.server.world.event.events.EnableRespawnScreenWorldEventValueProvider;
import net.hypejet.jet.server.world.event.events.RainLevelChangeWorldEventValueProvider;
import net.hypejet.jet.server.world.event.events.ThunderLevelChangeWorldEventValueProvider;
import net.hypejet.jet.server.world.event.events.WinWorldEventValueProvider;
import net.hypejet.jet.world.event.world.WorldEvent;
import net.hypejet.jet.world.event.world.events.ArrowHitPlayerWorldEvent;
import net.hypejet.jet.world.event.world.events.BeginRainingWorldEvent;
import net.hypejet.jet.world.event.world.events.ChangeGameModeWorldEvent;
import net.hypejet.jet.world.event.world.events.DemoWorldEvent;
import net.hypejet.jet.world.event.world.events.EnableLimitedCraftingWorldEvent;
import net.hypejet.jet.world.event.world.events.EnableRespawnScreenWorldEvent;
import net.hypejet.jet.world.event.world.events.EndRainingWorldEvent;
import net.hypejet.jet.world.event.world.events.NoRespawnBlockAvailableWorldEvent;
import net.hypejet.jet.world.event.world.events.PlayElderGuardianMobAppearanceWorldEvent;
import net.hypejet.jet.world.event.world.events.PlayPufferfishStingSoundWorldEvent;
import net.hypejet.jet.world.event.world.events.RainLevelChangeWorldEvent;
import net.hypejet.jet.world.event.world.events.StartWaitingForWorldChunksWorldEvent;
import net.hypejet.jet.world.event.world.events.ThunderLevelChangeWorldEvent;
import net.hypejet.jet.world.event.world.events.WinWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerWorldEventPlayPacket a world event play packet}.
 *
 * @since 1.0
 * @see ServerWorldEventPlayPacket
 * @see NetworkWriter
 */
public final class ServerWorldEventPlayPacketWriter implements NetworkWriter<ServerWorldEventPlayPacket> {

    /**
     * An instance of the {@linkplain ServerWorldEventPlayPacketWriter server world event play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerWorldEventPlayPacketWriter INSTANCE = new ServerWorldEventPlayPacketWriter();

    private static final Map<Class<? extends WorldEvent>, WorldEventType<?>> TYPES = new WorldEventTypesBuilder()
            .add(ChangeGameModeWorldEvent.class, new ChangeGameModeWorldEventValueProvider())
            .add(DemoWorldEvent.class, new DemoWorldEventValueProvider())
            .add(EnableLimitedCraftingWorldEvent.class, new EnableLimitedCraftingWorldEventValueProvider())
            .add(EnableRespawnScreenWorldEvent.class, new EnableRespawnScreenWorldEventValueProvider())
            .add(RainLevelChangeWorldEvent.class, new RainLevelChangeWorldEventValueProvider())
            .add(ThunderLevelChangeWorldEvent.class, new ThunderLevelChangeWorldEventValueProvider())
            .add(WinWorldEvent.class, new WinWorldEventValueProvider())
            .add(NoRespawnBlockAvailableWorldEvent.class, new WorldEventValueProvider<>((byte) 0))
            .add(BeginRainingWorldEvent.class, new WorldEventValueProvider<>((byte) 1))
            .add(EndRainingWorldEvent.class, new WorldEventValueProvider<>((byte) 2))
            .add(ArrowHitPlayerWorldEvent.class, new WorldEventValueProvider<>((byte) 6))
            .add(PlayPufferfishStingSoundWorldEvent.class, new WorldEventValueProvider<>((byte) 9))
            .add(PlayElderGuardianMobAppearanceWorldEvent.class, new WorldEventValueProvider<>((byte) 10))
            .add(StartWaitingForWorldChunksWorldEvent.class, new WorldEventValueProvider<>((byte) 13))
            .build();

    private ServerWorldEventPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerWorldEventPlayPacket object) {
        WorldEvent worldEvent = object.worldEvent();
        WorldEventType<?> eventType = TYPES.get(worldEvent.getClass());

        buf.writeByte(eventType.valueProvider().identifier());
        buf.writeFloat(value(eventType, worldEvent)); // Get the value with generics
    }

    private static <WE extends WorldEvent> float value(@NonNull WorldEventType<WE> eventType,
                                                       @NonNull WorldEvent worldEvent) {
        return eventType.valueProvider().value(eventType.worldEventClass().cast(worldEvent));
    }

    /**
     * Represents a builder of {@linkplain Map a map} of world event classes and their
     * {@linkplain WorldEventType world event types}.
     *
     * @since 1.0
     * @see WorldEvent
     * @see WorldEventType
     */
    private static final class WorldEventTypesBuilder {

        private final Map<Class<? extends WorldEvent>, WorldEventType<?>> map = new HashMap<>();

        /**
         * Registers {@linkplain WorldEventType a world event type}.
         *
         * @param worldEventClass a class of world event of the world event type
         * @param valueProvider a value provider of the world event type
         * @return this builder
         * @param <WE> a type of world event of the world event type
         * @since 1.0
         */
        private <WE extends WorldEvent> @NonNull WorldEventTypesBuilder add(
                @NonNull Class<WE> worldEventClass, @NonNull WorldEventValueProvider<WE> valueProvider
        ) {
            this.map.put(worldEventClass, new WorldEventType<>(worldEventClass, valueProvider));
            return this;
        }

        /**
         * Builds the {@linkplain Map map}.
         *
         * @return the map
         * @since 1.0
         */
        private @NonNull Map<Class<? extends WorldEvent>, WorldEventType<?>> build() {
            return Map.copyOf(this.map);
        }
    }

    /**
     * Represents holder of class and value provider of {@linkplain WorldEvent a world event}.
     *
     * @param worldEventClass a class of the world event
     * @param valueProvider a value provider of the world event
     * @param <WE> a type of the world event
     * @since 1.0
     */
    private record WorldEventType<WE extends WorldEvent>(@NonNull Class<WE> worldEventClass,
                                                         @NonNull WorldEventValueProvider<WE> valueProvider) {
        /**
         * Constructs the {@linkplain WorldEventType world event type}.
         *
         * @param worldEventClass a class of the world event
         * @param valueProvider a value provider of the world event
         * @since 1.0
         */
        private WorldEventType {
            Objects.requireNonNull(worldEventClass, "world event class");
            Objects.requireNonNull(valueProvider, "value provider");
        }
    }
}