package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerSystemMessagePlayPacket a system message play packet}.
 *
 * @since 1.0
 * @see ServerSystemMessagePlayPacket
 * @see NetworkWriter
 */
public final class ServerSystemMessagePlayPacketWriter implements NetworkWriter<ServerSystemMessagePlayPacket> {

    /**
     * An instance of the {@linkplain ServerSystemMessagePlayPacketWriter server system message play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerSystemMessagePlayPacketWriter INSTANCE = new ServerSystemMessagePlayPacketWriter();

    private static final int MAX_MESSAGE_SIZE = 262144;

    private ServerSystemMessagePlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerSystemMessagePlayPacket object) {
        ComponentNetworkWriter.INSTANCE.write(buf, registryManager, object.message());
        if (buf.readableBytes() > MAX_MESSAGE_SIZE)
            throw new IllegalArgumentException("The message size is higher than allowed");
        buf.writeBoolean(object.overlay());
    }
}