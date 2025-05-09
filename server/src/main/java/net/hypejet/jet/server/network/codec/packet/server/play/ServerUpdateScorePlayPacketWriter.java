package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.codec.game.scoreboard.score.number.NumberFormatNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateScorePlayPacket;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}
 * of {@linkplain ServerUpdateScorePlayPacket a server update score play packet}.
 *
 * @since 1.0
 * @see ServerUpdateScorePlayPacket
 * @see NetworkWriter
 */
public final class ServerUpdateScorePlayPacketWriter implements NetworkWriter<ServerUpdateScorePlayPacket> {
    /**
     * An instance of the {@linkplain ServerUpdateScorePlayPacketWriter server update score play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerUpdateScorePlayPacketWriter INSTANCE = new ServerUpdateScorePlayPacketWriter();

    private ServerUpdateScorePlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerUpdateScorePlayPacket object) {
        StringNetworkCodec.INSTANCE.write(buf, object.entityName());
        StringNetworkCodec.INSTANCE.write(buf, object.objectiveName());
        VarIntNetworkCodec.INSTANCE.write(buf, object.score());
        NetworkUtil.writeOptional(object.displayName(), ComponentNetworkWriter.INSTANCE, buf);
        NetworkUtil.writeOptional(object.numberFormat(), NumberFormatNetworkWriter.INSTANCE, buf);
    }
}