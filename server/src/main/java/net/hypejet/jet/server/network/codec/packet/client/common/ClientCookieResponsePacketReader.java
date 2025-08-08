package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.codec.game.key.KeyNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientCookieResponsePacket;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientCookieResponsePacket a client cookie response packet}.
 *
 * @since 1.0
 * @see ClientCookieResponsePacket
 * @see NetworkReader
 */
public final class ClientCookieResponsePacketReader implements NetworkReader<ClientCookieResponsePacket> {

    /**
     * An instance of the {@linkplain ClientCookieResponsePacketReader client cookie response packet reader}.
     *
     * @since 1.0
     */
    public static final ClientCookieResponsePacketReader INSTANCE = new ClientCookieResponsePacketReader();

    private ClientCookieResponsePacketReader() {}

    @Override
    public @NonNull ClientCookieResponsePacket read(@NonNull ByteBuf buf) {
        return new ClientCookieResponsePacket(
                KeyNetworkCodec.INSTANCE.read(buf),
                NetworkUtil.readOptional(ByteArrayNetworkReader.INSTANCE, buf)
        );
    }
}