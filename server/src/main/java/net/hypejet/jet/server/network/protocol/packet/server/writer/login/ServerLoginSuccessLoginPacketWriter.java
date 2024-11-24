package net.hypejet.jet.server.network.protocol.packet.server.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket.Property;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerLoginSuccessLoginPacket a login success login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginSuccessLoginPacket
 * @see NetworkWriter
 */
public final class ServerLoginSuccessLoginPacketWriter implements NetworkWriter<ServerLoginSuccessLoginPacket> {

    private static final StringNetworkCodec USERNAME_CODEC = StringNetworkCodec.create(16);
    private static final CollectionNetworkWriter<Property> PROPERTIES_CODEC =
            new CollectionNetworkWriter<>(new PropertyWriter()); // TODO

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerLoginSuccessLoginPacket object) {
        UUIDNetworkCodec.instance().write(buf, object.uniqueId());
        USERNAME_CODEC.write(buf, object.username());
        PROPERTIES_CODEC.write(buf, object.properties());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Property a property}.
     *
     * @since 1.0
     * @see Property
     * @see NetworkWriter
     */
    private static final class PropertyWriter implements NetworkWriter<Property> {
        @Override
        public void write(@NonNull ByteBuf buf, @NonNull Property object) {
            StringNetworkCodec.instance().write(buf, object.key());
            StringNetworkCodec.instance().write(buf, object.value());

            String signature = object.signature();
            buf.writeBoolean(signature != null);

            if (signature != null) {
                StringNetworkCodec.instance().write(buf, signature);
            }
        }
    }
}