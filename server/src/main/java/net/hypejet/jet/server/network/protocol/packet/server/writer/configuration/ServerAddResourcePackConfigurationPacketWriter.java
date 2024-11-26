package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerAddResourcePackConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerAddResourcePackConfigurationPacket an add resource pack configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerAddResourcePackConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerAddResourcePackConfigurationPacketWriter
        implements NetworkWriter<ServerAddResourcePackConfigurationPacket> {

    private static final StringNetworkCodec HASH_CODEC = StringNetworkCodec.create(40);

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerAddResourcePackConfigurationPacket object) {
        UUIDNetworkCodec.INSTANCE.write(buf, object.uniqueId());
        StringNetworkCodec.INSTANCE.write(buf, object.url());
        HASH_CODEC.write(buf, object.hash());
        buf.writeBoolean(object.forced());
        NetworkUtil.writeOptional(object.prompt(), ComponentNetworkWriter.INSTANCE, buf);
    }
}