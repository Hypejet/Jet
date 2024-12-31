package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerRemoveResourcePackPacket;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerRemoveResourcePackPacket a server remove resource pack packet}.
 *
 * @since 1.0
 * @see ServerRemoveResourcePackPacket
 * @see NetworkWriter
 */
public final class ServerRemoveResourcePackPacketWriter implements NetworkWriter<ServerRemoveResourcePackPacket> {

    /**
     * An instance of the {@linkplain ServerRemoveResourcePackPacketWriter server remove resource pack packet writer}.
     *
     * @since 1.0
     */
    public static final ServerRemoveResourcePackPacketWriter INSTANCE = new ServerRemoveResourcePackPacketWriter();

    private ServerRemoveResourcePackPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerRemoveResourcePackPacket object) {
        NetworkUtil.writeOptional(object.uniqueId(), UUIDNetworkCodec.INSTANCE, buf);
    }
}