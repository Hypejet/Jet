package net.hypejet.jet.server.network.codec.packet.server.play;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.codec.PrimitiveNetworkCodecs;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityAnimationPlayPacket;

import java.util.Map;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntityAnimationPlayPacket server entity animation play packets}.
 *
 * @since 1.0
 * @see ServerEntityAnimationPlayPacket
 * @see NetworkWriter
 */
public final class ServerEntityAnimationPlayPacketWriter implements NetworkWriter<ServerEntityAnimationPlayPacket> {

    private static final NetworkCodec<Player.Animation> ANIMATION_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    (byte) 0, Player.Animation.SWING_MAIN_HAND,
                    (byte) 2, Player.Animation.LEAVE_BED,
                    (byte) 3, Player.Animation.SWING_OFFHAND,
                    (byte) 4, Player.Animation.CRITICAL_HIT,
                    (byte) 5, Player.Animation.MAGIC_CRITICAL_HIT
            )),
            PrimitiveNetworkCodecs.BYTE
    );

    /**
     * An instance of the {@linkplain ServerEntityAnimationPlayPacketWriter server entity animation play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityAnimationPlayPacketWriter INSTANCE = new ServerEntityAnimationPlayPacketWriter();

    private ServerEntityAnimationPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityAnimationPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.entityId());
        ANIMATION_CODEC.write(buf, object.animation());
    }
}
