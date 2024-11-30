package net.hypejet.jet.server.network.packet.client.reader.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.login.ClientPluginMessageResponseLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientPluginMessageResponseLoginPacket a plugin message response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessageResponseLoginPacket
 * @see NetworkReader
 */
public final class ClientPluginMessageResponseLoginPacketReader
        implements NetworkReader<ClientPluginMessageResponseLoginPacket> {
    @Override
    public @NonNull ClientPluginMessageResponseLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessageResponseLoginPacket(
                VarIntNetworkCodec.INSTANCE.read(buf),
                buf.readBoolean(),
                NetworkUtil.readRemainingBytes(buf)
        );
    }
}