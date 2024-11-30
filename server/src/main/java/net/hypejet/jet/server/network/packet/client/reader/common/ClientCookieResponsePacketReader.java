package net.hypejet.jet.server.network.packet.client.reader.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.common.ClientCookieResponsePacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientCookieResponsePacket a client
 * cookie response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponsePacket
 * @see NetworkReader
 */
public final class ClientCookieResponsePacketReader implements NetworkReader<ClientCookieResponsePacket> {
    @Override
    public @NonNull ClientCookieResponsePacket read(@NonNull ByteBuf buf) {
        return new ClientCookieResponsePacket(PackedKeyNetworkCodec.INSTANCE.read(buf),
                NetworkUtil.readOptional(ByteArrayNetworkReader.INSTANCE, buf));
    }
}