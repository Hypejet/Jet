package net.hypejet.jet.server.network.codec.packet.server.play;

import java.util.Map;

import net.hypejet.jet.server.registry.JetRegistryManager;
import org.jspecify.annotations.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.PrimitiveNetworkCodecs;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityEventPlayPacket;
import net.hypejet.jet.server.util.index.IndexUtil;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntityEventPlayPacket server entity event play packets}.
 *
 * @since 1.0
 * @see ServerEntityEventPlayPacket
 * @see NetworkWriter
 */
public final class ServerEntityEventPlayPacketWriter implements NetworkWriter<ServerEntityEventPlayPacket> {

    private static final NetworkCodec<Entity.Event> EVENT_CODEC = new IndexNetworkCodec<>(
        IndexUtil.fromMap(
                Map.<Byte, Entity.Event>ofEntries(
                        Map.entry((byte) 0, Entity.Event.ARROW_TIP),
                        Map.entry((byte) 1, Entity.Event.RABBIT_JUMP),
                        Map.entry((byte) 3, Entity.Event.DEATH_ANIMATION),
                        Map.entry((byte) 4, Entity.Event.START_ATTACKING),
                        Map.entry((byte) 6, Entity.Event.TAMING_FAILED),
                        Map.entry((byte) 7, Entity.Event.TAMING_SUCCEEDED),
                        Map.entry((byte) 8, Entity.Event.WETNESS_SHAKING_START),
                        Map.entry((byte) 9, Entity.Event.ITEM_USAGE_FINISHED),
                        Map.entry((byte) 10, Entity.Event.EAT_GRASS_OR_IGNITE),
                        Map.entry((byte) 11, Entity.Event.START_OFFERING_POPPY),
                        Map.entry((byte) 12, Entity.Event.MATING),
                        Map.entry((byte) 13, Entity.Event.VILLAGER_ANGRY),
                        Map.entry((byte) 14, Entity.Event.VILLAGER_HAPPY),
                        Map.entry((byte) 15, Entity.Event.WITCH_MAGIC),
                        Map.entry((byte) 16, Entity.Event.ZOMBIE_VILLAGER_CURE),
                        Map.entry((byte) 17, Entity.Event.FIREWORK_EXPLODE),
                        Map.entry((byte) 18, Entity.Event.ANIMAL_LOVE),
                        Map.entry((byte) 19, Entity.Event.SQUID_ROTATION_RESET),
                        Map.entry((byte) 20, Entity.Event.SPAWN_EXPLOSION_PARTICLES),
                        Map.entry((byte) 21, Entity.Event.GUARDIAN_ATTACK),
                        Map.entry((byte) 22, Entity.Event.REDUCED_DEBUG_INFO),
                        Map.entry((byte) 23, Entity.Event.FULL_DEBUG_INFO),
                        Map.entry((byte) 24, Entity.Event.PERMISSION_LEVEL_0),
                        Map.entry((byte) 25, Entity.Event.PERMISSION_LEVEL_1),
                        Map.entry((byte) 26, Entity.Event.PERMISSION_LEVEL_2),
                        Map.entry((byte) 27, Entity.Event.PERMISSION_LEVEL_3),
                        Map.entry((byte) 28, Entity.Event.PERMISSION_LEVEL_4),
                        Map.entry((byte) 31, Entity.Event.FISHING_ROD_PULL_ENTITY),
                        Map.entry((byte) 32, Entity.Event.ARMOR_STAND_HIT),
                        Map.entry((byte) 34, Entity.Event.STOP_OFFERING_POPPY),
                        Map.entry((byte) 35, Entity.Event.TOTEM_OF_UNDYING_PROTECT),
                        Map.entry((byte) 38, Entity.Event.DOLPHIN_LOOK_FOR_TREASURE),
                        Map.entry((byte) 39, Entity.Event.RAVAGER_STUNNED),
                        Map.entry((byte) 40, Entity.Event.TRUSTING_FAILED),
                        Map.entry((byte) 41, Entity.Event.TRUSTING_SUCCEEDED),
                        Map.entry((byte) 42, Entity.Event.VILLAGER_SWEAT),
                        Map.entry((byte) 45, Entity.Event.FOX_CHEW),
                        Map.entry((byte) 46, Entity.Event.PORTAL_PARTICLES),
                        Map.entry((byte) 53, Entity.Event.HONEY_BLOCK_SLIDE),
                        Map.entry((byte) 54, Entity.Event.HONEY_BLOCK_FALL),
                        Map.entry((byte) 55, Entity.Event.SWAP_HANDS),
                        Map.entry((byte) 56, Entity.Event.WETNESS_SHAKING_STOP),
                        Map.entry((byte) 58, Entity.Event.START_RAM),
                        Map.entry((byte) 59, Entity.Event.STOP_RAM),
                        Map.entry((byte) 60, Entity.Event.DEATH_SMOKE),
                        Map.entry((byte) 61, Entity.Event.TENDRIL_SHAKE),
                        Map.entry((byte) 62, Entity.Event.SONIC_BOOM),
                        Map.entry((byte) 63, Entity.Event.SNIFFER_DIG),
                        Map.entry((byte) 64, Entity.Event.ARMADILLO_SCARED),
                        Map.entry((byte) 66, Entity.Event.SHAKE),
                        Map.entry((byte) 67, Entity.Event.DROWNING_PARTICLES),
                        Map.entry((byte) 69, Entity.Event.RAVAGER_ROAR)
                )
        ),
        PrimitiveNetworkCodecs.BYTE
    );

    /**
     * An instance of the {@linkplain ServerEntityEventPlayPacketWriter server entity event play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityEventPlayPacketWriter INSTANCE = new ServerEntityEventPlayPacketWriter();

    private ServerEntityEventPlayPacketWriter() {};

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerEntityEventPlayPacket object) {
        buf.writeInt(object.entityId());  
        EVENT_CODEC.write(buf, registryManager, object.event());
    }
}