package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerTransferPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerTransferPacket a server
 * transfer packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerTransferPacket
 * @see NetworkWriter
 */
public final class ServerTransferPacketWriter implements NetworkWriter<ServerTransferPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerTransferPacket object) {
        StringNetworkCodec.INSTANCE.write(buf, object.address());
        VarIntNetworkCodec.INSTANCE.write(buf, object.port());
    }
}