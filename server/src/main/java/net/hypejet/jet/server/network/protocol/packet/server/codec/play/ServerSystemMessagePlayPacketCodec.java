package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerSystemMessagePlayPacket system message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerSystemMessagePlayPacket
 * @see PacketCodec
 */
public final class ServerSystemMessagePlayPacketCodec extends PacketCodec<ServerSystemMessagePlayPacket> {

    private static final int MAX_MESSAGE_SIZE = 262144;

    /**
     * Constructs the {@linkplain PacketCodec packet codec}, which reads and writes
     * a {@linkplain ServerSystemMessagePlayPacket system message play packet}.
     *
     * @since 1.0
     */
    public ServerSystemMessagePlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_SYSTEM_MESSAGE, ServerSystemMessagePlayPacket.class);
    }

    @Override
    public @NonNull ServerSystemMessagePlayPacket read(@NonNull ByteBuf buf) {
        int index = buf.readerIndex();
        Component message = NetworkUtil.readComponent(buf);

        if (buf.readerIndex() - index > MAX_MESSAGE_SIZE)
            throw new IllegalArgumentException("The message size is higher than allowed");

        return new ServerSystemMessagePlayPacket(message, buf.readBoolean());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerSystemMessagePlayPacket object) {
        NetworkUtil.writeComponent(buf, object.message());
        if (buf.readableBytes() > MAX_MESSAGE_SIZE)
            throw new IllegalArgumentException("The message size is higher than allowed");
        buf.writeBoolean(object.overlay());
    }
}