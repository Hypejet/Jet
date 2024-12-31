package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerActionBarPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerActionBarPlayPacket an action bar play packet}.
 *
 * @since 1.0
 * @see ServerActionBarPlayPacket
 * @see NetworkWriter
 */
public final class ServerActionBarPlayPacketWriter implements NetworkWriter<ServerActionBarPlayPacket> {

    /**
     * An instance of the {@linkplain ServerActionBarPlayPacketWriter server action bar play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerActionBarPlayPacketWriter INSTANCE = new ServerActionBarPlayPacketWriter();

    private ServerActionBarPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerActionBarPlayPacket object) {
        ComponentNetworkWriter.INSTANCE.write(buf, object.text());
    }
}