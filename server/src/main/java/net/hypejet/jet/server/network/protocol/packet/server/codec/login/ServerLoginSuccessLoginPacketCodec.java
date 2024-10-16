package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket.Property;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec server packet codec}, which reads and writes
 * a {@linkplain ServerLoginSuccessLoginPacket login success login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginSuccessLoginPacket
 * @see PacketCodec
 */
public final class ServerLoginSuccessLoginPacketCodec extends PacketCodec<ServerLoginSuccessLoginPacket> {

    private static final StringNetworkCodec USERNAME_CODEC = StringNetworkCodec.create(16);
    private static final CollectionNetworkCodec<Property> PROPERTIES_CODEC = CollectionNetworkCodec
            .create(new PropertyCodec());

    /**
     * Constructs the {@linkplain ServerLoginSuccessLoginPacketCodec login success packet codec}.
     *
     * @since 1.0
     */
    public ServerLoginSuccessLoginPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_SUCCESS, ServerLoginSuccessLoginPacket.class);
    }

    @Override
    public @NonNull ServerLoginSuccessLoginPacket read(@NonNull ByteBuf buf) {
        return new ServerLoginSuccessLoginPacket(UUIDNetworkCodec.instance().read(buf), USERNAME_CODEC.read(buf),
                PROPERTIES_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerLoginSuccessLoginPacket object) {
        UUIDNetworkCodec.instance().write(buf, object.uniqueId());
        USERNAME_CODEC.write(buf, object.username());
        PROPERTIES_CODEC.write(buf, object.properties());
    }

    private static final class PropertyCodec implements NetworkCodec<Property> {
        @Override
        public @NonNull Property read(@NonNull ByteBuf buf) {
            return new Property(StringNetworkCodec.instance().read(buf), StringNetworkCodec.instance().read(buf),
                    buf.readBoolean() ? StringNetworkCodec.instance().read(buf) : null);
        }

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