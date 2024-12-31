package net.hypejet.jet.server.network.codec.packet.server.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerLoginSuccessLoginPacket.Property;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerLoginSuccessLoginPacket a login success login packet}.
 *
 * @since 1.0
 * @see ServerLoginSuccessLoginPacket
 * @see NetworkWriter
 */
public final class ServerLoginSuccessLoginPacketWriter implements NetworkWriter<ServerLoginSuccessLoginPacket> {

    /**
     * An instance of the {@linkplain ServerLoginSuccessLoginPacketWriter server login success login packet writer}.
     *
     * @since 1.0
     */
    public static final ServerLoginSuccessLoginPacketWriter INSTANCE = new ServerLoginSuccessLoginPacketWriter();

    private static final CollectionNetworkWriter<Property>
            PROPERTIES_CODEC = new CollectionNetworkWriter<>(new PropertyWriter());

    private ServerLoginSuccessLoginPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerLoginSuccessLoginPacket object) {
        UUIDNetworkCodec.INSTANCE.write(buf, object.uniqueId());
        StringNetworkCodec.MAX_16_INSTANCE.write(buf, object.username());
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
            StringNetworkCodec.INSTANCE.write(buf, object.key());
            StringNetworkCodec.INSTANCE.write(buf, object.value());
            NetworkUtil.writeOptional(object.signature(), StringNetworkCodec.INSTANCE, buf);
        }
    }
}