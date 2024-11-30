package net.hypejet.jet.server.network.packet.client.reader.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.common.ClientPluginMessagePacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientPluginMessagePacket a client
 * plugin message packet}.
 *
 * @since 1.0
 * @see ClientPluginMessagePacket
 * @see NetworkReader
 */
public final class ClientPluginMessagePacketReader implements NetworkReader<ClientPluginMessagePacket> {
    @Override
    public @NonNull ClientPluginMessagePacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessagePacket(PackedKeyNetworkCodec.INSTANCE.read(buf),
                NetworkUtil.readRemainingBytes(buf));
    }
}