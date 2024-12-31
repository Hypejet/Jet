package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerAddResourcePackPacket;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerAddResourcePackPacket a server add resource pack packet}.
 *
 * @since 1.0
 * @see ServerAddResourcePackPacket
 * @see NetworkWriter
 */
public final class ServerAddResourcePackPacketWriter implements NetworkWriter<ServerAddResourcePackPacket> {

    /**
     * An instance of the {@linkplain ServerAddResourcePackPacketWriter server add resource pack packet writer}.
     *
     * @since 1.0
     */
    public static final ServerAddResourcePackPacketWriter INSTANCE = new ServerAddResourcePackPacketWriter();

    private static final StringNetworkCodec HASH_CODEC = StringNetworkCodec.create(40);

    private ServerAddResourcePackPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerAddResourcePackPacket object) {
        UUIDNetworkCodec.INSTANCE.write(buf, object.uniqueId());
        StringNetworkCodec.INSTANCE.write(buf, object.url());
        HASH_CODEC.write(buf, object.hash());
        buf.writeBoolean(object.forced());
        NetworkUtil.writeOptional(object.prompt(), ComponentNetworkWriter.INSTANCE, buf);
    }
}