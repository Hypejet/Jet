package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientCookieResponseConfigurationPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientCookieResponseConfigurationPacket cookie response configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponseConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientCookieResponseConfigurationPacketCodec
        extends ClientPacketCodec<ClientCookieResponseConfigurationPacket> {
    /**
     * Constructs the {@linkplain ClientCookieResponseConfigurationPacketCodec cookie response configuration packet
     * codec}.
     *
     * @since 1.0
     */
    public ClientCookieResponseConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_COOKIE_RESPONSE, ClientCookieResponseConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientCookieResponseConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientCookieResponseConfigurationPacket(
                NetworkUtil.readIdentifier(buf),
                buf.readBoolean() ? NetworkUtil.readByteArray(buf) : null
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientCookieResponseConfigurationPacket object) {
        NetworkUtil.writeIdentifier(buf, object.identifier());

        byte[] data = object.data();
        buf.writeBoolean(data != null);

        if (data != null) {
            NetworkUtil.writeByteArray(buf, data);
        }
    }

    @Override
    public void handle(@NonNull ClientCookieResponseConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}
