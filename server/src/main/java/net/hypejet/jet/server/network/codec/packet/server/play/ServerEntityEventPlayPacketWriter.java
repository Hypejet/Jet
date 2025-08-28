package net.hypejet.jet.server.network.codec.packet.server.play;

import java.util.Map;

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
public class ServerEntityEventPlayPacketWriter implements NetworkWriter<ServerEntityEventPlayPacket> {

    // Little bit spammy
    private static final NetworkCodec<Entity.Status> STATUS_CODEC = new IndexNetworkCodec<>(
        IndexUtil.fromMap(
            Map.<Byte, Entity.Status>ofEntries(
                Map.entry((byte) 0, Entity.Status.ARROW_TIP),
                Map.entry((byte) 1, Entity.Status.PROJECTILE_HIT),
                Map.entry((byte) 3, Entity.Status.DEATH_ANIMATION),
                Map.entry((byte) 4, Entity.Status.ATTACK),
                Map.entry((byte) 6, Entity.Status.TAMING_FAIL),
                Map.entry((byte) 7, Entity.Status.TAMING_SUCCESS),
                Map.entry((byte) 8, Entity.Status.WOLF_SHAKE),
                Map.entry((byte) 9, Entity.Status.PLAYER_ITEM_USE_FINISH),
                Map.entry((byte) 10, Entity.Status.SHEEP_EAT),
                Map.entry((byte) 11, Entity.Status.GOLEM_HOLD_POPPY),
                Map.entry((byte) 12, Entity.Status.VILLAGER_MATING),
                Map.entry((byte) 13, Entity.Status.VILLAGER_ANGRY),
                Map.entry((byte) 14, Entity.Status.VILLAGER_HAPPY),
                Map.entry((byte) 15, Entity.Status.WITCH_MAGIC),
                Map.entry((byte) 16, Entity.Status.ZOMBIE_VILLAGER_CURE),
                Map.entry((byte) 17, Entity.Status.FIREWORK_EXPLODE),
                Map.entry((byte) 18, Entity.Status.ANIMAL_LOVE),
                Map.entry((byte) 19, Entity.Status.SQUID_ROTATION_RESET),
                Map.entry((byte) 20, Entity.Status.MOB_EXPLOSION),
                Map.entry((byte) 21, Entity.Status.GUARDIAN_ATTACK),
                Map.entry((byte) 22, Entity.Status.PLAYER_DEBUG_REDUCED),
                Map.entry((byte) 23, Entity.Status.PLAYER_DEBUG_NORMAL),
                Map.entry((byte) 24, Entity.Status.PLAYER_OP_0),
                Map.entry((byte) 25, Entity.Status.PLAYER_OP_1),
                Map.entry((byte) 26, Entity.Status.PLAYER_OP_2),
                Map.entry((byte) 27, Entity.Status.PLAYER_OP_3),
                Map.entry((byte) 28, Entity.Status.PLAYER_OP_4),
                Map.entry((byte) 29, Entity.Status.SHIELD_BLOCK),
                Map.entry((byte) 30, Entity.Status.SHIELD_BREAK),
                Map.entry((byte) 31, Entity.Status.FISHING_PULL_PLAYER),
                Map.entry((byte) 32, Entity.Status.ARMOR_STAND_HIT),
                Map.entry((byte) 34, Entity.Status.GOLEM_PUTAWAY_POPPY),
                Map.entry((byte) 35, Entity.Status.TOTEM_OF_UNDYING),
                Map.entry((byte) 38, Entity.Status.DOLPHIN_HAPPY),
                Map.entry((byte) 39, Entity.Status.RAVAGER_STUNNED),
                Map.entry((byte) 40, Entity.Status.OCELOT_TAMING_FAIL),
                Map.entry((byte) 41, Entity.Status.OCELOT_TAMING_SUCCESS),
                Map.entry((byte) 42, Entity.Status.VILLAGER_SPLASH),
                Map.entry((byte) 43, Entity.Status.PLAYER_BAD_OMEN_CLOUD),
                Map.entry((byte) 45, Entity.Status.FOX_CHEW),
                Map.entry((byte) 46, Entity.Status.MAIN_HAND_BREAK),
                Map.entry((byte) 47, Entity.Status.OFF_HAND_BREAK),
                Map.entry((byte) 48, Entity.Status.HEAD_BREAK),
                Map.entry((byte) 49, Entity.Status.CHEST_BREAK),
                Map.entry((byte) 50, Entity.Status.LEGS_BREAK),
                Map.entry((byte) 51, Entity.Status.FEET_BREAK),
                Map.entry((byte) 53, Entity.Status.HONEY_BLOCK_SLIDE),
                Map.entry((byte) 54, Entity.Status.HONEY_BLOCK_FALL),
                Map.entry((byte) 55, Entity.Status.SWAP_HANDS),
                Map.entry((byte) 56, Entity.Status.WOLF_SHAKE_STOP),
                Map.entry((byte) 58, Entity.Status.GOAT_RAM),
                Map.entry((byte) 59, Entity.Status.GOAT_STOP_RAM),
                Map.entry((byte) 60, Entity.Status.DEATH_SMOKE),
                Map.entry((byte) 61, Entity.Status.WARDEN_TENDRIL_SHAKE),
                Map.entry((byte) 62, Entity.Status.WARDEN_SONIC_BOOM),
                Map.entry((byte) 63, Entity.Status.SNIFFER_DIG)
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
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityEventPlayPacket object) {
        buf.writeInt(object.entityId());  
        STATUS_CODEC.write(buf, object.status());
    }
}
