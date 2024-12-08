package net.hypejet.jet.server.network.codec.packet.server.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerEncryptionRequestLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerEncryptionRequestLoginPacket an encryption request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEncryptionRequestLoginPacket
 * @see NetworkWriter
 */
public final class ServerEncryptionRequestLoginPacketWriter
        implements NetworkWriter<ServerEncryptionRequestLoginPacket> {

    private static final StringNetworkCodec SERVER_ID_CODEC = StringNetworkCodec.create(20);

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEncryptionRequestLoginPacket object) {
        SERVER_ID_CODEC.write(buf, object.serverId());
        ByteArrayNetworkWriter.INSTANCE.write(buf, object.publicKey().array());
        ByteArrayNetworkWriter.INSTANCE.write(buf, object.verifyToken().array());
        buf.writeBoolean(object.shouldAuthenticate());
    }
}
