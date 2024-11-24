package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerAddResourcePackConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.kyori.adventure.text.Component;
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

        Component prompt = object.prompt();
        buf.writeBoolean(prompt != null);

        if (prompt != null) {
            ComponentNetworkWriter.INSTANCE.write(buf, object.prompt());
        }
    }
}